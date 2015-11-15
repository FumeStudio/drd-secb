package com.secb.android.view.components.recycler_events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.model.EventItem;
import com.secb.android.view.MainActivity;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class EventItemRecyclerAdapter extends RecyclerView.Adapter<EventItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<EventItem>itemsList = Collections.emptyList();
    Context context;

    public EventItemRecyclerAdapter(Context context, List<EventItem> itemsList) {
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
    public EventItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

	    if(inflater==null && context!=null)
		    this.inflater = LayoutInflater.from(context);

	    View view = inflater.inflate(R.layout.event_item_card, parent, false);

        EventItemRecyclerViewHolder vh = new EventItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventItemRecyclerViewHolder holder, int position)
    {
        EventItem currentItem = itemsList.get(position);
	    if(!Utilities.isNullString(currentItem.ImageUrl))
	    {
		    Glide.with(context)
				    .load(currentItem.ImageUrl)
				    .placeholder(R.drawable.events_image_place_holder)
					.centerCrop()
				    .into(holder.imgv_eventImg);
	    }
	    else
		    holder.imgv_eventImg.setImageResource(R.drawable.events_image_place_holder);

		String evdateStr =null;
		evdateStr= MainActivity.reFormatDate(currentItem.EventDate,MainActivity.sdf_day_mon);
		if(!Utilities.isNullString(evdateStr)){
			String[] dayMonthArr = evdateStr.split("-");
			holder.txtv_eventImgDate_day.setText(dayMonthArr[0]);
			holder.txtv_eventImgDate_month.setText(dayMonthArr[1].toUpperCase());
		}
	    holder.txtv_eventTitle.setText(currentItem.Title);
        holder.txtv_eventDescription.setText(currentItem.Description);

	    evdateStr = MainActivity.reFormatDate(currentItem.EventDate, MainActivity.sdf_Time);

        holder.txtv_event_timeValue.setText(evdateStr);
        holder.txtv_event_placeValue.setText(currentItem.EventSiteCity);
        holder.txtv_event_categoryValue.setText(currentItem.EventCategory);
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
