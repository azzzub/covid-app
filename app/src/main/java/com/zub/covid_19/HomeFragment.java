package com.zub.covid_19;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private ArrayList<String> mNewsTitle = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: preparing the home");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        RecyclerView recyclerView = view.findViewById(R.id.news_recycleview);
        ImageView mNewsImage = view.findViewById(R.id.news_image);

        recyclerView.setVisibility(View.GONE);

        getDummy(view, shimmerFrameLayout, recyclerView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsHolder newsHolder = retrofit.create(NewsHolder.class);

        Call<News> call = newsHolder.getNews();

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body());
                    return;
                }

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                List<Articles> articles = response.body().getArticles();
                for(Articles theArticels : articles) {
                    mNewsTitle.add(theArticels.getTitle());
//                    Picasso.get().load(theArticels.getUrlToImage()).into(mNewsImage);
                    Log.d(TAG, "initRecycleView: preparing recycle view");

                    RecyclerView recyclerView = view.findViewById(R.id.news_recycleview);
                    NewsAdapter newsAdapter = new NewsAdapter(mNewsTitle, view.getContext());
                    recyclerView.setAdapter(newsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL,false));

                    Log.d(TAG, "onResponse: "+ theArticels.getTitle());
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

//        call.enqueue(new Callback<List<News>>() {
//            @Override
//            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
//                Log.d(TAG, "respon body: " + response.body());
//                if(!response.isSuccessful()) {
//                    Log.e(TAG, "onResponse: code = " + response.code());
//                    Log.d(TAG, "onResponse: " + response.body());
//                    return;
//                }
//
//                shimmerFrameLayout.stopShimmer();
//                shimmerFrameLayout.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//
//                List<News> news = response.body();
//
//                for(News theNews : news) {
//                    mNewsTitle.add(theNews.getStatus());
//
//                    Log.d(TAG, "initRecycleView: preparing recycle view");
//
//                    RecyclerView recyclerView = view.findViewById(R.id.news_recycleview);
//                    NewsAdapter newsAdapter = new NewsAdapter(mNewsTitle, view.getContext());
//                    recyclerView.setAdapter(newsAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL,false));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<News>> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.getMessage());
//            }
//        });

        return view;
    }

    private void getDummy(View view, ShimmerFrameLayout shimmerFrameLayout, RecyclerView recyclerView) {

    }

}
