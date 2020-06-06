package com.zub.covid_19;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zub.covid_19.api.regulerData.RegulerData;
import com.zub.covid_19.api.regulerData.RegulerDataFetch;
import com.zub.covid_19.api.regulerData.RegulerDataHolder;
import com.zub.covid_19.api.specData.SpecData;
import com.zub.covid_19.repo.SpecDataRepository;
import com.zub.covid_19.stats.Features;
import com.zub.covid_19.stats.Stats;
import com.zub.covid_19.stats.StatsHolder;
import com.zub.covid_19.stats.perStateAPI.FeaturesPerState;
import com.zub.covid_19.stats.perStateAPI.StatsPerState;
import com.zub.covid_19.stats.perStateAPI.StatsPerStateHolder;
import com.zub.covid_19.vm.RegulerDataViewModel;
import com.zub.covid_19.vm.SpecDataViewModel;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class StatsFragment extends Fragment {
    private static final String TAG = "StatsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        // ========= REGULER DATA WIDGET

        TextView mStatKasusPositif = view.findViewById(R.id.stat_kasus_aktif);
        TextView mStatKasusMeninggal = view.findViewById(R.id.stat_kasus_meninggal);
        TextView mStatKasusSembuh = view.findViewById(R.id.stat_kasus_sumbuh);
        TextView mStatKasusODP = view.findViewById(R.id.stat_kasus_odp);
        TextView mStatKasusPDP = view.findViewById(R.id.stat_kasus_pdp);
        TextView mStatAddedPos = view.findViewById(R.id.stat_added_pos);
        TextView mStatAddedMen = view.findViewById(R.id.stat_added_men);
        TextView mStatAddedSem = view.findViewById(R.id.stat_added_sem);
        TextView mUpdatedDate = view.findViewById(R.id.stat_updated_date);

        ShimmerFrameLayout mBoxShimmer = view.findViewById(R.id.stat_box_shimmer);
        ShimmerFrameLayout mCumulativeGraphShimmer = view.findViewById(R.id.stat_shimmer_cumulative_case_graph);
        ShimmerFrameLayout mNewCaseGraphShimmer = view.findViewById(R.id.stat_shimmer_new_case_graph);

        TableLayout mBoxLayout = view.findViewById(R.id.stat_box_layout);

        LineChart mCumulativeCaseGraph = view.findViewById(R.id.stat_cumulative_case_graph);
        LineChart mNewCaseGraph = view.findViewById(R.id.stat_new_case_graph);

        // ========= REGULER DATA FETCHING

        RegulerDataViewModel regulerDataViewModel;

        regulerDataViewModel = ViewModelProviders.of(this).get(RegulerDataViewModel.class);
        regulerDataViewModel.init();

        regulerDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showLoading(mBoxShimmer, mCumulativeGraphShimmer, mNewCaseGraphShimmer,
                            mCumulativeCaseGraph, mNewCaseGraph, mBoxLayout);
                } else {
                    hideLoading(mBoxShimmer, mCumulativeGraphShimmer, mNewCaseGraphShimmer,
                            mCumulativeCaseGraph, mNewCaseGraph, mBoxLayout);
                }
            }
        });

        regulerDataViewModel.getRegulerData().observe(this, new Observer<RegulerData>() {
            @Override
            public void onChanged(RegulerData regulerData) {
                showRegulerData(mStatKasusPositif, mStatKasusMeninggal, mStatKasusSembuh, mStatKasusODP,
                        mStatKasusPDP, mStatAddedPos, mStatAddedMen, mStatAddedSem, mUpdatedDate, regulerData);
                showCumulativeCaseGraph(mCumulativeCaseGraph, regulerData);
                showNewCaseGraph(mNewCaseGraph, regulerData);
            }

        });

        // ========= SPECIFIC DATA WIDGET

        ShimmerFrameLayout mConditionGraphShimmer = view.findViewById(R.id.stat_shimmer_codition_graph);
        ShimmerFrameLayout mAgeGraphShimmer = view.findViewById(R.id.stat_shimmer_age_graph);

        BarChart mConditionGraph = view.findViewById(R.id.stat_condition_graph);
        BarChart mAgeGraph = view.findViewById(R.id.stat_age_graph);

        // ========= SPECIFIC DATA FETCHING

        SpecDataViewModel specDataViewModel;

        specDataViewModel = ViewModelProviders.of(this).get(SpecDataViewModel.class);
        specDataViewModel.init();

        specDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showLoading1(mConditionGraphShimmer, mAgeGraphShimmer, mConditionGraph, mAgeGraph);
                } else {
                    hideLoading1(mConditionGraphShimmer, mAgeGraphShimmer, mConditionGraph, mAgeGraph);
                }
            }
        });

        specDataViewModel.getSpecData().observe(this, new Observer<SpecData>() {
            @Override
            public void onChanged(SpecData specData) {
                showConditionGraph(mConditionGraph, specData);
                showAgeGraph(mAgeGraph, specData);
            }
        });

        return view;
    }

    private void showAgeGraph(BarChart mAgeGraph, SpecData specData) {

        List<SpecData.DetailedData.DerivativeDetailedData.DetailedSpecList> kelompokUmurPos =
                specData.getmKasus().getmKelompokUmur().getmDetailedSpecLists();
        List<SpecData.DetailedData.DerivativeDetailedData.DetailedSpecList> kelompokUmurMen =
                specData.getmMeninggal().getmKelompokUmur().getmDetailedSpecLists();
        List<SpecData.DetailedData.DerivativeDetailedData.DetailedSpecList> kelompokUmurSem =
                specData.getmSembuh().getmKelompokUmur().getmDetailedSpecLists();
        List<SpecData.DetailedData.DerivativeDetailedData.DetailedSpecList> kelompokUmurPer =
                specData.getmPerawatan().getmKelompokUmur().getmDetailedSpecLists();

        List<IBarDataSet> kelompokUmurDS = new ArrayList<>();

        ArrayList<BarEntry> key = new ArrayList<>();
        ArrayList<BarEntry> key2 = new ArrayList<>();
        ArrayList<BarEntry> key3 = new ArrayList<>();
        ArrayList<BarEntry> key4 = new ArrayList<>();

        for (int i = 0; i < kelompokUmurPos.size(); i++) {
            key.add(new BarEntry(i, (float) kelompokUmurPos.get(i).getValue()));
            key2.add(new BarEntry(i, (float) kelompokUmurMen.get(i).getValue()));
            key3.add(new BarEntry(i, (float) kelompokUmurSem.get(i).getValue()));
            key4.add(new BarEntry(i, (float) kelompokUmurPer.get(i).getValue()));
        }

        BarDataSet barDataSet = new BarDataSet(key, "Positif");
        barDataSet.setColor(COLOR_SCHEME[0]);
        barDataSet.setValueTextSize(8);
        kelompokUmurDS.add(barDataSet);

        BarDataSet barDataSet2 = new BarDataSet(key2, "Meninggal");
        barDataSet2.setColor(COLOR_SCHEME[1]);
        barDataSet2.setValueTextSize(8);
        kelompokUmurDS.add(barDataSet2);

        BarDataSet barDataSet3 = new BarDataSet(key3, "Sembuh");
        barDataSet3.setColor(COLOR_SCHEME[2]);
        barDataSet3.setValueTextSize(8);
        kelompokUmurDS.add(barDataSet3);

        BarDataSet barDataSet4 = new BarDataSet(key4, "Perawatan");
        barDataSet4.setValueTextSize(8);
        barDataSet4.setColor(COLOR_SCHEME[3]);
        kelompokUmurDS.add(barDataSet4);

        BarData kelompokUmurBD = new BarData(barDataSet, barDataSet2, barDataSet3, barDataSet4);
        mAgeGraph.setData(kelompokUmurBD);
        mAgeGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int formated = Math.round(value);
                // rejecting the unused data that case error on getting the list
                if (formated > 5 || formated < 0) {
                    formated = 0;
                }
                return specData.getmKasus().getmKelompokUmur().getmDetailedSpecLists().get(formated).getKey();
            }
        });

        mAgeGraph.getXAxis().setCenterAxisLabels(true);

        final float BAR_WIDTH = 0.15f;
        final float GROUP_SPACE = 0.10f;
        final float BAR_SPACE = 0.05f;
        final float GROUP_COUNT = 6;

        mAgeGraph.getBarData().setBarWidth(BAR_WIDTH);

        // restrict the x-axis range
        mAgeGraph.getXAxis().setAxisMinimum(0);
        mAgeGraph.getLegend().setTextSize(12);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mAgeGraph.getXAxis().setAxisMaximum(0 + mAgeGraph.getBarData().getGroupWidth(GROUP_SPACE, BAR_SPACE) * GROUP_COUNT);
        mAgeGraph.groupBars(0, GROUP_SPACE, BAR_SPACE);
        mAgeGraph.getAxisRight().setEnabled(false);
        mAgeGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        Description description = new Description();
        description.setText("");
        mAgeGraph.setDescription(description);
        mAgeGraph.invalidate();

    }

    private void showConditionGraph(BarChart mConditionGraph, SpecData specData) {

        List<SpecData.DetailedData.DerivativeDetailedData.DetailedSpecList> detailedSpecLists =
                specData.getmKasus().getmKondisiPenyerta().getmDetailedSpecLists();

        List<IBarDataSet> iBarDataSets = new ArrayList<>();

        for (int i = 0; i < detailedSpecLists.size(); i++) {
            ArrayList<BarEntry> key = new ArrayList<>();
            key.add(new BarEntry(i, (float) detailedSpecLists.get(i).getValue()));
            // Manipulating first letter to be capitalized
            String theDataSet = detailedSpecLists.get(i).getKey();
            BarDataSet barDataSet = new BarDataSet(key, theDataSet.substring(0, 1).toUpperCase() + theDataSet.substring(1).toLowerCase());
            barDataSet.setColor(COLOR_SCHEME[i]);
            iBarDataSets.add(barDataSet);
        }
        BarData barData = new BarData(iBarDataSets);
        barData.setValueFormatter(new ValueFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f", value) + "%";
            }
        });

        barData.setValueTextSize(10);
        mConditionGraph.setData(barData);
        mConditionGraph.setMinimumHeight(180);
        mConditionGraph.getXAxis().setDrawLabels(false);
        mConditionGraph.getXAxis().setDrawAxisLine(false);
        mConditionGraph.getXAxis().setDrawGridLines(false);
        mConditionGraph.getAxisRight().setEnabled(false);
        mConditionGraph.getLegend().setWordWrapEnabled(true);
        mConditionGraph.getLegend().setTextSize(10);
        Description description = new Description();
        description.setText("");
        mConditionGraph.setDescription(description);
        mConditionGraph.setDoubleTapToZoomEnabled(false);
        mConditionGraph.setPinchZoom(false);
        mConditionGraph.invalidate();
    }

    private void hideLoading1(ShimmerFrameLayout mConditionGraphShimmer, ShimmerFrameLayout mAgeGraphShimmer,
                              BarChart mConditionGraph, BarChart mAgeGraph) {

        mConditionGraphShimmer.setVisibility(View.GONE);
        mAgeGraphShimmer.setVisibility(View.GONE);
        mConditionGraph.setVisibility(View.VISIBLE);
        mAgeGraph.setVisibility(View.VISIBLE);

    }

    private void showLoading1(ShimmerFrameLayout mConditionGraphShimmer, ShimmerFrameLayout mAgeGraphShimmer,
                              BarChart mConditionGraph, BarChart mAgeGraph) {

        mConditionGraphShimmer.setVisibility(View.VISIBLE);
        mAgeGraphShimmer.setVisibility(View.VISIBLE);
        mConditionGraph.setVisibility(View.GONE);
        mAgeGraph.setVisibility(View.GONE);

    }

    private void showNewCaseGraph(LineChart mNewCaseGraph, RegulerData regulerData) {

        ArrayList lineDataPositif = new ArrayList();
        ArrayList lineDataMeninggal = new ArrayList();
        ArrayList lineDataSembuh = new ArrayList();
        ArrayList lineDataDirawat = new ArrayList();

        List<RegulerData.UpdatedData.DailyData> dailyDataList = regulerData.getUpdatedData().getDailyData();

        for (RegulerData.UpdatedData.DailyData theDailyData : dailyDataList) {

            int dataPositif = theDailyData.getmPositif();
            int dataMeninggal = theDailyData.getmMeninggal();
            int dataSembuh = theDailyData.getmSembuh();
            int dataDirawat = theDailyData.getmDirawat();
            long dataTanggal = theDailyData.getmEpochDate();
            lineDataPositif.add(new Entry(dataTanggal, dataPositif));
            lineDataMeninggal.add(new Entry(dataTanggal, dataMeninggal));
            lineDataSembuh.add(new Entry(dataTanggal, dataSembuh));
            lineDataDirawat.add(new Entry(dataTanggal, dataDirawat));
        }

        LineDataSet lineDataSetPositif = new LineDataSet(lineDataPositif,"Positif");
        LineDataSet lineDataSetMeninggal = new LineDataSet(lineDataMeninggal,"Meninggal");
        LineDataSet lineDataSetSembuh = new LineDataSet(lineDataSembuh,"Sembuh");
        LineDataSet lineDataSetDirawat = new LineDataSet(lineDataDirawat,"Dirawat");

        setupLineChart(lineDataSetPositif, "#ffb259");
        setupLineChart(lineDataSetMeninggal, "ff5959");
        setupLineChart(lineDataSetSembuh, "4cd97b");
        setupLineChart(lineDataSetDirawat, "9059ff");

        mNewCaseGraph.animateY(1000);
        mNewCaseGraph.getAxisRight().setEnabled(false);
//        mNewCaseGraph.getLegend().setTextSize(12);
        mNewCaseGraph.setClickable(false);
        mNewCaseGraph.setDoubleTapToZoomEnabled(false);
        mNewCaseGraph.setScaleEnabled(false);
        mNewCaseGraph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mNewCaseGraph.getXAxis().setValueFormatter(new ValueFormatter() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public String getFormattedValue(float value) {
                long millisecond = (long) value;
                String dateString = DateFormat.format("dd MMM", new Date(millisecond)).toString();
                return dateString;
            }
        });

        Description desc = new Description();
        desc.setText("");
        mNewCaseGraph.setDescription(desc);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSetPositif);
        dataSets.add(lineDataSetMeninggal);
        dataSets.add(lineDataSetSembuh);
        dataSets.add(lineDataSetDirawat);

        LineData data = new LineData(dataSets);
        mNewCaseGraph.setData(data);
        mNewCaseGraph.invalidate();

    }

    private void showCumulativeCaseGraph(LineChart mLineChart, RegulerData regulerData) {

        ArrayList lineDataPositif = new ArrayList();
        ArrayList lineDataMeninggal = new ArrayList();
        ArrayList lineDataSembuh = new ArrayList();
        ArrayList lineDataDirawat = new ArrayList();

        List<RegulerData.UpdatedData.DailyData> dailyDataList = regulerData.getUpdatedData().getDailyData();

        for (RegulerData.UpdatedData.DailyData theDailyData : dailyDataList) {

            int dataPositif = theDailyData.getmPositifKum();
            int dataMeninggal = theDailyData.getmMeninggalKum();
            int dataSembuh = theDailyData.getmSembuhKum();
            int dataDirawat = theDailyData.getmDirawatKum();
            long dataTanggal = theDailyData.getmEpochDate();
            lineDataPositif.add(new Entry(dataTanggal, dataPositif));
            lineDataMeninggal.add(new Entry(dataTanggal, dataMeninggal));
            lineDataSembuh.add(new Entry(dataTanggal, dataSembuh));
            lineDataDirawat.add(new Entry(dataTanggal, dataDirawat));
        }

        LineDataSet lineDataSetPositif = new LineDataSet(lineDataPositif,"Positif");
        LineDataSet lineDataSetMeninggal = new LineDataSet(lineDataMeninggal,"Meninggal");
        LineDataSet lineDataSetSembuh = new LineDataSet(lineDataSembuh,"Sembuh");
        LineDataSet lineDataSetDirawat = new LineDataSet(lineDataDirawat,"Dirawat");

        setupLineChart(lineDataSetPositif, "#ffb259");
        setupLineChart(lineDataSetMeninggal, "ff5959");
        setupLineChart(lineDataSetSembuh, "4cd97b");
        setupLineChart(lineDataSetDirawat, "9059ff");

        mLineChart.animateY(1000);
        mLineChart.getAxisRight().setEnabled(false);
//        mLineChart.getLegend().setTextSize(12);
        mLineChart.setClickable(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public String getFormattedValue(float value) {
                long millisecond = (long) value;
                String dateString = DateFormat.format("dd MMM", new Date(millisecond)).toString();
                return dateString;
            }
        });

        Description desc = new Description();
        desc.setText("");
        mLineChart.setDescription(desc);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSetPositif);
        dataSets.add(lineDataSetMeninggal);
        dataSets.add(lineDataSetSembuh);
        dataSets.add(lineDataSetDirawat);

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate();

    }

    private void setupLineChart(LineDataSet lineDataSet, String s) {
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setColor(rgb(s));
        lineDataSet.setFillColor(rgb(s));
        lineDataSet.setDrawValues(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
    }

    private void showRegulerData(TextView mStatKasusPositif, TextView mStatKasusMeninggal,
                                 TextView mStatKasusSembuh, TextView mStatKasusODP,
                                 TextView mStatKasusPDP, TextView mStatAddedPos,
                                 TextView mStatAddedMen, TextView mStatAddedSem,
                                 TextView mUpdatedDate, RegulerData regulerData) {

        int mPositif = regulerData.getUpdatedData().getTotalCases().getmPositif();
        int mMeninggal = regulerData.getUpdatedData().getTotalCases().getmMeninggal();
        int mSembuh = regulerData.getUpdatedData().getTotalCases().getmSembuh();
        int mODP = regulerData.getDerivativeData().getmODP();
        int mPDP = regulerData.getDerivativeData().getmPDP();
        int mAddedPos = regulerData.getUpdatedData().getNewCases().getmPositif();
        int mAddedMen = regulerData.getUpdatedData().getNewCases().getmMeninggal();
        int mAddedSem = regulerData.getUpdatedData().getNewCases().getmSembuh();
        String mUpdate = regulerData.getUpdatedData().getNewCases().getmWaktuUpdate();

        mStatKasusPositif.setText(numberSeparator(mPositif));
        mStatKasusMeninggal.setText(numberSeparator(mMeninggal));
        mStatKasusSembuh.setText(numberSeparator(mSembuh));
        mStatKasusODP.setText(numberSeparator(mODP));
        mStatKasusPDP.setText(numberSeparator(mPDP));
        mStatAddedPos.setText("+" + numberSeparator(mAddedPos));
        mStatAddedMen.setText("+" + numberSeparator(mAddedMen));
        mStatAddedSem.setText("+" + numberSeparator(mAddedSem));
        mUpdatedDate.setText("Diperbarui pada: " + mUpdate);

    }

    private void hideLoading(ShimmerFrameLayout mBoxShimmer, ShimmerFrameLayout mCummulativeGraphShimmer,
                             ShimmerFrameLayout mNewCaseGraphSimmer, LineChart mCumulativeCaseGraph,
                             LineChart mNewCaseGraph, TableLayout mBoxLayout) {
        mBoxShimmer.setVisibility(View.GONE);
        mCummulativeGraphShimmer.setVisibility(View.GONE);
        mNewCaseGraphSimmer.setVisibility(View.GONE);
        mBoxLayout.setVisibility(View.VISIBLE);
        mCumulativeCaseGraph.setVisibility(View.VISIBLE);
        mNewCaseGraph.setVisibility(View.VISIBLE);
    }

    private void showLoading(ShimmerFrameLayout mBoxShimmer, ShimmerFrameLayout mCummulativeGraphShimmer,
                             ShimmerFrameLayout mNewCaseGraphSimmer, LineChart mCumulativeCaseGraph,
                             LineChart mNewCaseGraph, TableLayout mBoxLayout) {
        mBoxShimmer.setVisibility(View.VISIBLE);
        mCummulativeGraphShimmer.setVisibility(View.VISIBLE);
        mNewCaseGraphSimmer.setVisibility(View.VISIBLE);
        mBoxLayout.setVisibility(View.GONE);
        mCumulativeCaseGraph.setVisibility(View.GONE);
        mNewCaseGraph.setVisibility(View.GONE);
    }

    private String numberSeparator(int jumlahKumulatif) {
        return String.valueOf(NumberFormat.getNumberInstance(Locale.ITALY).format(jumlahKumulatif));
    }

    private static final int[] COLOR_SCHEME = {
            rgb("#ffb259"), rgb("#ff5959"), rgb("4cd97b"), rgb("4cb5ff"), rgb("9059ff"),
            rgb("#ff3434"), rgb("#ffeeee"), rgb("4c9a9a"), rgb("4c5b5b"), rgb("90ff75"),
            rgb("#900407")
    };

}
