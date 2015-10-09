package com.secb.android.view.components.locations_recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;

//view holder===================================================
public class LocationsItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_locationTitle;
    TextView txtv_locationDescription;
    TextView txtv_location_capacityValue;
    TextView txtv_location_spaceValue;

    ImageView imgv_locationImg;


    public LocationsItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        txtv_locationTitle = (TextView) itemView.findViewById(R.id.txtv_locationTitle);
        txtv_locationDescription = (TextView) itemView.findViewById(R.id.txtv_locationDescription);
        txtv_location_capacityValue = (TextView) itemView.findViewById(R.id.txtv_location_capacityValue);
        txtv_location_spaceValue = (TextView) itemView.findViewById(R.id.txtv_location_spaceValue);
        imgv_locationImg = (ImageView)itemView.findViewById(R.id.imgv_locationImg);
    }
}