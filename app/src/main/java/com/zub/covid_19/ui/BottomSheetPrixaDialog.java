package com.zub.covid_19.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zub.covid_19.R;
import com.zub.covid_19.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class BottomSheetPrixaDialog extends BottomSheetMapsDialog {

    @BindView(R.id.bottom_prixa_time_countdown)
    TextView mCountdown;

    CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_prixa_dialog, container, false);
        ButterKnife.bind(this, view);

        this.countDownTimer = new CountDownTimer(5000, 1000) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                mCountdown.setText("(" + l / 1000 + ")");
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("passingURL", "https://covid19.prixa.ai");
                intent.putExtra("passingTitle", "Prixa.ai");
                getActivity().startActivity(intent);
                dismiss();
            }
        }.start();

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        this.countDownTimer.cancel();
        super.onDismiss(dialog);
    }
}
