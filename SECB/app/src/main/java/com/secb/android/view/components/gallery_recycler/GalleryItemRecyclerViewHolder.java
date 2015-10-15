package com.secb.android.view.components.gallery_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

//view holder===================================================
public class GalleryItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_galleryTitle;
    ImageView imgv_galleryImg;
    ImageView imgv_galleryPlayVideoIcon;

    public GalleryItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        initViews(itemView);
        applyFonts();
    }
    private void initViews(View itemView) {
        txtv_galleryTitle = (TextView) itemView.findViewById(R.id.txtv_gallary_title);
        imgv_galleryImg = (ImageView)itemView.findViewById(R.id.imgv_gallary_img);
        imgv_galleryPlayVideoIcon = (ImageView)itemView.findViewById(R.id.imgv_play_video_icon);
    }


    private void applyFonts()
    {
        if(txtv_galleryTitle!=null)
        {
            UiEngine.applyCustomFont(txtv_galleryTitle, UiEngine.Fonts.HVAR);
        }
    }

}