package com.zub.covid_19;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    final Fragment homeFragment = new HomeFragment();
    final Fragment statsFragment = new StatsFragment();
    final Fragment mapsFragment = new MapsFragment();
    final Fragment infoFragment = new InfoFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
        }
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

//        fragmentManager.beginTransaction().add(R.id.fragment_container, infoFragment, "3").hide(infoFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.fragment_container, statsFragment, "2").hide(statsFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public void loadLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "");
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(language.toLowerCase()));
        } else {
            configuration.locale = new Locale(language.toLowerCase());
        }
        resources.updateConfiguration(configuration,displayMetrics);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            fragment = new HomeFragment();
                            break;
                        case R.id.nav_stats:
                            fragment = new StatsFragment();
                            break;
                        case R.id.nav_maps:
                            fragment = new MapsFragment();
                            break;
                        case R.id.nav_info:
                            fragment = new InfoFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                    return true;
                }
            };

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.nav_home:
//                fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
//                active = homeFragment;
//                return true;
//
//            case R.id.nav_stats:
//                fragmentManager.beginTransaction().hide(active).show(statsFragment).commit();
//                active = statsFragment;
//                return true;
//
//            case R.id.nav_info:
//                fragmentManager.beginTransaction().hide(active).show(infoFragment).commit();
//                active = infoFragment;
//                return true;
//
//        }
//        return false;
//    }
//
//    public static void refreshFragments(){
//
//        final Fragment accountFragment = new HomeFragment();
//
//    }

}
