package com.zub.covid_19.stats;

public class AttributesData {

    private int Hari_ke;
    private long Tanggal;
    private int Jumlah_Kasus_Kumulatif;
    private int Jumlah_Pasien_Meninggal;
    private int Jumlah_Pasien_Sembuh;
    private int ODP;
    private int PDP;

    public long getTanggal() {
        return Tanggal;
    }

    public int getJumlah_Pasien_Meninggal() {
        return Jumlah_Pasien_Meninggal;
    }

    public int getJumlah_Pasien_Sembuh() {
        return Jumlah_Pasien_Sembuh;
    }

    public int getODP() {
        return ODP;
    }

    public int getPDP() {
        return PDP;
    }

    public int getHari_ke() {
        return Hari_ke;
    }

    public int getJumlah_Kasus_Kumulatif() {
        return Jumlah_Kasus_Kumulatif;
    }
}
