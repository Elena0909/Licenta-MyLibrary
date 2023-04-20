package com.example.mylibrary.fragments.mainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.adapters.UserBookAdapter;
import com.example.mylibrary.api.StatsApi;
import com.example.mylibrary.reotrfit.RetrofitService;
import com.example.mylibrary.requestModel.Nationality;
import com.example.mylibrary.requestModel.UserBook;
import com.example.mylibrary.utils.ErrorManager;
import com.example.mylibrary.utils.StatsUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksAroundTheWorldFragment extends Fragment {

    Map<String, List<UserBook>> userBooks;
    String books;
    RecyclerView recyclerView;
    EditText yearET;
    Integer year;
    private StatsUtils statsUtils;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onMapReady(GoogleMap googleMap) {
            RetrofitService retrofitService = new RetrofitService();
            StatsApi statsApi = retrofitService.getRetrofit().create(StatsApi.class);


            statsApi.getAllBooksRead(year).enqueue(new Callback<Map<String, List<UserBook>>>() {
                @Override
                public void onResponse(Call<Map<String, List<UserBook>>> call, Response<Map<String, List<UserBook>>> response) {
                    if (response.isSuccessful()) {

                        if (response.body() == null) {
                            if (getContext() != null)
                                Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                            return;
                        }

                        userBooks = response.body();
                        if (userBooks.isEmpty() && getView() != null)
                            statsUtils.showWarningDialog(R.string.warning);

                        googleMap.clear();
                        UserBookAdapter bookAdapter = new UserBookAdapter(new ArrayList<>());
                        recyclerView.setAdapter(bookAdapter);

                        Map<String, List<UserBook>> map = new HashMap<>();
                        userBooks.forEach((s, userBooks1) -> {
                            Nationality nationality = userBooks1.get(0).getBook().getAuthors().get(0).getNationality();
                            books = "";
                            userBooks1.forEach(userBook -> {
                                books += userBook.getBook().getName() + "\n";
                            });
                            LatLng coords = new LatLng(nationality.getLatitude(), nationality.getLongitude());

                            Marker marker = googleMap.addMarker(new MarkerOptions().position(coords).title(userBooks1.size() + " books").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                            if (marker == null) {
                                if (getContext() != null)
                                    Toast.makeText(getContext(), getString(R.string.object_null), Toast.LENGTH_LONG).show();
                                return;
                            }
                            map.put(marker.getId(), userBooks1);
                        });

                        userBooks.clear();
                        userBooks = map;
                        return;
                    }
                    ErrorManager.getErrorMessage(getContext(), response);
                }

                @Override
                public void onFailure(Call<Map<String, List<UserBook>>> call, Throwable t) {
                    ErrorManager.failCall(getContext(), t);
                }
            });

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    List<UserBook> books = userBooks.get(marker.getId());
                    UserBookAdapter bookAdapter = new UserBookAdapter(books);
                    recyclerView.setAdapter(bookAdapter);

                    return false;
                }
            });
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books_around_the_world, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statsUtils = new StatsUtils(getParentFragmentManager(), getContext());

        recyclerView = view.findViewById(R.id.books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        recyclerView.setHasFixedSize(true);

        yearET = view.findViewById(R.id.year);
        LocalDate now = LocalDate.now();
        year = LocalDate.now().getYear();
        yearET.setText(String.valueOf(now.getYear()));


        changeYear();


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeYear() {
        yearET.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (yearET.getText().toString().matches(getString(R.string.regex_year))) {
                    int y = Integer.parseInt(yearET.getText().toString());


                    LocalDate now = LocalDate.now();
                    if (y > now.getYear()) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.future) + "</b></font>"), Toast.LENGTH_LONG).show();
                            return true;
                        }

                        return true;
                    }

                    year = y;

                    SupportMapFragment mapFragment =
                            (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(callback);
                    }
                    return true;
                }
                if (getContext() != null)
                    Toast.makeText(getContext(), Html.fromHtml("<font color='#9E0000' ><b>" + getString(R.string.year_invalid) + "</b></font>"), Toast.LENGTH_LONG).show();

                return true;
            }
            return false;
        });
    }

}