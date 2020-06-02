package com.zub.covid_19;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.zub.covid_19.stats.Features;
import com.zub.covid_19.stats.Stats;
import com.zub.covid_19.stats.StatsHolder;
import com.zub.covid_19.stats.perStateAPI.FeaturesPerState;
import com.zub.covid_19.stats.perStateAPI.StatsPerState;
import com.zub.covid_19.stats.perStateAPI.StatsPerStateHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatsFragment extends Fragment {
    private static final String TAG = "StatsFragment";

    private ArrayList<String> provinsi = new ArrayList<>();
    private ArrayList<String> positif = new ArrayList<>();
    private ArrayList<String> sembuh = new ArrayList<>();
    private ArrayList<String> meninggal = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        TextView mStatKasusPositif = view.findViewById(R.id.stat_kasus_aktif);
        TextView mStatKasusMeninggal = view.findViewById(R.id.stat_kasus_meninggal);
        TextView mStatKasusSembuh = view.findViewById(R.id.stat_kasus_sumbuh);
        TextView mStatKasusODP = view.findViewById(R.id.stat_kasus_odp);
        TextView mStatKasusPDP = view.findViewById(R.id.stat_kasus_pdp);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.stats_shimmer);
        TableLayout tableLayout = view.findViewById(R.id.stats_box_layout);
        RecyclerView recyclerView = view.findViewById(R.id.stats_recyclerView);
        ShimmerFrameLayout mPerStateShimmer = view.findViewById(R.id.stats_shimmer_prov);

        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        tableLayout.setVisibility(View.GONE);

        String baseURL = "https://services5.arcgis.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StatsHolder statsHolder = retrofit.create(StatsHolder.class);

        Call<Stats> statsCall = statsHolder.getStats();

        statsCall.enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                shimmerFrameLayout.setVisibility(View.GONE);
                tableLayout.setVisibility(View.VISIBLE);

                List<Features> features = response.body().getFeatures();
                Log.d(TAG, "onResponse: list size " + features.size());
                Log.d(TAG, "onResponse: the latest data " + features.get(features.size() - 1).getAttributes().getHari_ke());
                int findTheFilledOne = 1;
                int jumlahKumulatif = features.get(features.size() - 1).getAttributes().getJumlah_Kasus_Kumulatif();
                while(jumlahKumulatif == 0) {
                    findTheFilledOne++;
                    jumlahKumulatif = features.get(features.size() - findTheFilledOne).getAttributes().getJumlah_Kasus_Kumulatif();
                }
                int jumlahMeninggal = features.get(features.size() - findTheFilledOne).getAttributes().getJumlah_Pasien_Meninggal();
                int jumlahSembuh = features.get(features.size() - findTheFilledOne).getAttributes().getJumlah_Pasien_Sembuh();
                int jumlahODP = features.get(features.size() - findTheFilledOne).getAttributes().getODP();
                int jumlahPDP = features.get(features.size() - findTheFilledOne).getAttributes().getPDP();
                Log.d(TAG, "onResponse: jumlah kumulatif " + jumlahKumulatif);
                mStatKasusPositif.setText(numberSeparator(jumlahKumulatif));
                mStatKasusSembuh.setText(numberSeparator(jumlahSembuh));
                mStatKasusMeninggal.setText(numberSeparator(jumlahMeninggal));
                mStatKasusODP.setText(numberSeparator(jumlahODP));
                mStatKasusPDP.setText(numberSeparator(jumlahPDP));
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                Log.e(TAG, "onFailure: "  + t.getMessage());
            }
        });


        Retrofit retrofitPerState = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StatsPerStateHolder statsPerStateHolder = retrofitPerState.create(StatsPerStateHolder.class);

        Call<StatsPerState> statsPerStateCall = statsPerStateHolder.getStatsPerState();

        statsPerStateCall.enqueue(new Callback<StatsPerState>() {
            @Override
            public void onResponse(Call<StatsPerState> call, Response<StatsPerState> response) {

                recyclerView.setVisibility(View.VISIBLE);
                mPerStateShimmer.setVisibility(View.GONE);

                List<FeaturesPerState> featuresPerStates = response.body().getFeaturesPerStates();

                for(FeaturesPerState theFeature : featuresPerStates){
                    provinsi.add(theFeature.getAttributesDataPerState().getProvinsi());
                    positif.add(theFeature.getAttributesDataPerState().getPositif());
                    sembuh.add(theFeature.getAttributesDataPerState().getSembuh());
                    meninggal.add(theFeature.getAttributesDataPerState().getMeninggal());
                }

                StatsAdapter statsAdapter = new StatsAdapter(provinsi, positif, sembuh, meninggal, view.getContext());

                recyclerView.setAdapter(statsAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);

            }

            @Override
            public void onFailure(Call<StatsPerState> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


        return view;
    }

    private String numberSeparator(int jumlahKumulatif) {
        return String.valueOf(NumberFormat.getNumberInstance(Locale.ITALY).format(jumlahKumulatif));
    }
}
