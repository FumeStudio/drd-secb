package com.secb.android.view.components.events_recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.EventItem;

import java.util.Collections;
import java.util.List;

public class EventItemRecyclerAdapter extends RecyclerView.Adapter<EventItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<EventItem>itemsList = Collections.emptyList();
    Context context;

    public EventItemRecyclerAdapter(Context context, List<EventItem> itemsList) {
        this.inflater = LayoutInflater.from(context);
        this.itemsList = itemsList;
        this.context=context;
    }

    @Override
    public EventItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item_card, parent, false);

        EventItemRecyclerViewHolder vh = new EventItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventItemRecyclerViewHolder holder, int position) {
        EventItem currentItem = itemsList.get(position);
        holder.imgv_eventImg.setImageBitmap(currentItem.eventItemImage);
        holder.txtv_eventTitle.setText(currentItem.eventItemTitle);
        holder.txtv_eventDescription.setText(currentItem.eventItemDescription);
        holder.txtv_event_timeValue.setText(currentItem.eventItemTime);
        holder.txtv_event_placeValue.setText(currentItem.eventItemLocation);
        holder.txtv_event_categoryValue.setText(currentItem.eventItemCategory);
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
