package com.zub.covid_19;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

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

//        ShimmerFrameLayout shimmerFrameLayout = container.findViewById(R.id.shimmer_layout);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsHolder newsHolder = retrofit.create(NewsHolder.class);

        Call<List<News>> call = newsHolder.getNews();

        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: code = " + response.code());
                    return;
                }

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                List<News> news = response.body();

                for(News theNews : news) {
                    String content = "";
                    content += "ID: " + theNews.getId() + "\n";
                    content += "User ID: " + theNews.getUserId() + "\n";
                    content += "Title: " + theNews.getTitle() + "\n";
                    content += "Body: " + theNews.getBody() + "\n\n";

                    mNewsTitle.add(theNews.getTitle());

                    Log.d(TAG, "initRecycleView: preparing recycle view");

                    RecyclerView recyclerView = view.findViewById(R.id.news_recycleview);
                    NewsAdapter newsAdapter = new NewsAdapter(mNewsTitle, view.getContext());
                    recyclerView.setAdapter(newsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL,false));

                    Log.d(TAG, "onResponse: " + content);
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

//        mNewsTitle.add("Berita 1");
//        mNewsTitle.add("Berita 2");
//        mNewsTitle.add("Berita 3");



        return view;
    }

}
