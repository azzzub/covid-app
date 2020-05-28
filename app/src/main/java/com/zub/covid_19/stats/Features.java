package com.zub.covid_19.stats;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Features {
    @SerializedName("attributes")
    private AttributesData attributes;

    public AttributesData getAttributes() {
        return attributes;
    }
}
