package com.secb.android.view.components.recycler_e_service;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.view.CTApplication;

//view holder=============================  ======================
public class E_ServiceItemRecyclerViewHolder extends RecyclerView.ViewHolder
{
    TextView txtv_item_title;
    TextView txtv_date_title;
    TextView txtv_date_value;
    TextView txtv_status_title;
    TextView txtv_status_value;
    TextView txtv_number_title;
    TextView txtv_number_value;
    TextView txtv_type_title;
    TextView txtv_type_value;
    LinearLayout layout_eservice_colored_line;
    LinearLayout layout_data_holder;


    public E_ServiceItemRecyclerViewHolder(View itemView)
    {
        super(itemView);
        initViews(itemView);
        applyFonts(itemView);
    }

    private void initViews(View itemView) {
        txtv_item_title = (TextView) itemView.findViewById(R.id.txtv_item_title);
        txtv_date_title = (TextView) itemView.findViewById(R.id.txtv_date_title);
        txtv_date_value = (TextView) itemView.findViewById(R.id.txtv_date_value);
        txtv_status_title = (TextView) itemView.findViewById(R.id.txtv_status_title);
        txtv_status_value = (TextView) itemView.findViewById(R.id.txtv_status_value);
        txtv_number_title = (TextView) itemView.findViewById(R.id.txtv_number_title);
        txtv_number_value = (TextView) itemView.findViewById(R.id.txtv_number_value);
        txtv_type_title = (TextView) itemView.findViewById(R.id.txtv_type_title);
        txtv_type_value = (TextView) itemView.findViewById(R.id.txtv_type_value);
        layout_eservice_colored_line  = (LinearLayout) itemView.findViewById(R.id.layout_eservice_colored_line);
    }

    private void applyFonts(View itemView)
    {
        UiEngine.applyFontsForAll(CTApplication.getContext(),itemView,UiEngine.Fonts.HVAR);
        /*if(txtv_item_title !=null)
        {
            UiEngine.applyCustomFont(txtv_item_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_date_value !=null)
        {
            UiEngine.applyCustomFont(txtv_date_value, UiEngine.Fonts.HVAR);
        }
        if(txtv_status_value !=null)
        {
            UiEngine.applyCustomFont(txtv_status_value, UiEngine.Fonts.HVAR);
        }
        if(txtv_number_value !=null)
        {
            UiEngine.applyCustomFont(txtv_number_value, UiEngine.Fonts.HVAR);
        }
        if(txtv_type_value !=null)
        {
            UiEngine.applyCustomFont(txtv_type_value, UiEngine.Fonts.HVAR);
        }*/
    }

}