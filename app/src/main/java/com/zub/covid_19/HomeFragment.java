package com.zub.covid_19;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.zub.covid_19.adapter.NewsAdapter;
import com.zub.covid_19.api.newsData.NewsData;
import com.zub.covid_19.ui.BottomSheetPreventionDialog;
import com.zub.covid_19.ui.BottomSheetPrixaDialog;
import com.zub.covid_19.util.LoadLocale;
import com.zub.covid_19.util.SpacesItemDecoration;
import com.zub.covid_19.vm.NewsDataViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zub.covid_19.R.drawable;
import static com.zub.covid_19.R.id;
import static com.zub.covid_19.R.layout;
import static com.zub.covid_19.R.string;

public class HomeFragment extends Fragment {
    private ArrayList<String> mNewsImage = new ArrayList<>();
    private ArrayList<String> mNewsTitle = new ArrayList<>();
    private ArrayList<String> mNewsURL = new ArrayList<>();

    private BottomSheetPreventionDialog bottomSheetPreventionDialog = new BottomSheetPreventionDialog();

    @BindView(id.shimmer_layout)
    ShimmerFrameLayout mNewsShimmer;
    @BindView(id.news_recycler_view)
    RecyclerView mNewsRecyclerView;
    @BindView(id.home_call_button)
    LinearLayout mCallButton;
    @BindView(id.home_website_button)
    LinearLayout mWebsiteButton;
    @BindView(id.home_do_test_button)
    RelativeLayout mDoTest;
    @BindView(id.home_country_button)
    LinearLayout mCountry;
    @BindView(id.home_preventive_1)
    LinearLayout mPreventive1;
    @BindView(id.home_preventive_2)
    LinearLayout mPreventive2;
    @BindView(id.home_preventive_3)
    LinearLayout mPreventive3;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_home, container, false);
        Toast mToast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);
        ButterKnife.bind(this, view);

        LoadLocale loadLocale = new LoadLocale(getActivity());

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:119"));
                startActivity(intent);
            }
        });

        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("passingURL", "https://covid19.go.id");
                intent.putExtra("passingTitle", "Portal Resmi Gugus Tugas Covid-19");
                getContext().startActivity(intent);
            }
        });

        mDoTest.setOnClickListener(new View.OnClickListener() {
            BottomSheetPrixaDialog bottomSheetPrixaDialog = new BottomSheetPrixaDialog();

            @Override
            public void onClick(View view) {
                bottomSheetPrixaDialog.show(getFragmentManager(), "BottomSheet");
            }
        });

        mCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToast.setText("Other countries' data coming soon!");
                mToast.show();
            }
        });

        mPreventive1.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();
            Integer passingTitle = string.home_preventive1;
            Integer passingHeader = drawable.social_distancing_header;
            Integer passingContent = string.social_distancing;
            Integer passingCitation = string.social_distancing_citation;
            @Override
            public void onClick(View view) {
                bundle.putInt("passingTitle", passingTitle);
                bundle.putInt("passingHeader", passingHeader);
                bundle.putInt("passingContent", passingContent);
                bundle.putInt("passingCitation", passingCitation);
                bottomSheetPreventionDialog.setArguments(bundle);
                bottomSheetPreventionDialog.show(getFragmentManager(), "BottomSheet");
            }
        });

        mPreventive2.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();
            Integer passingTitle = string.home_preventive2;
            Integer passingHeader = drawable.wash_hand_header;
            Integer passingContent = string.wash_hand;
            Integer passingCitation = string.wash_hand_citation;
            @Override
            public void onClick(View view) {
                bundle.putInt("passingTitle", passingTitle);
                bundle.putInt("passingHeader", passingHeader);
                bundle.putInt("passingContent", passingContent);
                bundle.putInt("passingCitation", passingCitation);
                bottomSheetPreventionDialog.setArguments(bundle);
                bottomSheetPreventionDialog.show(getFragmentManager(), "BottomSheet");
            }
        });

        mPreventive3.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();
            Integer passingTitle = string.home_preventive3;
            Integer passingHeader = drawable.wear_mask_header;
            Integer passingContent = string.wear_mask;
            Integer passingCitation = string.wear_mask_citation;
            @Override
            public void onClick(View view) {
                bundle.putInt("passingTitle", passingTitle);
                bundle.putInt("passingHeader", passingHeader);
                bundle.putInt("passingContent", passingContent);
                bundle.putInt("passingCitation", passingCitation);
                bottomSheetPreventionDialog.setArguments(bundle);
                bottomSheetPreventionDialog.show(getFragmentManager(), "BottomSheet");
            }
        });
        // THE DATA FETCHING PROCESS
        NewsDataViewModel newsDataViewModel;

        newsDataViewModel = ViewModelProviders.of(this).get(NewsDataViewModel.class);
        newsDataViewModel.init();

        newsDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });

        LiveData<NewsData> newsDataLiveData;

        if (loadLocale.getLocale().equals("en")) {
            newsDataLiveData = newsDataViewModel.getNewsDataEn();
        } else {
            newsDataLiveData = newsDataViewModel.getNewsData();
        }

        newsDataLiveData.observe(this, new Observer<NewsData>() {
            @Override
            public void onChanged(NewsData newsData) {
                List<NewsData.Articles> articles = newsData.getArticles();
                for (NewsData.Articles theArticle : articles) {
                    mNewsTitle.add(theArticle.getTitle());
                    mNewsImage.add(theArticle.getUrltoimage());
                    mNewsURL.add(theArticle.getUrl());
                }

                NewsAdapter newsAdapter = new NewsAdapter(mNewsImage, mNewsTitle, mNewsURL, getContext());
                mNewsRecyclerView.setAdapter(newsAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
                mNewsRecyclerView.setLayoutManager(linearLayoutManager);
                mNewsRecyclerView.addItemDecoration(new SpacesItemDecoration(30));
                SnapHelper snapHelper = new PagerSnapHelper();
                if (mNewsRecyclerView.getOnFlingListener() == null)
                    snapHelper.attachToRecyclerView(mNewsRecyclerView);
            }
        });

        return view;

    }

    private void hideLoading() {
        mNewsShimmer.stopShimmer();
        mNewsShimmer.setVisibility(View.GONE);
        mNewsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mNewsShimmer.setVisibility(View.VISIBLE);
        mNewsRecyclerView.setVisibility(View.GONE);
    }

}