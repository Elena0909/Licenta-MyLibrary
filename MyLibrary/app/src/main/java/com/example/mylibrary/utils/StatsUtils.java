package com.example.mylibrary.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mylibrary.R;
import com.example.mylibrary.fragments.mainActivity.HomeFragment;

public class StatsUtils {

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private FragmentManager fragmentManager;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    public StatsUtils(FragmentManager fragmentManager, Context context) {
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public void goTo(Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    public void showWarningDialog(int warning) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_warning);

        TextView info = dialog.findViewById(R.id.info);
        Button search = dialog.findViewById(R.id.search);
        Button change = dialog.findViewById(R.id.change);

        info.setText(warning);

        search.setOnClickListener(v -> {
            goTo(new HomeFragment());
            dialog.dismiss();
        });

        change.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
