package com.zub.covid_19.repo;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.zub.covid_19.api.newsData.NewsData;
import com.zub.covid_19.api.newsData.NewsDataFetch;
import com.zub.covid_19.api.newsData.NewsDataHolder;
import com.zub.covid_19.util.LoadLocale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class NewsDataRepository {

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

    public MutableLiveData<NewsData> getNewsData() {
        MutableLiveData<NewsData> NewsData = new MutableLiveData<>();
        isLoading.setValue(true);
        newsDataHolder.getNews("699fa5b4ab4d49aab02a36bc88eb6cde")
                .enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                isLoading.setValue(false);
                NewsData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                isLoading.setValue(false);
                Timber.e(t);
            }
        });

        return NewsData;

    }

    public MutableLiveData<NewsData> getNewsDataEn() {
        MutableLiveData<NewsData> NewsData = new MutableLiveData<>();
        isLoading.setValue(true);
        newsDataHolder.getNewsEn("699fa5b4ab4d49aab02a36bc88eb6cde")
                .enqueue(new Callback<NewsData>() {
                    @Override
                    public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                        isLoading.setValue(false);
                        NewsData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<NewsData> call, Throwable t) {
                        isLoading.setValue(false);
                        Timber.e(t);
                    }
                });

        return NewsData;

    }
    
}
