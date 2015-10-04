package com.secb.android.view.menu.items_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;

//view holder===================================================
public class MenuItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView itemText;
    ImageView itemIcon;

    public MenuItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        itemText = (TextView) itemView.findViewById(R.id.text_itemText);
        itemIcon = (ImageView)itemView.findViewById(R.id.image_itemIcon);
    }
}