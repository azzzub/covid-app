package com.zub.covid_19;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
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

public class StatsFragment extends Fragment {
    private static final String TAG = "StatsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

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

        return view;
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
        mNewCaseGraph.getLegend().setTextSize(14);
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
        mLineChart.getLegend().setTextSize(14);
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
        lineDataSet.setColor(ColorTemplate.rgb(s));
        lineDataSet.setFillColor(ColorTemplate.rgb(s));
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

}
