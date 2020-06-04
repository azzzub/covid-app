package com.zub.covid_19;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zub.covid_19.api.regulerData.RegulerData;
import com.zub.covid_19.api.regulerData.RegulerDataFetch;
import com.zub.covid_19.api.regulerData.RegulerDataHolder;
import com.zub.covid_19.stats.Features;
import com.zub.covid_19.stats.Stats;
import com.zub.covid_19.stats.StatsHolder;
import com.zub.covid_19.stats.perStateAPI.FeaturesPerState;
import com.zub.covid_19.stats.perStateAPI.StatsPerState;
import com.zub.covid_19.stats.perStateAPI.StatsPerStateHolder;
import com.zub.covid_19.vm.RegulerDataViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lib.kingja.switchbutton.SwitchMultiButton;
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
        TextView mKeterangan = view.findViewById(R.id.stats_keterangan);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.stats_shimmer);
        TableLayout tableLayout = view.findViewById(R.id.stats_box_layout);
        RecyclerView recyclerView = view.findViewById(R.id.stats_recyclerView);
        ShimmerFrameLayout mPerStateShimmer = view.findViewById(R.id.stats_shimmer_prov);

        LineChart lineChart = view.findViewById(R.id.linechart);

        ArrayList statsData = new ArrayList();

        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        tableLayout.setVisibility(View.GONE);

//        String baseURL = "https://services5.arcgis.com/";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        StatsHolder statsHolder = retrofit.create(StatsHolder.class);
//
//        Call<Stats> statsCall = statsHolder.getStats();
//
//        statsCall.enqueue(new Callback<Stats>() {
//            @Override
//            public void onResponse(Call<Stats> call, Response<Stats> response) {
//                shimmerFrameLayout.setVisibility(View.GONE);
//                tableLayout.setVisibility(View.VISIBLE);
//
//                List<Features> features = response.body().getFeatures();
//
//                for(Features theFeatures : features){
//                    if(theFeatures.getAttributes().getJumlah_Kasus_Kumulatif() == 0){
//                        break;
//                    }
//                    int dataX = theFeatures.getAttributes().getHari_ke();
//                    int dataY = theFeatures.getAttributes().getJumlah_Kasus_Kumulatif();
//                    statsData.add(new Entry(dataX, dataY));
//                }
//
//                Log.d(TAG, "onResponse: list size " + features.size());
//                Log.d(TAG, "onResponse: the latest data " + features.get(features.size() - 1).getAttributes().getHari_ke());
//                int findTheFilledOne = 1;
//                int jumlahKumulatif = features.get(features.size() - 1).getAttributes().getJumlah_Kasus_Kumulatif();
//                while(jumlahKumulatif == 0) {
//                    findTheFilledOne++;
//                    jumlahKumulatif = features.get(features.size() - findTheFilledOne).getAttributes().getJumlah_Kasus_Kumulatif();
//                }
//                int jumlahMeninggal = features.get(features.size() - findTheFilledOne).getAttributes().getJumlah_Pasien_Meninggal();
//                int jumlahSembuh = features.get(features.size() - findTheFilledOne).getAttributes().getJumlah_Pasien_Sembuh();
//                int jumlahODP = features.get(features.size() - findTheFilledOne).getAttributes().getODP();
//                int jumlahPDP = features.get(features.size() - findTheFilledOne).getAttributes().getPDP();
//                long tanggal = features.get(features.size() - findTheFilledOne).getAttributes().getTanggal();
//                Log.d(TAG, "onResponse: jumlah kumulatif " + jumlahKumulatif);
//                mStatKasusPositif.setText(numberSeparator(jumlahKumulatif));
//                mStatKasusSembuh.setText(numberSeparator(jumlahSembuh));
//                mStatKasusMeninggal.setText(numberSeparator(jumlahMeninggal));
//                mStatKasusODP.setText(numberSeparator(jumlahODP));
//                mStatKasusPDP.setText(numberSeparator(jumlahPDP));
//                mKeterangan.setText("Diperbarui pada: " + getDate(tanggal));
//
//                //CHART LOGIC
//                LineDataSet lineDataSet = new LineDataSet(statsData,"Data");
//                lineDataSet.setDrawCircles(false);
//                lineDataSet.setDrawFilled(true);
//                lineDataSet.setColor(ColorTemplate.rgb("#ff5959"));
//                lineDataSet.setFillColor(ColorTemplate.rgb("#ff5959"));
//                lineDataSet.setDrawValues(false);
//                LineData data = new LineData(lineDataSet);
//                lineChart.animateY(1000);
//                lineChart.setData(data);
//                lineChart.getXAxis().setEnabled(false);
//                lineChart.getAxisRight().setEnabled(false);
//                Description desc = new Description();
//                desc.setText("");
//                lineChart.setDescription(desc);
//                lineChart.getLegend().setEnabled(false);
//                lineChart.setClickable(false);
//                lineChart.setDoubleTapToZoomEnabled(false);
//                lineChart.setScaleEnabled(false);
//
//            }
//
//            @Override
//            public void onFailure(Call<Stats> call, Throwable t) {
//                Log.e(TAG, "onFailure: "  + t.getMessage());
//            }
//        });
//
//
//        Retrofit retrofitPerState = new Retrofit.Builder()
//                .baseUrl(baseURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        StatsPerStateHolder statsPerStateHolder = retrofitPerState.create(StatsPerStateHolder.class);
//
//        Call<StatsPerState> statsPerStateCall = statsPerStateHolder.getStatsPerState();
//
//        statsPerStateCall.enqueue(new Callback<StatsPerState>() {
//            @Override
//            public void onResponse(Call<StatsPerState> call, Response<StatsPerState> response) {
//
//                recyclerView.setVisibility(View.VISIBLE);
//                mPerStateShimmer.setVisibility(View.GONE);
//
//                List<FeaturesPerState> featuresPerStates = response.body().getFeaturesPerStates();
//
//                for(FeaturesPerState theFeature : featuresPerStates){
//                    provinsi.add(theFeature.getAttributesDataPerState().getProvinsi());
//                    positif.add(theFeature.getAttributesDataPerState().getPositif());
//                    sembuh.add(theFeature.getAttributesDataPerState().getSembuh());
//                    meninggal.add(theFeature.getAttributesDataPerState().getMeninggal());
//                }
//
//                StatsAdapter statsAdapter = new StatsAdapter(provinsi, positif, sembuh, meninggal, view.getContext());
//
//                recyclerView.setAdapter(statsAdapter);
//
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false);
//                recyclerView.setLayoutManager(linearLayoutManager);
//
//            }
//
//            @Override
//            public void onFailure(Call<StatsPerState> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.getMessage());
//            }
//        });


//        regulerDataFetch();


        RegulerDataViewModel regulerDataViewModel;

        regulerDataViewModel = ViewModelProviders.of(this).get(RegulerDataViewModel.class);
        regulerDataViewModel.init();
        regulerDataViewModel.getNewsRepository().observe(this, RegulerData -> {
            Log.d(TAG, "onCreateView: " + RegulerData.getUpdatedData().getTotalCases().getmPositif());
        });


        return view;
    }

    private void regulerDataFetch() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://data.covid19.go.id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegulerDataHolder regulerDataHolder = retrofit.create(RegulerDataHolder.class);

        Call<RegulerData> call = regulerDataHolder.getRegulerData();

        call.enqueue(new Callback<RegulerData>() {
            @Override
            public void onResponse(Call<RegulerData> call, Response<RegulerData> response) {



//                Log.d(TAG, "onResponse: ODP "+ response.body().getDerivativeData().getmODP());
//                Log.d(TAG, "onResponse: PDP "+ response.body().getDerivativeData().getmPDP());
//                Log.d(TAG, "onResponse: Total Spesimen "+ response.body().getDerivativeData().getmTotalSpesimen());
//                Log.d(TAG, "onResponse: Total Spesimen Negatif "+ response.body().getDerivativeData().getmTotalSpesimenNegatif());
//                Log.d(TAG, "onResponse: Tambahan Positif "+ response.body().getUpdatedData().getNewCases().getmPositif());
//                Log.d(TAG, "onResponse: Tambahan Meninggal "+ response.body().getUpdatedData().getNewCases().getmMeninggal());
//                Log.d(TAG, "onResponse: Tambahan Sembuh "+ response.body().getUpdatedData().getNewCases().getmSembuh());
//                Log.d(TAG, "onResponse: Tambahan Dirawat "+ response.body().getUpdatedData().getNewCases().getmDirawat());
//                Log.d(TAG, "onResponse: Waktu Update "+ response.body().getUpdatedData().getNewCases().getmWaktuUpdate());
//                Log.d(TAG, "onResponse: Total Positif "+ response.body().getUpdatedData().getTotalCases().getmPositif());
//                Log.d(TAG, "onResponse: Total Meninggal "+ response.body().getUpdatedData().getTotalCases().getmMeninggal());
//                Log.d(TAG, "onResponse: Total Sembuh "+ response.body().getUpdatedData().getTotalCases().getmSembuh());
//                Log.d(TAG, "onResponse: Total Dirawat "+ response.body().getUpdatedData().getTotalCases().getmDirawat());
//                List<RegulerData.UpdatedData.DailyData> dailyDataList = response.body().getUpdatedData().getDailyData();
//
//                for(RegulerData.UpdatedData.DailyData theDailyData : dailyDataList){
//                    Log.d(TAG, "onResponse: Daily Data Date" + theDailyData.getmEpochDate());
//                    Log.d(TAG, "onResponse: Daily Meninggal" + theDailyData.getmMeninggal());
//                    Log.d(TAG, "onResponse: Daily Sembuh" + theDailyData.getmSembuh());
//                    Log.d(TAG, "onResponse: Daily Positif" + theDailyData.getmPositif());
//                    Log.d(TAG, "onResponse: Daily Dirawat" + theDailyData.getmDirawat());
//                    Log.d(TAG, "onResponse: Daily Meninggal Kum" + theDailyData.getmMeninggalKum());
//                    Log.d(TAG, "onResponse: Daily Sembuh Kum" + theDailyData.getmSembuhKum());
//                    Log.d(TAG, "onResponse: Daily Positif Kum" + theDailyData.getmPositifKum());
//                    Log.d(TAG, "onResponse: Daily Dirawat Kum" + theDailyData.getmDirawatKum());
//                }

            }

            @Override
            public void onFailure(Call<RegulerData> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private String numberSeparator(int jumlahKumulatif) {
        return String.valueOf(NumberFormat.getNumberInstance(Locale.ITALY).format(jumlahKumulatif));
    }

    private String getDate(long time) {
        Date date = new Date(time);
        return date.toString();
    }

}
