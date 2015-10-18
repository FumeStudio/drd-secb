package com.secb.android.view.components.recycler_locations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.LocationItem;

import java.util.Collections;
import java.util.List;

public class LocationsItemRecyclerAdapter extends RecyclerView.Adapter<LocationsItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<LocationItem>itemsList = Collections.emptyList();
    Context context;

    public LocationsItemRecyclerAdapter(Context context, List<LocationItem> itemsList) {
        this.inflater = LayoutInflater.from(context);
        this.itemsList = itemsList;
        this.context=context;
    }

    @Override
    public LocationsItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.location_item_card, parent, false);

        LocationsItemRecyclerViewHolder vh = new LocationsItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(LocationsItemRecyclerViewHolder holder, int position) {
        LocationItem currentItem = itemsList.get(position);
        holder.imgv_locationImg.setImageBitmap(currentItem.LoccationItemImage);
        holder.txtv_locationTitle.setText(currentItem.LoccationItemTitle);
        holder.txtv_locationDescription.setText(currentItem.LoccationItemDescription);
        holder.txtv_location_capacityValue.setText(currentItem.LoccationItemCapacity+"");
        holder.txtv_location_spaceValue.setText(currentItem.LoccationItemSpace+"");

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
