package com.secb.android.view.components.recycler_locations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.EGuideLocationTypeItem;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class LocationsTypesFilterRecyclerAdapter extends RecyclerView.Adapter<LocationsTypesFilterRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<EGuideLocationTypeItem>itemsList = Collections.emptyList();
    Context context;

    public LocationsTypesFilterRecyclerAdapter(Context context, List<EGuideLocationTypeItem> itemsList) {
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
    public LocationsTypesFilterRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if(inflater==null && context!=null)
			this.inflater = LayoutInflater.from(context);

		View view = inflater.inflate(R.layout.location_type_filter_item_card, parent, false);

		LocationsTypesFilterRecyclerViewHolder vh = new LocationsTypesFilterRecyclerViewHolder(view);
        return vh;
    }


	@Override
    public void onBindViewHolder(final LocationsTypesFilterRecyclerViewHolder holder, final int position)
    {
	    final EGuideLocationTypeItem currentItem = itemsList.get(position);
	    String radbtn_text = UiEngine.isAppLanguageArabic(context)?currentItem.SiteTypeArabic:currentItem.SiteTypeEnglish;
	    holder.chkbox_locationTypeItem.setText(radbtn_text);
	    holder.chkbox_locationTypeItem.setChecked(currentItem.isSelected);

	    holder.chkbox_locationTypeItem.setOnClickListener(new OnClickListener(){

		    @Override
		    public void onClick(View v) {
			    notifyDataSetChanged();
			    currentItem.isSelected=holder.chkbox_locationTypeItem.isChecked();
			    notifyDataSetChanged();
		    }
	    });
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
