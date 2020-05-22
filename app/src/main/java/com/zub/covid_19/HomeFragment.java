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

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private ArrayList<String> mNewsTitle = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: preparing the home");

//        ShimmerFrameLayout shimmerFrameLayout =
//                container.findViewById(R.id.shimmer_layout);
//        shimmerFrameLayout.startShimmer(); // If auto-start is set to false

        mNewsTitle.add("Berita 1");
        mNewsTitle.add("Berita 2");
        mNewsTitle.add("Berita 3");

        Log.d(TAG, "initRecycleView: preparing recycle view");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.news_recycleview);
        NewsAdapter newsAdapter = new NewsAdapter(mNewsTitle, view.getContext());
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL,false));

        return view;
    }

}
