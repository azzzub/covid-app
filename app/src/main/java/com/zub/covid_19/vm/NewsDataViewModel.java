package com.zub.covid_19.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zub.covid_19.api.newsData.NewsData;
import com.zub.covid_19.api.provData.ProvData;
import com.zub.covid_19.repo.NewsDataRepository;
import com.zub.covid_19.repo.ProvDataRepository;

public class NewsDataViewModel extends ViewModel {
    private MutableLiveData<NewsData> newsData;
    private MutableLiveData<Boolean> isLoading;
    private NewsDataRepository newsDataRepository;

    public void init() {
        if (newsData != null){
            return;
        }
        newsDataRepository = NewsDataRepository.getInstance();
        isLoading = newsDataRepository.getLoading();

    }

    public LiveData<NewsData> getNewsData() {
        return newsDataRepository.getNewsData();
    }

    public LiveData<NewsData> getNewsDataEn() {
        return newsDataRepository.getNewsDataEn();
    }

    public LiveData<Boolean> getLoading() {
        return isLoading;
    }

}
