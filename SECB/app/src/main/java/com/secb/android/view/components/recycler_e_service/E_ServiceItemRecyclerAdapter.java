package com.secb.android.view.components.recycler_e_service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.E_ServiceItem;

import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class E_ServiceItemRecyclerAdapter extends RecyclerView.Adapter<E_ServiceItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<E_ServiceItem>itemsList = Collections.emptyList();
    Context context;

    public E_ServiceItemRecyclerAdapter(Context context, List<E_ServiceItem> itemsList) {

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
        E_ServiceItem currentItem = itemsList.get(position);
        holder.txtv_item_title.setText(currentItem.title);
        holder.txtv_date_value.setText(currentItem.date);
        holder.txtv_status_value.setText(currentItem.status);
        holder.txtv_number_value.setText(currentItem.number);
        holder.txtv_type_value.setText(currentItem.type);
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
