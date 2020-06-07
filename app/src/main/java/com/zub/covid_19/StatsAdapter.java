package com.zub.covid_19;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private static final String TAG = "StatsAdapter";

    private ArrayList<String> provinsi = new ArrayList<>();
    private ArrayList<String> positif = new ArrayList<>();
    private ArrayList<String> sembuh = new ArrayList<>();
    private ArrayList<String> meninggal = new ArrayList<>();
    private Context context;

    public StatsAdapter(ArrayList<String> provinsi, ArrayList<String> positif, ArrayList<String> sembuh, ArrayList<String> meninggal, Context context) {
        this.provinsi = provinsi;
        this.positif = positif;
        this.sembuh = sembuh;
        this.meninggal = meninggal;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.mProvinsi.setText(provinsi.get(position));
        holder.mPositif.setText(positif.get(position));
        holder.mSembuh.setText(sembuh.get(position));
        holder.mMeninggal.setText(meninggal.get(position));
    }

    @Override
    public int getItemCount() {
        return provinsi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView mProvinsi, mPositif, mSembuh, mMeninggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.stats_card_view);
            mProvinsi = itemView.findViewById(R.id.stats_provinsi);
            mPositif = itemView.findViewById(R.id.stats_positif);
            mSembuh = itemView.findViewById(R.id.stats_sembuh);
            mMeninggal = itemView.findViewById(R.id.stats_meninggal);
        }
    }
}
