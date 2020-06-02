package com.zub.covid_19.stats.perStateAPI;

import com.google.gson.annotations.SerializedName;

public class FeaturesPerState {

    @SerializedName("attributes")
    private AttributesDataPerState attributesDataPerState;

    public AttributesDataPerState getAttributesDataPerState() {
        return attributesDataPerState;
    }
}
