package com.zub.covid_19;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.zub.covid_19.adapter.ProvAdapter;
import com.zub.covid_19.api.provData.ProvData;
import com.zub.covid_19.vm.ProvDataViewModel;

import org.xmlpull.v1.XmlPullParser;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //PROV DATA WIDGET

        ShimmerFrameLayout mProvCardShimmer = view.findViewById(R.id.prov_shimmer);

        RecyclerView mProvRecyclerView = view.findViewById(R.id.prov_rv);

        //PROV DATA FETCHING

        ProvDataViewModel provDataViewModel;

        provDataViewModel = ViewModelProviders.of(this).get(ProvDataViewModel.class);
        provDataViewModel.init();

        provDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showLoading(mProvCardShimmer, mProvRecyclerView);
                } else {
                    hideLoading(mProvCardShimmer, mProvRecyclerView);
                }
            }
        });

        provDataViewModel.getRegulerData().observe(this, new Observer<ProvData>() {
            @Override
            public void onChanged(ProvData provData) {
                //TODO
                Log.d(TAG, "onChanged: " + provData.getCurrentData());
                setupRecyclerView(mProvRecyclerView, provData);
            }

        });
        
        return view;
    }

    private void setupRecyclerView(RecyclerView mProvRecyclerView, ProvData provData) {

        ArrayList<String> provName = new ArrayList<>();
        ArrayList<Integer> provCase = new ArrayList<>();
        ArrayList<Integer> provDeath = new ArrayList<>();
        ArrayList<Integer> provCured = new ArrayList<>();
        ArrayList<Integer> provTreated = new ArrayList<>();

        List<ProvData.ProvListData> provListData = provData.getProvListDataLists();
        for(ProvData.ProvListData theProvData : provListData) {

            Log.d(TAG, "setupRecyclerView: " + theProvData.getProvName());
            provName.add(theProvData.getProvName());
            provCase.add(theProvData.getCaseAmount());
            provDeath.add(theProvData.getDeathAmount());
            provCured.add(theProvData.getHealedAmount());
            provTreated.add(theProvData.getTreatedAmount());
            ProvAdapter provAdapter = new ProvAdapter(provName, provCase, provDeath, provCured, provTreated, this.getContext());
            mProvRecyclerView.setAdapter(provAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            mProvRecyclerView.setLayoutManager(linearLayoutManager);
        }

    }

    private void hideLoading(ShimmerFrameLayout mProvCardShimmer, RecyclerView mProvRecyclerView) {

        mProvCardShimmer.setVisibility(View.GONE);

        mProvRecyclerView.setVisibility(View.VISIBLE);

    }

    private void showLoading(ShimmerFrameLayout mProvCardShimmer, RecyclerView mProvRecyclerView) {

        mProvCardShimmer.setVisibility(View.VISIBLE);

        mProvRecyclerView.setVisibility(View.GONE);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = view.findViewById(R.id.prov_map_view);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
    }

}
