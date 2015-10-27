package com.secb.android.view.components.recycler_locations;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

//view holder===================================================
public class LocationsTypesFilterRecyclerViewHolder extends RecyclerView.ViewHolder {
	CheckBox chkbox_locationTypeItem;
	View itemView;

	public LocationsTypesFilterRecyclerViewHolder(View itemView) {
		super(itemView);
		this.itemView=itemView;
		initViews(itemView);
		applyFonts();

	}

	private void initViews(View itemView) {
		chkbox_locationTypeItem = (CheckBox) itemView.findViewById(R.id.chkbox_locationTypeItem);
	}


	private void applyFonts() {
		if (chkbox_locationTypeItem != null) {
			UiEngine.applyCustomFont(chkbox_locationTypeItem, UiEngine.Fonts.HVAR);
		}
	}
}