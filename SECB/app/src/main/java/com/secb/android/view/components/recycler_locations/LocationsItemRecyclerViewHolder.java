package com.secb.android.view.components.recycler_locations;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

//view holder===================================================
public class LocationsItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_locationTitle;
    TextView txtv_locationDescription;
    TextView txtv_location_capacityTitle;
    TextView txtv_location_capacityValue;
    TextView txtv_location_spaceTitle;
    TextView txtv_location_spaceValue;

    ImageView imgv_locationImg;
	View layout_item_root;

    public LocationsItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        initViews(itemView);
        applyFonts(itemView);
    }


    private void initViews(View itemView) {
        txtv_locationTitle = (TextView) itemView.findViewById(R.id.txtv_locationTitle);
        txtv_locationDescription = (TextView) itemView.findViewById(R.id.txtv_locationDescription);
        txtv_location_capacityTitle = (TextView) itemView.findViewById(R.id.txtv_location_capacityTitle);
        txtv_location_capacityValue = (TextView) itemView.findViewById(R.id.txtv_location_capacityValue);
        txtv_location_spaceTitle = (TextView) itemView.findViewById(R.id.txtv_location_spaceTitle);
        txtv_location_spaceValue = (TextView) itemView.findViewById(R.id.txtv_location_spaceValue);
        imgv_locationImg = (ImageView)itemView.findViewById(R.id.imgv_locationImg);
	    layout_item_root= itemView.findViewById(R.id.layout_item_root);

    }

    private void applyFonts(View itemView)
    {
        UiEngine.applyCustomFont(itemView, UiEngine.Fonts.HVAR);
/*
        if(txtv_locationTitle!=null)
        {
            UiEngine.applyCustomFont(txtv_locationTitle, UiEngine.Fonts.HVAR);
        }
        if(txtv_locationDescription!=null)
        {
            UiEngine.applyCustomFont(txtv_locationDescription, UiEngine.Fonts.HVAR);
        }
        if(txtv_location_capacityTitle!=null)
        {
            UiEngine.applyCustomFont(txtv_location_capacityTitle, UiEngine.Fonts.HVAR);
        }
        if(txtv_location_capacityValue!=null)
        {
            UiEngine.applyCustomFont(txtv_location_capacityValue, UiEngine.Fonts.HVAR);
        }
        if(txtv_location_spaceTitle!=null)
        {
            UiEngine.applyCustomFont(txtv_location_spaceTitle, UiEngine.Fonts.HVAR);
        }
        if(txtv_location_spaceValue!=null)
        {
            UiEngine.applyCustomFont(txtv_location_spaceValue, UiEngine.Fonts.HVAR);
        }*/
    }
}