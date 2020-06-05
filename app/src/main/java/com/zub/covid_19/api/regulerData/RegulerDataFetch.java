package com.zub.covid_19.api.regulerData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegulerDataFetch {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://data.covid19.go.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
