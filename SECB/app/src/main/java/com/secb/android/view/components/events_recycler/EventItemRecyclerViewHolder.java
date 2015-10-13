package com.secb.android.view.components.events_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;

//view holder=============================  ======================
public class EventItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_eventTitle;
    TextView txtv_eventDescription;
    TextView txtv_event_timeValue;
    ImageView imgv_eventImg;

    public EventItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        txtv_eventTitle = (TextView) itemView.findViewById(R.id.txtv_eventTitle);
        txtv_eventDescription = (TextView) itemView.findViewById(R.id.txtv_eventDescription);
        txtv_event_timeValue = (TextView) itemView.findViewById(R.id.txtv_event_timeValue);
        imgv_eventImg = (ImageView)itemView.findViewById(R.id.imgv_eventImg);
    }
}