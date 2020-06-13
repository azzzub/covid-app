package com.zub.covid_19.api.newsData;

import android.content.res.Resources;

import com.bumptech.glide.load.engine.Resource;
import com.zub.covid_19.R;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NewsDataHolder {

    @GET("v2/top-headlines?country=id&q=covid")
    Call<NewsData> getNews(@Query("apiKey") String apiKey);

}
