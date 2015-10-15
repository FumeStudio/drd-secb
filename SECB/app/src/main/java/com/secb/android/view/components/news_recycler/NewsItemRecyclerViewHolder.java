package com.secb.android.view.components.news_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

//view holder===================================================
public class NewsItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_newTitle;
    TextView txtv_newDescription;
    TextView txtv_newTime;
    ImageView imgv_newImg;

    public NewsItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        initViews(itemView);
        applyFonts();

    }

    private void initViews(View itemView) {
        txtv_newTitle = (TextView) itemView.findViewById(R.id.txtv_newTitle);
        txtv_newDescription = (TextView) itemView.findViewById(R.id.txtv_newDescription);
        txtv_newTime = (TextView) itemView.findViewById(R.id.txtv_newTime);
        imgv_newImg = (ImageView)itemView.findViewById(R.id.imgv_newImg);
    }


    private void applyFonts()
    {
        if(txtv_newTitle!=null)
        {
            UiEngine.applyCustomFont(txtv_newTitle, UiEngine.Fonts.HVAR);
        }
        if(txtv_newDescription!=null)
        {
            UiEngine.applyCustomFont(txtv_newDescription, UiEngine.Fonts.HVAR);
        }
        if(txtv_newTime!=null)
        {
            UiEngine.applyCustomFont(txtv_newTime, UiEngine.Fonts.HVAR);
        }
    }
}