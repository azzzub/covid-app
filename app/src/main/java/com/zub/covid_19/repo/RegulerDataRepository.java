package com.zub.covid_19.repo;

import androidx.lifecycle.MutableLiveData;

import com.zub.covid_19.api.regulerData.RegulerData;
import com.zub.covid_19.api.regulerData.RegulerDataFetch;
import com.zub.covid_19.api.regulerData.RegulerDataHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RegulerDataRepository {

    private static RegulerDataRepository regulerDataRepository;

    public static RegulerDataRepository getInstance() {
        if(regulerDataRepository == null) {
            regulerDataRepository = new RegulerDataRepository();
        }
        return regulerDataRepository;
    }

    private RegulerDataHolder regulerDataHolder;

    private RegulerDataRepository() {
        regulerDataHolder = RegulerDataFetch.createService(RegulerDataHolder.class);
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoading() {
        return isLoading;
    }

    public MutableLiveData<RegulerData> getRegulerData() {
        MutableLiveData<RegulerData> regulerData = new MutableLiveData<>();
        isLoading.setValue(true);
        regulerDataHolder.getRegulerData().enqueue(new Callback<RegulerData>() {
            @Override
            public void onResponse(Call<RegulerData> call, Response<RegulerData> response) {
                if (response.isSuccessful()){
                    isLoading.setValue(false);
                    regulerData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RegulerData> call, Throwable t) {
                isLoading.setValue(false);
                Timber.e(t);
            }
        });

        return regulerData;

    }

}
