package com.secb.android.view.components.filters_layouts;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.E_GuideLocationTypesOperation;
import com.secb.android.controller.backend.EventsCityOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EGuideLocationManager;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.model.EGuideLocationTypeItem;
import com.secb.android.model.EventsCityItem;
import com.secb.android.model.LocationsFilterData;
import com.secb.android.view.MainActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.EventFilterCitiesSpinnerAdapter;
import com.secb.android.view.components.NewDividerItemDecoration;
import com.secb.android.view.components.recycler_locations.LocationsTypesFilterRecyclerAdapter;

import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.List;

public class LocationsFilterLayout extends LinearLayout  implements  RequestObserver {
    private final View view;
	private final Context context;

	private LocationsFilterData locationsFilterData;
    private TextView txtv_location_filter_name_title, txtv_location_filter_city_title,
            txtv_location_filter_capacity_from_title, txtv_location_filter_capacity_to_title;
    private EditText txtv_location_filter_name_value,
            txtv_location_filter_capacity_from_value,
            txtv_location_filter_capacity_to_value;
    private CheckBox chkbox_type1, chkbox_type2, chkbox_type3, chkbox_type4;
	private Spinner spn_city;
	private RecyclerView locationTypesRecyclerView;
	private LocationsTypesFilterRecyclerAdapter locationsTypesFilterRecyclerAdapter;
	private TextView txtv_noData;

    private Button btn_applyFilter;
	private List<EGuideLocationTypeItem> locationTypesList;
	private List<EventsCityItem> citiesList ;
	private EventFilterCitiesSpinnerAdapter eventFilterCitiesSpinnerAdapter;
	private boolean isCategoryOperationDone;

	public View getLayoutView() {
        return view;
    }

    public LocationsFilterLayout(Context context) {
        super(context);
	    this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.locations_filter_screen, null);
	    ((MainActivity)context).setLocationRequstObserver(this);
        initViews(view);
        applyFonts(view);
	    locationTypesList = EGuideLocationManager.getInstance().getLocationTypesList(context);
	    if(locationTypesList==null || locationTypesList.size()==0)
	    {
		    startLocationTypesOperation();
	    }
	    citiesList = EventsManager.getInstance().getEventsCityList(context);
	    if(citiesList==null || citiesList.size()==0)
	    {
		    startLocationCitiesOperation();
	    }

	    if(citiesList!=null && locationTypesList!=null &&
			    citiesList.size()>0&& locationTypesList.size()>0)
	    {
		    getFilterData();
	    }

    }

    private void initViews(View view) {
        txtv_location_filter_name_title = (TextView) view.findViewById(R.id.txtv_location_filter_name_title);
        txtv_location_filter_city_title = (TextView) view.findViewById(R.id.txtv_location_filter_city_title);
        txtv_location_filter_capacity_from_title = (TextView) view.findViewById(R.id.txtv_location_filter_capacity_from_title);
        txtv_location_filter_capacity_to_title = (TextView) view.findViewById(R.id.txtv_location_filter_capacity_to_title);
        txtv_location_filter_name_value = (EditText) view.findViewById(R.id.txtv_location_filter_name_value);
	    spn_city = (Spinner) view.findViewById(R.id.spn_city_filter_city_value);
        txtv_location_filter_capacity_from_value = (EditText) view.findViewById(R.id.txtv_location_filter_capacity_from_value);
        txtv_location_filter_capacity_to_value = (EditText) view.findViewById(R.id.txtv_location_filter_capacity_to_value);
        chkbox_type1 = (CheckBox) view.findViewById(R.id.chkbox_type1);
        chkbox_type2 = (CheckBox) view.findViewById(R.id.chkbox_type2);
        chkbox_type3 = (CheckBox) view.findViewById(R.id.chkbox_type3);
        chkbox_type4 = (CheckBox) view.findViewById(R.id.chkbox_type4);
        btn_applyFilter = (Button) view.findViewById(R.id.btn_applyFilter);

	    txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
	    locationTypesRecyclerView = (RecyclerView) view.findViewById(R.id.locationTypesRecyclerView);
	    locationTypesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
	    locationTypesRecyclerView.addItemDecoration(new NewDividerItemDecoration(context));

	    locationTypesList= EGuideLocationManager.getInstance().getLocationTypesList(context);
	    citiesList = EventsManager.getInstance().getEventsCityList(context);

	    bindLocationTypesRecycler();
	    bindCitiesSpinner();


    }

	private void startLocationCitiesOperation() {
		EventsCityOperation operation = new EventsCityOperation(RequestIds.EVENTS_CITY_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	private void startLocationTypesOperation() {
		final E_GuideLocationTypesOperation operation = new E_GuideLocationTypesOperation(RequestIds.EGUIDE_LOCATION_TYPES_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	private void bindLocationTypesRecycler() {
		if (locationTypesList != null && locationTypesList.size() > 0)
		{
			locationTypesRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			locationsTypesFilterRecyclerAdapter = new LocationsTypesFilterRecyclerAdapter(context,locationTypesList);
			locationTypesRecyclerView.setAdapter(locationsTypesFilterRecyclerAdapter);
		}
		else
		{
			locationTypesRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			if(isCategoryOperationDone)
				txtv_noData.setText(context.getString(R.string.news_no_types));


		}
	}
	private void bindCitiesSpinner()
	{
		if(citiesList!=null && citiesList.size()>0)
		{
			eventFilterCitiesSpinnerAdapter =
					new EventFilterCitiesSpinnerAdapter(context,
							R.layout.spinner_simple_row,
							(ArrayList<EventsCityItem>) citiesList);
			spn_city.setAdapter(eventFilterCitiesSpinnerAdapter);
		}
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

    public LocationsFilterData getFilterData()
    {
        locationsFilterData = new LocationsFilterData();

        locationsFilterData.name = txtv_location_filter_name_value.getText().toString();

	    EventsCityItem selectedItem = ((EventsCityItem) spn_city.getSelectedItem());
	    locationsFilterData.city=selectedItem.ID;

	    if(!Utilities.isNullString(txtv_location_filter_capacity_from_value .getText().toString()))
            locationsFilterData.totalCapacityFrom = txtv_location_filter_capacity_from_value.getText().toString();
	    if(!Utilities.isNullString(txtv_location_filter_capacity_to_value .getText().toString()))
	        locationsFilterData.totalCapacityTo = txtv_location_filter_capacity_to_value.getText().toString();


        locationsFilterData.types = new ArrayList<>();
       /* if (chkbox_type1.isChecked()) {
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
        }*/

	    for(EGuideLocationTypeItem iterator:locationTypesList)
	    {
		    if(iterator.isSelected)
			    locationsFilterData.types.add(iterator.ID);
	    }
	    if(locationsFilterData.types.size()>0)
		    if (! locationsFilterData.types.get(0).equalsIgnoreCase("All"))
		        locationsFilterData.selectedType = TextUtils.join(",",locationsFilterData.types);
	        else
			    locationsFilterData.selectedType="All";
	    return locationsFilterData;
    }

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		locationTypesRecyclerView.setAdapter(null);
	}

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if (error == null)
		{
			if((int)requestId == RequestIds.EGUIDE_LOCATION_TYPES_REQUEST_ID )
			{
				isCategoryOperationDone=true;
				locationTypesList= EGuideLocationManager.getInstance().getLocationTypesList(context);
				bindLocationTypesRecycler();
			}

			else if ((int) requestId == RequestIds.EVENTS_CITY_REQUEST_ID ) {
				citiesList = EventsManager.getInstance().getEventsCityList(context);
				bindCitiesSpinner();
			}
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}
}
