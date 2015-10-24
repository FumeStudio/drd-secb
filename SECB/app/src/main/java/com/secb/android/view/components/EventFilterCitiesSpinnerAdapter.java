package com.secb.android.view.components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.EventsCityItem;
import com.secb.android.view.UiEngine;

import java.util.ArrayList;

public class EventFilterCitiesSpinnerAdapter extends ArrayAdapter<EventsCityItem> {
	private final ArrayList<EventsCityItem> cityItems;
	private final Context context;
	boolean isArabic;

	public EventFilterCitiesSpinnerAdapter(Context context, int textViewResourceId, ArrayList<EventsCityItem> cityItems) {
		super(context, textViewResourceId,cityItems);
		this.context = context;
		this.cityItems = cityItems;
		this.isArabic = UiEngine.isAppLanguageArabic(context);
	}

	public int getCount(){
		return cityItems.size();
	}

	public EventsCityItem getItem(int position){
		return cityItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView label = new TextView(context);
		UiEngine.applyCustomFont(label,UiEngine.Fonts.HVAR);
		label.setTextColor(context.getResources().getColor(R.color.secb_dark_text));
		EventsCityItem currentItem = cityItems.get(position);
		if (currentItem != null)
			label.setText(isArabic ? currentItem.CityArabic + "" : currentItem.CityEnglish + "");
		return label;
	}


	@Override
	public View getDropDownView(int position, View convertView,
	                            ViewGroup parent) {
		TextView label = new TextView(context);
		UiEngine.applyCustomFont(label,UiEngine.Fonts.HVAR);
		label.setTextColor(context.getResources().getColor(R.color.secb_dark_text));
		label.setBackgroundColor(context.getResources().getColor(R.color.off_white));
		EventsCityItem currentItem = cityItems.get(position);
		if (currentItem != null)
			label.setText(isArabic ? currentItem.CityArabic + "" : currentItem.CityEnglish + "");
		return label;
	}



/*	public View getCustomView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);

		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.spinner_simple_row, parent, false);
		TextView label = (TextView) row.findViewById(R.id.txtv_spinner_item);
		EventsCityItem currentItem = cityItems.get(position);
		if (currentItem != null)
			label.setText(isArabic ? currentItem.CityArabic + "" : currentItem.CityEnglish + "");
		return row;
	}*/

}
