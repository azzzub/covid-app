package com.zub.covid_19.stats.perStateAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatsPerState {
    @SerializedName("features")
    private List<FeaturesPerState> featuresPerStates;

    public List<FeaturesPerState> getFeaturesPerStates() {
        return featuresPerStates;
    }
}
