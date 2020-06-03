package com.zub.covid_19;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "SpacesItemDecoration";

    private final int mSpace;
    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int dp = Math.round(mSpace * (view.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        Log.d(TAG, "getItemOffsets: "+dp);
        outRect.right = dp;

        if (parent.getChildAdapterPosition(view) == 0)
            outRect.left = dp;
    }

}
