package com.secb.android.view.components.recycler_events;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

//view holder=============================  ======================
public class EventItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_eventTitle;
    TextView txtv_eventDescription;
    TextView txtv_event_timeValue;
    TextView txtv_event_placeValue;
    TextView txtv_event_categoryValue;


    TextView txtv_eventImgDate_day;
    TextView txtv_eventImgDate_month;
    ImageView imgv_eventImg;

    public EventItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        initViews(itemView);
        applyFonts();
    }

    private void initViews(View itemView) {
        txtv_eventTitle = (TextView) itemView.findViewById(R.id.txtv_eventTitle);
        txtv_eventDescription = (TextView) itemView.findViewById(R.id.txtv_eventDescription);
        txtv_event_timeValue = (TextView) itemView.findViewById(R.id.txtv_event_timeValue);
        txtv_event_placeValue = (TextView) itemView.findViewById(R.id.txtv_event_placeValue);
        txtv_event_categoryValue = (TextView) itemView.findViewById(R.id.txtv_event_categoryValue);
	    txtv_eventImgDate_day = (TextView) itemView.findViewById(R.id.txtv_eventImgDate_day);
	    txtv_eventImgDate_month = (TextView) itemView.findViewById(R.id.txtv_eventImgDate_month);

        imgv_eventImg = (ImageView)itemView.findViewById(R.id.imgv_eventImg);
    }
    private void applyFonts() {
        if(txtv_eventTitle!=null)
        {
            UiEngine.applyCustomFont(txtv_eventTitle, UiEngine.Fonts.HVAR);
        }
        if(txtv_eventDescription!=null)
        {
            UiEngine.applyCustomFont(txtv_eventDescription, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_timeValue!=null)
        {
            UiEngine.applyCustomFont(txtv_event_timeValue, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_placeValue!=null)
        {
            UiEngine.applyCustomFont(txtv_event_placeValue, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_categoryValue!=null)
        {
            UiEngine.applyCustomFont(txtv_event_categoryValue, UiEngine.Fonts.HVAR);
        }
        if(txtv_eventImgDate_day!=null)
        {
            UiEngine.applyCustomFont(txtv_eventImgDate_day, UiEngine.Fonts.HVAR_BOLD);
        }
        if(txtv_eventImgDate_month!=null)
        {
            UiEngine.applyCustomFont(txtv_eventImgDate_month, UiEngine.Fonts.HVAR);
        }
    }

}