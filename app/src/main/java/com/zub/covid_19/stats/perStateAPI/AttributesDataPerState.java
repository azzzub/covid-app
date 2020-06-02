package com.zub.covid_19.stats.perStateAPI;

import com.google.gson.annotations.SerializedName;

public class AttributesDataPerState {

    @SerializedName("Provinsi")
    private String provinsi;
    @SerializedName("Kasus_Posi")
    private String positif;
    @SerializedName("Kasus_Semb")
    private String sembuh;
    @SerializedName("Kasus_Meni")
    private String meninggal;

    public String getProvinsi() {
        return provinsi;
    }

    public String getPositif() {
        return positif;
    }

    public String getSembuh() {
        return sembuh;
    }

    public String getMeninggal() {
        return meninggal;
    }
}
