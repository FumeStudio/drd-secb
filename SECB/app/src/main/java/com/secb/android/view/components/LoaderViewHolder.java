package com.secb.android.view.components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.secb.android.R;


public class LoaderViewHolder extends RecyclerView.ViewHolder {

    ProgressBar mProgressBar;

    public LoaderViewHolder(View itemView) {
        super(itemView);
        mProgressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
    }
}