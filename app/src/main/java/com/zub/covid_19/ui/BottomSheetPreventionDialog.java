package com.zub.covid_19.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zub.covid_19.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomSheetPreventionDialog extends BottomSheetDialogFragment {

    @BindView(R.id.bottom_sheet_prevention_title)
    TextView mTitle;
    @BindView(R.id.bottom_sheet_prevention_header)
    ImageView mHeader;
    @BindView(R.id.bottom_sheet_prevention_content)
    TextView mContent;
    @BindView(R.id.bottom_sheet_prevention_citation)
    TextView mCitation;

    Integer passingTitle, passingHeader, passingContent, passingCitation;

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setHideable(true);
                bottomSheetBehavior.setSkipCollapsed(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_prevention, container, false);
        ButterKnife.bind(this, view);

        passingTitle = this.getArguments().getInt("passingTitle");
        passingHeader = this.getArguments().getInt("passingHeader");
        passingContent = this.getArguments().getInt("passingContent");
        passingCitation = this.getArguments().getInt("passingCitation");

        mTitle.setText(passingTitle);
        mHeader.setImageResource(passingHeader);
        mContent.setText(passingContent);
        mCitation.setText(passingCitation);

        return view;
    }
}
