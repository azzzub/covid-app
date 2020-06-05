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
        ShimmerFrameLayout mGraphShimmer = view.findViewById(R.id.stat_shimmer_graph);
        ShimmerFrameLayout mPerStateShimmer = view.findViewById(R.id.stat_shimmer_prov);

        TableLayout mBoxLayout = view.findViewById(R.id.stat_box_layout);

        RecyclerView mRvPerProvinsi = view.findViewById(R.id.stat_rv_per_provinsi);

        LineChart mLineChart = view.findViewById(R.id.stat_linechart);

        RegulerDataViewModel regulerDataViewModel;

        regulerDataViewModel = ViewModelProviders.of(this).get(RegulerDataViewModel.class);
        regulerDataViewModel.init();

        regulerDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showLoading(mBoxShimmer, mGraphShimmer, mLineChart, mBoxLayout);
                } else {
                    hideLoading(mBoxShimmer, mGraphShimmer, mLineChart, mBoxLayout);
                }
            }
        });

        regulerDataViewModel.getRegulerData().observe(this, new Observer<RegulerData>() {
            @Override
            public void onChanged(RegulerData regulerData) {
                showRegulerData(mStatKasusPositif, mStatKasusMeninggal, mStatKasusSembuh, mStatKasusODP,
                        mStatKasusPDP, mStatAddedPos, mStatAddedMen, mStatAddedSem, mUpdatedDate, regulerData);
                showGraphData(mLineChart, regulerData);
            }
        });

        return view;
    }

    private void showGraphData(LineChart mLineChart, RegulerData regulerData) {

        ArrayList lineDataPositif = new ArrayList();
        ArrayList lineDataMeninggal = new ArrayList();
        ArrayList lineDataSembuh = new ArrayList();

        List<RegulerData.UpdatedData.DailyData> dailyDataList = regulerData.getUpdatedData().getDailyData();

        for (RegulerData.UpdatedData.DailyData theDailyData : dailyDataList) {

            int dataPositif = theDailyData.getmPositifKum();
            int dataMeninggal = theDailyData.getmMeninggalKum();
            int dataSembuh = theDailyData.getmSembuhKum();
            long dataTanggal = theDailyData.getmEpochDate();
            lineDataPositif.add(new Entry(dataTanggal, dataPositif));
            lineDataMeninggal.add(new Entry(dataTanggal, dataMeninggal));
            lineDataSembuh.add(new Entry(dataTanggal, dataSembuh));
        }

        LineDataSet lineDataSetPositif = new LineDataSet(lineDataPositif,"Positif");
        lineDataSetPositif.setDrawCircles(false);
        lineDataSetPositif.setDrawFilled(true);
        lineDataSetPositif.setColor(ColorTemplate.rgb("#ffb259"));
        lineDataSetPositif.setFillColor(ColorTemplate.rgb("#ffb259"));
        lineDataSetPositif.setDrawValues(false);
        lineDataSetPositif.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineDataSet lineDataSetMeninggal = new LineDataSet(lineDataMeninggal,"Meninggal");
        lineDataSetMeninggal.setDrawCircles(false);
        lineDataSetMeninggal.setDrawFilled(true);
        lineDataSetMeninggal.setColor(ColorTemplate.rgb("#ff5959"));
        lineDataSetMeninggal.setFillColor(ColorTemplate.rgb("#ff5959"));
        lineDataSetMeninggal.setDrawValues(false);
        lineDataSetMeninggal.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineDataSet lineDataSetSembuh = new LineDataSet(lineDataSembuh,"Sembuh");
        lineDataSetSembuh.setDrawCircles(false);
        lineDataSetSembuh.setDrawFilled(true);
        lineDataSetSembuh.setColor(ColorTemplate.rgb("#4cd97b"));
        lineDataSetSembuh.setFillColor(ColorTemplate.rgb("#4cd97b"));
        lineDataSetSembuh.setDrawValues(false);
        lineDataSetSembuh.setAxisDependency(YAxis.AxisDependency.LEFT);

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

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate();

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

    private void hideLoading(ShimmerFrameLayout mBoxShimmer, ShimmerFrameLayout mGraphShimmer,
                             LineChart mLineChart, TableLayout mBoxLayout) {
        mBoxShimmer.setVisibility(View.GONE);
        mGraphShimmer.setVisibility(View.GONE);
        mBoxLayout.setVisibility(View.VISIBLE);
        mLineChart.setVisibility(View.VISIBLE);
    }

    private void showLoading(ShimmerFrameLayout mBoxShimmer, ShimmerFrameLayout mGraphShimmer,
                             LineChart mLineChart, TableLayout mBoxLayout) {
        mBoxShimmer.setVisibility(View.VISIBLE);
        mGraphShimmer.setVisibility(View.VISIBLE);
        mBoxLayout.setVisibility(View.GONE);
        mLineChart.setVisibility(View.GONE);
    }

    private String numberSeparator(int jumlahKumulatif) {
        return String.valueOf(NumberFormat.getNumberInstance(Locale.ITALY).format(jumlahKumulatif));
    }

}
