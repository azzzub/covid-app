package com.zub.covid_19.stats;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StatsHolder {

    @GET("/VS6HdKS0VfIhv8Ct/arcgis/rest/services/Statistik_Perkembangan_COVID19_Indonesia/FeatureServer/0/query?f=json&where=Tanggal%3Ctimestamp%20%272022-04-21%2017%3A00%3A00%27&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=Tanggal%20asc&resultOffset=0&resultRecordCount=32000&resultType=standard&cacheHint=true")
    Call<Stats> getStats();

}
