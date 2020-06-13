package com.zub.covid_19.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.zub.covid_19.api.provData.ProvData;
import com.zub.covid_19.api.provData.ProvDataFetch;
import com.zub.covid_19.api.provData.ProvDataHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ProvDataRepository {

    private static ProvDataRepository provDataRepository;

    public static ProvDataRepository getInstance() {
        if(provDataRepository == null) {
            provDataRepository = new ProvDataRepository();
        }
        return provDataRepository;
    }

    private ProvDataHolder provDataHolder;

    private ProvDataRepository() {
        provDataHolder = ProvDataFetch.createService(ProvDataHolder.class);
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getLoading() {
        return isLoading;
    }

    public MutableLiveData<ProvData> getProvData() {
        MutableLiveData<ProvData> ProvData = new MutableLiveData<>();
        isLoading.setValue(true);
        provDataHolder.getProvData().enqueue(new Callback<ProvData>() {
            @Override
            public void onResponse(Call<ProvData> call, Response<ProvData> response) {
                if (response.isSuccessful()){
                    isLoading.setValue(false);
                    ProvData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ProvData> call, Throwable t) {
                isLoading.setValue(false);
                Timber.e(t);
            }
        });

        return ProvData;

    }

}