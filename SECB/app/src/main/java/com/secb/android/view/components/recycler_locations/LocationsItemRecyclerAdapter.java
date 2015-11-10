package com.secb.android.view.components.recycler_locations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.model.LocationItem;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class LocationsItemRecyclerAdapter extends RecyclerView.Adapter<LocationsItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<LocationItem>itemsList = Collections.emptyList();
    Context context;

    public LocationsItemRecyclerAdapter(Context context, List<LocationItem> itemsList) {
	    this.context=context;
	    if(context == null)
		    this.context= CTApplication.getContext();

	    this.itemsList = itemsList;
	    try {
		    this.inflater = LayoutInflater.from(context);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }

    }

    @Override
    public LocationsItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

	    if(inflater == null && context!=null)
		    this.inflater = LayoutInflater.from(context);
	    View view = inflater.inflate(R.layout.location_item_card, parent, false);

        LocationsItemRecyclerViewHolder vh = new LocationsItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(LocationsItemRecyclerViewHolder holder, int position) {
        LocationItem currentItem = itemsList.get(position);
//        holder.imgv_locationImg.setImageBitmap(currentItem.LoccationItemImage);

	    if(!Utilities.isNullString(currentItem.SiteImage))
	    {
		    Glide.with(context)
				    .load(currentItem.SiteImage)
				    .placeholder(R.drawable.location_image_place_holder)
                    .centerCrop()
				    .into(holder.imgv_locationImg)
		    ;
	    }
	    else
		    holder.imgv_locationImg.setImageResource(R.drawable.location_image_place_holder);

        holder.txtv_locationTitle.setText(currentItem.SiteName);
        holder.txtv_locationDescription.setText(currentItem.SiteDescription);
        holder.txtv_location_capacityValue.setText(currentItem.SiteCapacity +"");
        holder.txtv_location_spaceValue.setText(currentItem.SiteArea +"");

        holder.txtv_location_capacityValue.setTextColor(context.getResources().getColor(R.color.secb_dark_text));
        holder.txtv_location_spaceValue.setTextColor(context.getResources().getColor(R.color.secb_dark_text));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


}
