package com.zub.covid_19;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String TAG = "NewsAdapter";

    private ArrayList<String> mNewsImage = new ArrayList<>();
    private ArrayList<String> mNewsTitle = new ArrayList<>();
    private Context context;

    public NewsAdapter(ArrayList<String> mNewsTitle, Context context) {
//        this.mNewsImage = mNewsImage;
        this.mNewsTitle = mNewsTitle;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.textView.setText(mNewsTitle.get(position));

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: click on " + mNewsTitle.get(position));

                Toast.makeText(context, mNewsTitle.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.news_layout_rl);
            textView = itemView.findViewById(R.id.news_title);

        }
    }
}
