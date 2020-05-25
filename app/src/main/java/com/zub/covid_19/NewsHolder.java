package com.zub.covid_19;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsHolder {

    @GET("posts")
    Call<List<News>> getNews();

}
