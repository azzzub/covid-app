package com.zub.covid_19.api.specData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpecDataHolder {

    @GET("public/api/data.json")
    Call<SpecData> getSpecData();

}
