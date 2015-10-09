package com.secb.android.view.components.filters_layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.secb.android.R;
import com.secb.android.model.LocationsFilterData;

import java.util.ArrayList;

public class LocationsFilterLayout extends LinearLayout {
    private final View view;

    private LocationsFilterData locationsFilterData;
    private EditText txtv_location_filter_name_value,
            txtv_location_filter_city_value,
            txtv_location_filter_capacity_from_value,
            txtv_location_filter_capacity_to_value;
    private CheckBox chkbox_type1, chkbox_type2, chkbox_type3, chkbox_type4;

    public View getLayoutView() {
        return view;
    }

    public LocationsFilterLayout(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.locations_filter_screen, null);
        getFilterData();
    }

    public LocationsFilterData getFilterData()
    {
        locationsFilterData = new LocationsFilterData();
        txtv_location_filter_name_value = (EditText) view.findViewById(R.id.txtv_location_filter_name_value);
        txtv_location_filter_city_value = (EditText) view.findViewById(R.id.txtv_location_filter_city_value);
        txtv_location_filter_capacity_from_value = (EditText) view.findViewById(R.id.txtv_location_filter_capacity_from_value);
        txtv_location_filter_capacity_to_value = (EditText) view.findViewById(R.id.txtv_location_filter_capacity_to_value);
        chkbox_type1 = (CheckBox) view.findViewById(R.id.chkbox_type1);
        chkbox_type2 = (CheckBox) view.findViewById(R.id.chkbox_type2);
        chkbox_type3 = (CheckBox) view.findViewById(R.id.chkbox_type3);
        chkbox_type4 = (CheckBox) view.findViewById(R.id.chkbox_type4);


        locationsFilterData.name = txtv_location_filter_name_value.getText().toString();
        locationsFilterData.city = txtv_location_filter_city_value.getText().toString();
        locationsFilterData.totalCapacityFrom = txtv_location_filter_capacity_from_value.getText().toString();
        locationsFilterData.totalCapacityTo = txtv_location_filter_capacity_to_value.getText().toString();


        locationsFilterData.types = new ArrayList<>();
        if (chkbox_type1.isChecked()){locationsFilterData.types.add(1); }
        if (chkbox_type2.isChecked()){locationsFilterData.types.add(2); }
        if (chkbox_type3.isChecked()){locationsFilterData.types.add(3); }
        if (chkbox_type4.isChecked()){locationsFilterData.types.add(4); }
        return locationsFilterData;
    }

}
