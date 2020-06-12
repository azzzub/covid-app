package com.zub.covid_19.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zub.covid_19.WebViewActivity;
import com.zub.covid_19.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String TAG = "NewsAdapter";

    private ArrayList<String> mNewsImage = new ArrayList<>();
    private ArrayList<String> mNewsTitle = new ArrayList<>();
    private ArrayList<String> mNewsURL = new ArrayList<>();

    private Context context;

    public NewsAdapter(ArrayList<String> mNewsImage, ArrayList<String> mNewsTitle, ArrayList<String> mNewsURL, Context context) {
        this.mNewsImage = mNewsImage;
        this.mNewsTitle = mNewsTitle;
        this.mNewsURL = mNewsURL;
        this.context = context;
    }


    public NewsAdapter(ArrayList<String> mNewsImage, ArrayList<String> mNewsTitle, Context context) {
        this.mNewsImage = mNewsImage;
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
        holder.textView.setText(mNewsTitle.get(position));

        Glide.with(context).
                load(mNewsImage.get(position)).
                centerCrop().
                placeholder(R.drawable.news_placeholder).
                into(holder.newsImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                intent.putExtra("passingURL", mNewsURL.get(position));
                intent.putExtra("passingTitle", mNewsTitle.get(position));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        ImageView newsImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.news_card_view);
            textView = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
        }
    }
}
