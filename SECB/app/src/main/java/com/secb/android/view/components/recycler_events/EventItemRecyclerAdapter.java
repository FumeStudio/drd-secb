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
import com.secb.android.view.components.FooterLoaderAdapter;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventItemRecyclerAdapter extends FooterLoaderAdapter<EventItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<EventItem>itemsList = Collections.emptyList();
    Context context;
	private ArrayList<Integer> selectedIndices;
	private ArrayList<View>selectedViews;
	private View view;

	public EventItemRecyclerAdapter(Context context, List<EventItem> itemsList) {
		super(context);
	    this.context=context;
	    selectedIndices = new ArrayList<>();
	    selectedViews = new ArrayList<>();

	    if(context == null)
		    this.context= CTApplication.getContext();

		setItemsList(itemsList);
	    try {
		    this.inflater = LayoutInflater.from(context);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
    }

	public void setItemsList(List<EventItem> itemsList) {
		setItems(itemsList);
		this.itemsList = itemsList;
	}

	@Override
	public long getYourItemId(int position) {
		return (itemsList.get(position).ID).hashCode();
	}

    @Override
    public EventItemRecyclerViewHolder getYourItemViewHolder(ViewGroup parent) {

	    if(inflater==null && context!=null)
		    this.inflater = LayoutInflater.from(context);

	    view = inflater.inflate(R.layout.event_item_card, parent, false);

        EventItemRecyclerViewHolder vh = new EventItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder _holder, int position)
    {
		EventItemRecyclerViewHolder holder = (EventItemRecyclerViewHolder) _holder;
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


	   /* if(Utilities.isTablet(context))
	    {
		    int id = (itemsList.get(position).ID).hashCode();
		    if(selectedIndices.contains(id))
			    setItemSelected(view,id);
		    else
			    view.setSelected(false);
	    }*/
//		    holder.layout_item_root.setSelected(selectedIndices.contains(position));
    }

    @Override
    public int getItemCount() {
		return super.getItemCount();
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }

	public void setItemSelected(View v,int index) {
		setItemSelected(selectedIndices, selectedViews, context, v, index);
	}
}
