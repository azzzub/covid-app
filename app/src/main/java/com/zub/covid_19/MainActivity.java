package com.zub.covid_19;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
//    public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    final Fragment homeFragment = new HomeFragment();
    final Fragment statsFragment = new StatsFragment();
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
