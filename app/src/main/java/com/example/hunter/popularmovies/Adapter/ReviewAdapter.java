package com.example.hunter.popularmovies.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hunter.popularmovies.Model.Reviews;
import com.example.hunter.popularmovies.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private Context mcontext;
    private List<Reviews> reviewList;
    private com.example.hunter.popularmovies.Adapter.TrailerAdapter.OnItemClickListener mListner;

    public ReviewAdapter(Context mcontext, List<Reviews> reviewList) {
        this.mcontext =  mcontext;
        this.reviewList = reviewList;

    }

    @NonNull
    @Override

    public ReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review,parent,false);
        return new ReviewAdapter.ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewHolder holder, int position) {
        holder.tv_review_author.setText(reviewList.get(position).getmAuthor());
        holder.tv_reviews.setText(reviewList.get(position).getmContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        public TextView tv_review_author;
        public TextView tv_reviews;


        public ReviewHolder(View itemView) {
            super(itemView);
            tv_review_author = itemView.findViewById(R.id.textView_review_author);
            tv_reviews = itemView.findViewById(R.id.textView_review);
        }
    }
}
