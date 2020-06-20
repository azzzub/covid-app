package com.zub.covid_19.api.globalData

import com.google.gson.annotations.SerializedName

class GlobalData {

    @SerializedName("confirmed")
    var confirmed: DetailedGlobalData? = null

    @SerializedName("recovered")
    var recovered: DetailedGlobalData? = null

    @SerializedName("deaths")
    var death: DetailedGlobalData? = null

    @SerializedName("lastUpdate")
    var lastUpdate: String? = null

}

class DetailedGlobalData {

    @SerializedName("value")
    var theValue: Int? = null

}
