package com.secb.android.view.components.recycler_e_service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.view.MainActivity;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class E_ServiceItemRecyclerAdapter extends RecyclerView.Adapter<E_ServiceItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<E_ServiceRequestItem>itemsList = Collections.emptyList();
    Context context;

    public E_ServiceItemRecyclerAdapter(Context context, List<E_ServiceRequestItem> itemsList) {

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
    public E_ServiceItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

	    if(inflater==null && context!=null)
		    this.inflater = LayoutInflater.from(context);

	    View view = inflater.inflate(R.layout.e_service_item_card, parent, false);

        E_ServiceItemRecyclerViewHolder vh = new E_ServiceItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(E_ServiceItemRecyclerViewHolder holder, int position) {
        E_ServiceRequestItem currentItem = itemsList.get(position);
	    if(!Utilities.isNullString(currentItem.title))
	    {
		    holder.txtv_item_title.setVisibility(View.VISIBLE);
		    holder.txtv_item_title.setText(currentItem.title);
	    }
	    else{
		    holder.txtv_item_title.setVisibility(View.GONE);
	    }
        String newDate = "";
        if(!Utilities.isNullString(currentItem.RequestDate))
            newDate = MainActivity.reFormatDate(currentItem.RequestDate, MainActivity.sdf_Source_News, MainActivity.sdf_Source_EService);
        holder.txtv_date_value.setText(newDate);
        holder.txtv_status_value.setText(currentItem.ReuquestStatusSingleValue);
        holder.txtv_number_value.setText(currentItem.RequestNumber);
        holder.txtv_type_value.setText(currentItem.RequestType);

        if(!Utilities.isNullString(currentItem.ReuquestStatusSingleValue)) {
            if(currentItem.ReuquestStatusSingleValue.compareToIgnoreCase(MainActivity.STATUS_INBOX) == 0) {
                holder.layout_eservice_colored_line.setBackgroundColor(context.getResources().getColor(R.color.graph_color_inbox));
            } else if(currentItem.ReuquestStatusSingleValue.compareToIgnoreCase(MainActivity.STATUS_CLOSEDREQUESTS) == 0) {
                holder.layout_eservice_colored_line.setBackgroundColor(context.getResources().getColor(R.color.graph_color_closed));
            } else if(currentItem.ReuquestStatusSingleValue.compareToIgnoreCase(MainActivity.STATUS_INPROGRESS) == 0) {
                holder.layout_eservice_colored_line.setBackgroundColor(context.getResources().getColor(R.color.graph_color_inProgress));
            }
        }
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
