package com.zub.covid_19.api.globalData;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalDataFetch {

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://covid19.mathdro.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
