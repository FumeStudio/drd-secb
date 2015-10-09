package com.secb.android.view.components.organizers_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;

//view holder===================================================
public class OrganizerItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_organizerTitle;
    TextView txtv_organizerDescription;
    ImageView imgv_organizerImg;

    public OrganizerItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        txtv_organizerTitle = (TextView) itemView.findViewById(R.id.txtv_organizerTitle);
        txtv_organizerDescription = (TextView) itemView.findViewById(R.id.txtv_organizerDescription);
        imgv_organizerImg = (ImageView)itemView.findViewById(R.id.imgv_organizerImg);
    }
}