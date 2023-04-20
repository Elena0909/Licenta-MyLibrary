package com.example.mylibrary.activities;

import static com.example.mylibrary.fragments.bookDetailsActivity.BookDetailsFragment.TAG_FRAGMENT_BOOK_DETAILS;
import static com.example.mylibrary.fragments.welcomeActivity.LoginFragment.TAG_FRAGMENT_LOGIN;
import static com.example.mylibrary.fragments.welcomeActivity.ProfileFragment.TAG_FRAGMENT_PROFILE;
import static com.example.mylibrary.fragments.welcomeActivity.WelcomeFragment.TAG_FRAGMENT_WELCOME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.mylibrary.R;
import com.example.mylibrary.fragments.bookDetailsActivity.BookDetailsFragment;
import com.example.mylibrary.fragments.welcomeActivity.LoginFragment;
import com.example.mylibrary.fragments.welcomeActivity.ProfileFragment;
import com.example.mylibrary.fragments.welcomeActivity.WelcomeFragment;
import com.example.mylibrary.interfaces.OnFragmentActivityCommunication;
import com.example.mylibrary.requestModel.Book;

import java.io.Serializable;

public class BookDetailsActivity extends AppCompatActivity implements OnFragmentActivityCommunication {

    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Intent i = getIntent();
        book = (Book) i.getSerializableExtra("book");

        onAddWelcomeFragment();
    }

    private void onAddWelcomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        fragment = BookDetailsFragment.newInstance();
        Bundle bundle= new Bundle();
        bundle.putSerializable("book", (Serializable) book);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fl_container, fragment, TAG_FRAGMENT_BOOK_DETAILS);
        fragmentTransaction.commit();
    }

    @Override
    public void onAddFragment(String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment;

        switch (TAG) {
            case TAG_FRAGMENT_BOOK_DETAILS: {
                fragment = BookDetailsFragment.newInstance();
                Bundle bundle= new Bundle();
                bundle.putSerializable("book", (Serializable) book);
                fragment.setArguments(bundle);
                break;
            }
            case TAG_FRAGMENT_LOGIN: {
                fragment = LoginFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_WELCOME: {
                fragment = WelcomeFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_PROFILE: {
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

        switch (TAG) {
            case TAG_FRAGMENT_BOOK_DETAILS: {
                fragment = BookDetailsFragment.newInstance();
                Bundle bundle= new Bundle();
                bundle.putSerializable("book", (Serializable) book);
                fragment.setArguments(bundle);
                break;
            }
            case TAG_FRAGMENT_LOGIN: {
                fragment = LoginFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_WELCOME: {
                fragment = WelcomeFragment.newInstance();
                break;
            }
            case TAG_FRAGMENT_PROFILE: {
                fragment = ProfileFragment.newInstance();
                break;
            }
            default:
                return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment, TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}