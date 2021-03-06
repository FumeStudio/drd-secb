package com.secb.android.view.components.recycler_e_service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.E_ServiceRequestWorkSpaceModeItem;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class E_ServiceRequestWorkSpaceModeRecyclerAdapter extends RecyclerView.Adapter<EServiceFilterRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<E_ServiceRequestWorkSpaceModeItem> itemsList = Collections.emptyList();
    Context context;

    public E_ServiceRequestWorkSpaceModeRecyclerAdapter(Context context, List<E_ServiceRequestWorkSpaceModeItem> itemsList) {
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
    public EServiceFilterRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if(inflater==null && context!=null)
			this.inflater = LayoutInflater.from(context);

		View view = inflater.inflate(R.layout.events_category_filter_item_card, parent, false);

		EServiceFilterRecyclerViewHolder vh = new EServiceFilterRecyclerViewHolder(view);
        return vh;
    }


	@Override
    public void onBindViewHolder(final EServiceFilterRecyclerViewHolder holder, final int position)
    {
	    E_ServiceRequestWorkSpaceModeItem currentItem = itemsList.get(position);
	    String radbtn_text = UiEngine.isAppLanguageArabic(context)?currentItem.NameAr:currentItem.NameEn;
	    holder.radbtn_categoryItem.setText(radbtn_text);
	    holder.radbtn_categoryItem.setChecked(currentItem.isSelected);

/*	    holder.radbtn_categoryItem.setOnClickListener(new OnClickListener(){

		    @Override
		    public void onClick(View v) {
			    setItemChecked(position,holder.radbtn_categoryItem.isChecked());
		    }
	    });*/
    }

	public void setItemChecked(int position/*, boolean checked*/) {
//		if(checked)
		{
			for (int i = 0 ; i<this.itemsList.size();i++)
			{
				if(i==position)
					this.itemsList.get(i).isSelected=true;
				else
					this.itemsList.get(i).isSelected=false;
			}
			notifyDataSetChanged();

		}
	}

	@Override
    public int getItemCount()
	{
		if(itemsList!=null)
            return itemsList.size();
		return 0;
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


}
