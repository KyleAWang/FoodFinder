package com.wang.kyle.foodfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.kyle.foodfinder.R;
import com.wang.kyle.foodfinder.module.Review;

import java.util.List;

/**
 * Created by Kyle on 5/8/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder>{
    private List<Review> mReviewList;

    public ReviewAdapter(List<Review> reviews) {
        mReviewList = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_review, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review testEntity = mReviewList.get(position);
        holder.getName().setText(testEntity.getAuthor_name());
        holder.getText().setText(testEntity.getText());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }
}