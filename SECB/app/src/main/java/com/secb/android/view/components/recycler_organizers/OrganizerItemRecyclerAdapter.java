package com.secb.android.view.components.recycler_organizers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.model.OrganizerItem;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class OrganizerItemRecyclerAdapter extends RecyclerView.Adapter<OrganizerItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<OrganizerItem>itemsList = Collections.emptyList();
    Context context;

    public OrganizerItemRecyclerAdapter(Context context, List<OrganizerItem> itemsList) {
	    this.context=context;
	    if(context == null)
		    this.context= CTApplication.getContext();

        this.itemsList = itemsList;
	    this.inflater = LayoutInflater.from(context);
    }

    @Override
    public OrganizerItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

	    if(inflater==null && context!=null)
		    this.inflater = LayoutInflater.from(context);

	    View view = inflater.inflate(R.layout.organizer_item_card, parent, false);

        OrganizerItemRecyclerViewHolder vh = new OrganizerItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(OrganizerItemRecyclerViewHolder holder, int position) {
        OrganizerItem currentItem = itemsList.get(position);
	    if(!Utilities.isNullString(currentItem.OrganizerImage))
	    {
		    Glide.with(context)
				    .load(currentItem.OrganizerImage)
                    .centerCrop()
				    .placeholder(R.drawable.organizer_place_holder)
				    .into(holder.imgv_organizerImg);
	    }
	    else
		    holder.imgv_organizerImg.setImageResource(R.drawable.organizer_place_holder);

        holder.txtv_organizerTitle.setText(currentItem.OrganizerName);
        holder.txtv_organizerDescription.setText(currentItem.OrganizerDescription);
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
