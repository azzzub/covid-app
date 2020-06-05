package com.zub.covid_19.api.regulerData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RegulerDataHolder {

    @GET("public/api/update.json")
    Call<RegulerData> getRegulerData();

}
