package com.secb.android.view.components.recycler_e_service;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

//view holder===================================================
public class EServiceFilterRecyclerViewHolder extends RecyclerView.ViewHolder {
	RadioButton radbtn_categoryItem;
	View itemView;

	public EServiceFilterRecyclerViewHolder(View itemView) {
		super(itemView);
		this.itemView=itemView;
		initViews(itemView);
		applyFonts();

	}

	private void initViews(View itemView) {
		radbtn_categoryItem = (RadioButton) itemView.findViewById(R.id.radbtn_categoryItem);
	}


	private void applyFonts() {
		if (radbtn_categoryItem != null) {
			UiEngine.applyCustomFont(radbtn_categoryItem, UiEngine.Fonts.HVAR);
		}
	}
}