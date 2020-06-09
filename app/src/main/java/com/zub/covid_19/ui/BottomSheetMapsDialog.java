package com.zub.covid_19.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zub.covid_19.R;
import com.zub.covid_19.api.provData.ProvData;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class BottomSheetMapsDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_maps_layout, container, false);

        TextView textView = view.findViewById(R.id.test_bottom);

        textView.setText("HOLA");

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                textView.setText(provData.getLastUpdate());
            }
        }, 1000);

        return view;
    }
}
