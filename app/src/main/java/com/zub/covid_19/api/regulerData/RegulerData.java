package com.zub.covid_19.api.regulerData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegulerData {

    @SerializedName("data")
    private DerivativeData derivativeData;

    @SerializedName("update")
    private UpdatedData updatedData;

    public DerivativeData getDerivativeData() {
        return derivativeData;
    }

    public UpdatedData getUpdatedData() {
        return updatedData;
    }

    public static class DerivativeData {

        @SerializedName("jumlah_odp")
        private int mODP;

        @SerializedName("jumlah_pdp")
        private int mPDP;

        @SerializedName("total_spesimen")
        private int mTotalSpesimen;

        @SerializedName("total_spesimen_negatif")
        private int mTotalSpesimenNegatif;

        public int getmODP() {
            return mODP;
        }

        public int getmPDP() {
            return mPDP;
        }

        public int getmTotalSpesimen() {
            return mTotalSpesimen;
        }

        public int getmTotalSpesimenNegatif() {
            return mTotalSpesimenNegatif;
        }

    }

    public static class UpdatedData {

        @SerializedName("penambahan")
        private NewCases newCases;

        @SerializedName("total")
        private TotalCases totalCases;

        @SerializedName("harian")
        private List<DailyData> dailyData;

        public NewCases getNewCases() {
            return newCases;
        }

        public TotalCases getTotalCases() {
            return totalCases;
        }

        public List<DailyData> getDailyData() {
            return dailyData;
        }

        public static class NewCases {

            @SerializedName("jumlah_positif")
            private int mPositif;

            @SerializedName("jumlah_meninggal")
            private int mMeninggal;

            @SerializedName("jumlah_sembuh")
            private int mSembuh;

            @SerializedName("jumlah_dirawat")
            private int mDirawat;

            @SerializedName("created")
            private String mWaktuUpdate;

            public int getmPositif() {
                return mPositif;
            }

            public int getmMeninggal() {
                return mMeninggal;
            }

            public int getmSembuh() {
                return mSembuh;
            }

            public int getmDirawat() {
                return mDirawat;
            }

            public String getmWaktuUpdate() {
                return mWaktuUpdate;
            }

        }

        public static class TotalCases {

            @SerializedName("jumlah_positif")
            private int mPositif;

            @SerializedName("jumlah_dirawat")
            private int mDirawat;

            @SerializedName("jumlah_sembuh")
            private int mSembuh;

            @SerializedName("jumlah_meninggal")
            private int mMeninggal;

            public int getmPositif() {
                return mPositif;
            }

            public int getmDirawat() {
                return mDirawat;
            }

            public int getmSembuh() {
                return mSembuh;
            }

            public int getmMeninggal() {
                return mMeninggal;
            }
        }

        public static class DailyData {

            @SerializedName("key")
            private long mEpochDate;

            @SerializedName("jumlah_meninggal")
            private TheValue mMeninggal;

            @SerializedName("jumlah_sembuh")
            private TheValue mSembuh;

            @SerializedName("jumlah_positif")
            private TheValue mPositif;

            @SerializedName("jumlah_dirawat")
            private TheValue mDirawat;

            @SerializedName("jumlah_meninggal_kum")
            private TheValue mMeninggalKum;

            @SerializedName("jumlah_sembuh_kum")
            private TheValue mSembuhKum;

            @SerializedName("jumlah_positif_kum")
            private TheValue mPositifKum;

            @SerializedName("jumlah_dirawat_kum")
            private TheValue mDirawatKum;

            public int getmMeninggal() {
                return mMeninggal.getmValue();
            }

            public int getmSembuh() {
                return mSembuh.getmValue();
            }

            public int getmPositif() {
                return mPositif.getmValue();
            }

            public int getmDirawat() {
                return mDirawat.getmValue();
            }

            public int getmMeninggalKum() {
                return mMeninggalKum.getmValue();
            }

            public int getmSembuhKum() {
                return mSembuhKum.getmValue();
            }

            public int getmPositifKum() {
                return mPositifKum.getmValue();
            }

            public int getmDirawatKum() {
                return mDirawatKum.getmValue();
            }

            public long getmEpochDate() {
                return mEpochDate;
            }

            public static class TheValue {

                @SerializedName("value")
                private int mValue;

                public int getmValue() {
                    return mValue;
                }
            }
        }
    }

}
