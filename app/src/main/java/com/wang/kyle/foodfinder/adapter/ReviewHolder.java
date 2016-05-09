package com.wang.kyle.foodfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wang.kyle.foodfinder.R;

/**
 * Created by Kyle on 5/8/2016.
 */

public class ReviewHolder extends RecyclerView.ViewHolder  {
    private TextView mName;
    private TextView mText;

    public TextView getName() {
        return mName;
    }

    public TextView getText() {
        return mText;
    }

    public ReviewHolder(View itemView) {
        super(itemView);
        mName = (TextView)itemView.findViewById(R.id.review_name);
        mText = (TextView)itemView.findViewById(R.id.review_content);

    }


}
