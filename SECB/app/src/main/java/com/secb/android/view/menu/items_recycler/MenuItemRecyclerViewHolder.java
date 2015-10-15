package com.secb.android.view.menu.items_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

//view holder===================================================
public class MenuItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView itemText;
    ImageView itemIcon;

    public MenuItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        initViews(itemView);
        applyFonts();
    }

    private void initViews(View itemView) {
        itemText = (TextView) itemView.findViewById(R.id.text_itemText);
        itemIcon = (ImageView) itemView.findViewById(R.id.image_itemIcon);
    }


    public void applyFonts(){
        if(itemText!=null)
        {
            UiEngine.applyCustomFont(itemText, UiEngine.Fonts.HVAR);
        }
    }
}