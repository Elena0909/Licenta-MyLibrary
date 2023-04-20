package com.example.mylibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mylibrary.R;
import com.example.mylibrary.fragments.welcomeActivity.LoginFragment;
import com.example.mylibrary.fragments.welcomeActivity.ProfileFragment;
import com.example.mylibrary.fragments.welcomeActivity.RegisterFragment;
import com.example.mylibrary.fragments.welcomeActivity.WelcomeFragment;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;

import static com.example.mylibrary.fragments.welcomeActivity.LoginFragment.TAG_FRAGMENT_LOGIN;
import static com.example.mylibrary.fragments.welcomeActivity.ProfileFragment.TAG_FRAGMENT_PROFILE;
import static com.example.mylibrary.fragments.welcomeActivity.RegisterFragment.TAG_FRAGMENT_REGISTER;
import static com.example.mylibrary.fragments.welcomeActivity.WelcomeFragment.TAG_FRAGMENT_WELCOME;


public class WelcomeActivity extends AppCompatActivity implements OnFragmentActivityCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        onAddWelcomeFragment();
    }

    private void onAddWelcomeFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_container, WelcomeFragment.newInstance(), TAG_FRAGMENT_WELCOME);
        fragmentTransaction.commit();
    }

    @Override
    public void onAddFragment(String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment;

        switch (TAG){
            case TAG_FRAGMENT_REGISTER:
            {
                fragment = RegisterFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_LOGIN:
            {
                fragment = LoginFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_WELCOME:
            {
                fragment = WelcomeFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_PROFILE:
            {
                fragment = ProfileFragment.newInstance();
                break;
            }
            default:
                return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_container, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();

    }

    @Override
    public void onReplaceFragment(String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment;

        switch (TAG)
        {
            case TAG_FRAGMENT_REGISTER:
            {
                fragment = RegisterFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_LOGIN:
            {
                fragment = LoginFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_WELCOME:
            {
                fragment = WelcomeFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_PROFILE:
            {
                fragment = ProfileFragment.newInstance();
                break;
            }
            default:
                return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}