package com.zub.covid_19.api.specData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpecData {

    @SerializedName("last_update")
    private String mUpdatedDate;

    @SerializedName("kasus")
    private DetailedData mKasus;

    public String getmUpdatedDate() {
        return mUpdatedDate;
    }

    public DetailedData getmKasus() {
        return mKasus;
    }

    public static class DetailedData {

        @SerializedName("kondisi_penyerta")
        private DerivativeDetailedData mKondisiPenyerta;

        public DerivativeDetailedData getmKondisiPenyerta() {
            return mKondisiPenyerta;
        }

        public static class DerivativeDetailedData {

            @SerializedName("current_data")
            private int mTotalData;

            @SerializedName("missing_data")
            private double mMissingData;

            @SerializedName("list_data")
            private List<DetailedSpecList> mDetailedSpecLists;

            public int getmTotalData() {
                return mTotalData;
            }

            public double getmMissingData() {
                return mMissingData;
            }

            public List<DetailedSpecList> getmDetailedSpecLists() {
                return mDetailedSpecLists;
            }

            public static class DetailedSpecList {

                @SerializedName("key")
                private String key;

                @SerializedName("doc_count")
                private double value;

                public String getKey() {
                    return key;
                }

                public double getValue() {
                    return value;
                }
            }

        }

    }

}
