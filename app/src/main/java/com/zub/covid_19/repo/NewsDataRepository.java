package com.zub.covid_19.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.zub.covid_19.api.newsData.NewsData;
import com.zub.covid_19.api.newsData.NewsDataFetch;
import com.zub.covid_19.api.newsData.NewsDataHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDataRepository {

    private static final String TAG = "NewsDataRepository";
    
    private static NewsDataRepository newsDataRepository;

    public static NewsDataRepository getInstance() {
        if(newsDataRepository == null) {
            newsDataRepository = new NewsDataRepository();
        }
        return newsDataRepository;
    }

    private NewsDataHolder newsDataHolder;

    private NewsDataRepository() {
        newsDataHolder = NewsDataFetch.createService(NewsDataHolder.class);
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoading() {
        return isLoading;
    }

    public MutableLiveData<NewsData> getProvData() {
        MutableLiveData<NewsData> NewsData = new MutableLiveData<>();
        isLoading.setValue(true);
        newsDataHolder.getNews().enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                isLoading.setValue(false);
                NewsData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                isLoading.setValue(false);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return NewsData;

    }
    
}
