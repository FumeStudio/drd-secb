package com.secb.android.view.components.event_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;

//view holder===================================================
public class EventItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView itemText;
    ImageView itemIcon;

    public EventItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        itemText = (TextView) itemView.findViewById(R.id.text_itemText);
        itemIcon = (ImageView)itemView.findViewById(R.id.image_itemIcon);
    }
}