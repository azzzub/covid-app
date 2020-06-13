package com.zub.covid_19;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final Fragment homeFragment = new HomeFragment();
    final Fragment statsFragment = new StatsFragment();
    final Fragment mapsFragment = new MapsFragment();
    final Fragment infoFragment = new InfoFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
