package com.zub.covid_19;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.zub.covid_19.adapter.ProvAdapter;
import com.zub.covid_19.api.provData.ProvData;
import com.zub.covid_19.vm.ProvDataViewModel;

import org.xmlpull.v1.XmlPullParser;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends Fragment implements
        OnMapReadyCallback,
        ProvAdapter.ListClickedListener {

    private static final String TAG = "MapsFragment";

    private ProvAdapter provAdapter;

    private ArrayList<ProvData.ProvListData> provListData = new ArrayList<>();

    private ArrayList<ProvData.ProvListData> filteredList = new ArrayList<>();;

    private GoogleMap googleMap;

    private SlidingUpPanelLayout mSlideUpLayout;

    private EditText mFilter;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mSlideUpLayout = view.findViewById(R.id.sliding_layout);

        mFilter = view.findViewById(R.id.prov_filter);

        mFilter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mSlideUpLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideUpLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

        mFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!provListData.isEmpty()) {
                    filter(editable.toString());
                }
            }
        });

        return view;
    }

    private void filter(String toString) {
        filteredList.clear();
        for (ProvData.ProvListData theProvData : provListData) {
            if (theProvData.getProvName().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(theProvData);
            }
        }
        provAdapter.filterList(filteredList);
    }

    private void setupRecyclerView(RecyclerView mProvRecyclerView, ProvData provData) {

        List<ProvData.ProvListData> provListData = provData.getProvListDataLists();

        this.provListData.addAll(provListData);
        this.filteredList.addAll(provListData);

        provAdapter = new ProvAdapter(this.provListData, this);
        mProvRecyclerView.setAdapter(provAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        mProvRecyclerView.setLayoutManager(linearLayoutManager);

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
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));

        this.googleMap = googleMap;

        final double LAT = -8.0675589d;
        final double LNG = 120.9046018d;
        final float ZOOM = 3.17f;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(LAT, LNG), ZOOM);

        googleMap.moveCamera(cameraUpdate);

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getContext(), R.raw.style_json));

        //PROV DATA WIDGET

        ShimmerFrameLayout mProvCardShimmer = Objects.requireNonNull(this.getView()).findViewById(R.id.prov_shimmer);

        RecyclerView mProvRecyclerView = this.getView().findViewById(R.id.prov_rv);

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
                List<ProvData.ProvListData> provListData = provData.getProvListDataLists();
                for (ProvData.ProvListData theProvListData : provListData) {
                    double lat = theProvListData.getProvDataLocation().getLat();
                    double lng = theProvListData.getProvDataLocation().getLng();
                    LatLng latLng = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(latLng));
                }
                setupRecyclerView(mProvRecyclerView, provData);
            }

        });

    }

    @Override
    public void onListClicked(int position) {
        double LAT = filteredList.get(position).getProvDataLocation().getLat();
        double LNG = filteredList.get(position).getProvDataLocation().getLng();
        final float ZOOM = 7;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(LAT, LNG), ZOOM);

        googleMap.animateCamera(cameraUpdate, 1000, null);

        mSlideUpLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

    }
}