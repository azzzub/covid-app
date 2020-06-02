package com.zub.covid_19.stats.perStateAPI;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StatsPerStateHolder {

    @GET("VS6HdKS0VfIhv8Ct/arcgis/rest/services/COVID19_Indonesia_per_Provinsi/FeatureServer/0/query?f=json&where=(Kasus_Posi%20<>%200)%20AND%20(Provinsi%20<>%20%27Indonesia%27)&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=Kasus_Posi%20desc&resultOffset=0&resultRecordCount=34&resultType=standard&cacheHint=true")
    Call<StatsPerState> getStatsPerState();

}
