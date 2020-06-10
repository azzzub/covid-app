package com.zub.covid_19.api.provData;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProvData implements Parcelable {

    @SerializedName("last_date")
    private String lastUpdate;

    @SerializedName("current_data")
    private double currentData;

    @SerializedName("missing_data")
    private double missingData;

    @SerializedName("tanpa_provinsi")
    private int unidentifiedProv;

    @SerializedName("list_data")
    private List<ProvListData> provListDataLists;

    public static class ProvListData {

        @SerializedName("key")
        private String provName;

        @SerializedName("doc_count")
        private double docCount;

        @SerializedName("jumlah_kasus")
        private int caseAmount;

        @SerializedName("jumlah_sembuh")
        private int healedAmount;

        @SerializedName("jumlah_meninggal")
        private int deathAmount;

        @SerializedName("jumlah_dirawat")
        private int treatedAmount;

        @SerializedName("jenis_kelamin")
        private List<ProvDataSexList> provDataSexLists;

        @SerializedName("kelompok_umur")
        private List<ProvDataAgeList> provDataAgeLists;

        @SerializedName("lokasi")
        @Nullable
        private ProvDataLocation provDataLocation;

        public static class ProvDataSexList {

            @SerializedName("key")
            private String sex;

            @SerializedName("doc_count")
            private int docCount;

            public String getSex() {
                return sex;
            }

            public int getDocCount() {
                return docCount;
            }
        }

        public static class ProvDataAgeList {

            @SerializedName("key")
            private String age;

            @SerializedName("doc_count")
            private int docCount;

            public String getAge() {
                return age;
            }

            public int getDocCount() {
                return docCount;
            }
        }

        public static class ProvDataLocation {
            @SerializedName("lon")
            private double lng;

            @SerializedName("lat")
            private double lat;

            public double getLng() {
                return lng;
            }

            public double getLat() {
                return lat;
            }
        }

        public String getProvName() {
            return provName;
        }

        public double getDocCount() {
            return docCount;
        }

        public int getCaseAmount() {
            return caseAmount;
        }

        public int getHealedAmount() {
            return healedAmount;
        }

        public int getDeathAmount() {
            return deathAmount;
        }

        public int getTreatedAmount() {
            return treatedAmount;
        }

        public List<ProvDataSexList> getProvDataSexLists() {
            return provDataSexLists;
        }

        public List<ProvDataAgeList> getProvDataAgeLists() {
            return provDataAgeLists;
        }

        public ProvDataLocation getProvDataLocation() {
            return provDataLocation;
        }
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public double getCurrentData() {
        return currentData;
    }

    public double getMissingData() {
        return missingData;
    }

    public int getUnidentifiedProv() {
        return unidentifiedProv;
    }

    public List<ProvListData> getProvListDataLists() {
        return provListDataLists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastUpdate);
        dest.writeDouble(this.currentData);
        dest.writeDouble(this.missingData);
        dest.writeInt(this.unidentifiedProv);
        dest.writeList(this.provListDataLists);
    }

    public ProvData() {
    }

    protected ProvData(Parcel in) {
        this.lastUpdate = in.readString();
        this.currentData = in.readDouble();
        this.missingData = in.readDouble();
        this.unidentifiedProv = in.readInt();
        this.provListDataLists = new ArrayList<ProvListData>();
        in.readList(this.provListDataLists, ProvListData.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProvData> CREATOR = new Parcelable.Creator<ProvData>() {
        @Override
        public ProvData createFromParcel(Parcel source) {
            return new ProvData(source);
        }

        @Override
        public ProvData[] newArray(int size) {
            return new ProvData[size];
        }
    };
}
