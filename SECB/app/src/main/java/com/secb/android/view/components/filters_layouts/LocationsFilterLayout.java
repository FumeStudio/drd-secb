package com.secb.android.view.components.filters_layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.LocationsFilterData;
import com.secb.android.view.UiEngine;

import java.util.ArrayList;

public class LocationsFilterLayout extends LinearLayout {
    private final View view;
	private final Context context;

	private LocationsFilterData locationsFilterData;
    private TextView txtv_location_filter_name_title, txtv_location_filter_city_title,
            txtv_location_filter_capacity_from_title, txtv_location_filter_capacity_to_title;
    private EditText txtv_location_filter_name_value,
            txtv_location_filter_city_value,
            txtv_location_filter_capacity_from_value,
            txtv_location_filter_capacity_to_value;
    private CheckBox chkbox_type1, chkbox_type2, chkbox_type3, chkbox_type4;
    private Button btn_applyFilter;

    public View getLayoutView() {
        return view;
    }

    public LocationsFilterLayout(Context context) {
        super(context);
	    this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.locations_filter_screen, null);
        initViews(view);
        applyFonts(view);
        getFilterData();
    }

    private void initViews(View view) {
        txtv_location_filter_name_title = (TextView) view.findViewById(R.id.txtv_location_filter_name_title);
        txtv_location_filter_city_title = (TextView) view.findViewById(R.id.txtv_location_filter_city_title);
        txtv_location_filter_capacity_from_title = (TextView) view.findViewById(R.id.txtv_location_filter_capacity_from_title);
        txtv_location_filter_capacity_to_title = (TextView) view.findViewById(R.id.txtv_location_filter_capacity_to_title);
        txtv_location_filter_name_value = (EditText) view.findViewById(R.id.txtv_location_filter_name_value);
        txtv_location_filter_city_value = (EditText) view.findViewById(R.id.txtv_location_filter_city_value);
        txtv_location_filter_capacity_from_value = (EditText) view.findViewById(R.id.txtv_location_filter_capacity_from_value);
        txtv_location_filter_capacity_to_value = (EditText) view.findViewById(R.id.txtv_location_filter_capacity_to_value);
        chkbox_type1 = (CheckBox) view.findViewById(R.id.chkbox_type1);
        chkbox_type2 = (CheckBox) view.findViewById(R.id.chkbox_type2);
        chkbox_type3 = (CheckBox) view.findViewById(R.id.chkbox_type3);
        chkbox_type4 = (CheckBox) view.findViewById(R.id.chkbox_type4);
        btn_applyFilter = (Button) view.findViewById(R.id.btn_applyFilter);


    }

    private void applyFonts(View view)
    {
        UiEngine.applyFontsForAll(context,view, UiEngine.Fonts.HVAR);
        /*
        if (txtv_location_filter_name_title != null) {
            UiEngine.applyCustomFont(txtv_location_filter_name_title, UiEngine.Fonts.HVAR);
        }
        if (txtv_location_filter_city_title != null) {
            UiEngine.applyCustomFont(txtv_location_filter_city_title, UiEngine.Fonts.HVAR);
        }
        if (txtv_location_filter_capacity_from_title != null) {
            UiEngine.applyCustomFont(txtv_location_filter_capacity_from_title, UiEngine.Fonts.HVAR);
        }
        if (txtv_location_filter_capacity_to_title != null) {
            UiEngine.applyCustomFont(txtv_location_filter_capacity_to_title, UiEngine.Fonts.HVAR);
        }
        if (txtv_location_filter_name_value != null) {
            UiEngine.applyCustomFont(txtv_location_filter_name_value, UiEngine.Fonts.HVAR);
        }
        if (txtv_location_filter_city_value != null) {
            UiEngine.applyCustomFont(txtv_location_filter_city_value, UiEngine.Fonts.HVAR);
        }
        if (txtv_location_filter_capacity_from_value != null) {
            UiEngine.applyCustomFont(txtv_location_filter_capacity_from_value, UiEngine.Fonts.HVAR);
        }
        if (txtv_location_filter_capacity_to_value != null) {
            UiEngine.applyCustomFont(txtv_location_filter_capacity_to_value, UiEngine.Fonts.HVAR);
        }
        if (chkbox_type1 != null) {
            UiEngine.applyCustomFont(chkbox_type1, UiEngine.Fonts.HVAR);
        }
        if (chkbox_type2 != null) {
            UiEngine.applyCustomFont(chkbox_type2, UiEngine.Fonts.HVAR);
        }
        if (chkbox_type3 != null) {
            UiEngine.applyCustomFont(chkbox_type3, UiEngine.Fonts.HVAR);
        }
        if (chkbox_type4 != null) {
            UiEngine.applyCustomFont(chkbox_type4, UiEngine.Fonts.HVAR);
        }
        if (btn_applyFilter != null) {
            UiEngine.applyCustomFont(btn_applyFilter, UiEngine.Fonts.HVAR);
        }*/
    }

    public LocationsFilterData getFilterData() {
        locationsFilterData = new LocationsFilterData();

        locationsFilterData.name = txtv_location_filter_name_value.getText().toString();
        locationsFilterData.city = txtv_location_filter_city_value.getText().toString();
        locationsFilterData.totalCapacityFrom = txtv_location_filter_capacity_from_value.getText().toString();
        locationsFilterData.totalCapacityTo = txtv_location_filter_capacity_to_value.getText().toString();


        locationsFilterData.types = new ArrayList<>();
        if (chkbox_type1.isChecked()) {
            locationsFilterData.types.add(1);
        }
        if (chkbox_type2.isChecked()) {
            locationsFilterData.types.add(2);
        }
        if (chkbox_type3.isChecked()) {
            locationsFilterData.types.add(3);
        }
        if (chkbox_type4.isChecked()) {
            locationsFilterData.types.add(4);
        }
        return locationsFilterData;
    }

}
