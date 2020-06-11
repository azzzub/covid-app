package com.zub.covid_19.api.newsData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsDataHolder {

    @GET("v2/top-headlines?country=id&q=covid&apiKey=699fa5b4ab4d49aab02a36bc88eb6cde")
    Call<NewsData> getNews();

}
