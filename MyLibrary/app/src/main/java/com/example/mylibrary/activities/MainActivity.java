package com.example.mylibrary.activities;

import static com.example.mylibrary.fragments.mainActivity.HomeFragment.TAG_FRAGMENT_HOME;
import static com.example.mylibrary.fragments.welcomeActivity.ProfileFragment.TAG_FRAGMENT_PROFILE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mylibrary.R;
import com.example.mylibrary.fragments.mainActivity.BooksAroundTheWorldFragment;
import com.example.mylibrary.fragments.mainActivity.EditProfileFragment;
import com.example.mylibrary.fragments.mainActivity.HomeFragment;
import com.example.mylibrary.fragments.mainActivity.stats.YourYearFragment;
import com.example.mylibrary.fragments.welcomeActivity.LoginFragment;
import com.example.mylibrary.fragments.mainActivity.MyBooksFragment;
import com.example.mylibrary.fragments.welcomeActivity.ProfileFragment;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.utils.BitmapConverter;
import com.example.mylibrary.utils.Status;
import com.example.mylibrary.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Base64;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Fragment fragment;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CoordinatorLayout coordLay;
    private FragmentManager fm;
    private Long status;
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        status = (Long) i.getLongExtra("status", 0);

        setContentView(R.layout.activity_main);
        ActionBar toolbar = getSupportActionBar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        coordLay = (CoordinatorLayout) findViewById(R.id.coordLay);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.app_name, R.string.app_name);


        Toolbar tb = findViewById(R.id.toolbar);


        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);


        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //getUserDetails();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        navUsername.setText(Utils.getUsername());
        ImageView userIcon = headerView.findViewById(R.id.icon);


        if (Utils.getUser() != null && !Utils.getUser().getImage().isEmpty()) {
            byte[] decode = Base64.getDecoder().decode(Utils.getUser().getImage());
            Bitmap bitmap = BitmapConverter.fromBytes(decode);
            userIcon.setImageBitmap(bitmap);
        } else
            userIcon.setImageDrawable(getDrawable(R.drawable.m));


        // disply home button for actionbar
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#ffcb4e"));
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundDrawable(cd);


        // navigation view select home menu by default
        //navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setCheckedItem(R.id.nav_home);

        fm = getSupportFragmentManager();

        if (status != 0) {
            navigationView.setCheckedItem(R.id.nav_my_books);
            FragmentTransaction t = fm.beginTransaction();
            fragment = new MyBooksFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("status", status);
            fragment.setArguments(bundle);
            t.replace(R.id.content_frame, fragment);
            t.commit();
            return;
        }

        if (savedInstanceState == null) {

            FragmentTransaction t = fm.beginTransaction();
            fragment = new HomeFragment();
            t.replace(R.id.content_frame, fragment);
            t.commit();
            return;

        }
        fragment = (Fragment) fm.findFragmentById(R.id.content_frame);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Open Close Drawer Layout
        if (drawer.isOpen()) {
            drawer.closeDrawers();
        } else {
            drawer.openDrawer(Gravity.LEFT);
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        FragmentTransaction t = fm.beginTransaction();
        switch (id) {
            case R.id.nav_home:
                //FragmentTransaction t = fm.beginTransaction();
                fragment = new HomeFragment();
                t.replace(R.id.content_frame, fragment);
                t.commit();
                Snackbar.make(coordLay, "Clicked on Search", Snackbar.LENGTH_LONG).show();
                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_profile:
                fragment = new EditProfileFragment();
                t.replace(R.id.content_frame, fragment);
                t.commit();
                Snackbar.make(coordLay, "Clicked on Profile", Snackbar.LENGTH_LONG).show();
                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_stats:
                /* Add Fragment for Settings **/
                fragment = new YourYearFragment();
                t.replace(R.id.content_frame, fragment);
                t.commit();
                Snackbar.make(coordLay, "Clicked on Stats", Snackbar.LENGTH_SHORT).show();
                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_my_books:
                fragment = new MyBooksFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("status", Status.WANT);
                fragment.setArguments(bundle);
                t.replace(R.id.content_frame, fragment);
                t.commit();
                Snackbar.make(coordLay, "Clicked on My Books", Snackbar.LENGTH_LONG).show();
                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_maps:
                fragment = new BooksAroundTheWorldFragment();
                t.replace(R.id.content_frame, fragment);
                t.commit();
                Snackbar.make(coordLay, "Clicked on Maps", Snackbar.LENGTH_LONG).show();
                navigationView.setCheckedItem(id);
                break;
            case R.id.nav_logout:
                Utils.logout();
                Intent i = new Intent(this, WelcomeActivity.class);
                startActivity(i);
                this.finish();
                break;
        }

        // close drawer after clicking the menu item
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        navigationView.setCheckedItem(R.id.nav_home);
        Fragment fragment = HomeFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, TAG_FRAGMENT_HOME);
        fragmentTransaction.commit();




        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit/logout", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}