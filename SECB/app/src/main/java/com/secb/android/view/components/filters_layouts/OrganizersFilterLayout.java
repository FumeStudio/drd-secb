package com.secb.android.view.components.filters_layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.secb.android.R;
import com.secb.android.controller.backend.EventsCityOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.model.EventsCityItem;
import com.secb.android.model.OrganizersFilterData;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.EventFilterCitiesSpinnerAdapter;

import net.comptoirs.android.common.controller.backend.RequestObserver;

import java.util.ArrayList;
import java.util.List;

public class OrganizersFilterLayout extends LinearLayout implements RequestObserver{
    private final View view;
    private final Context context;

    private OrganizersFilterData organizersFilterData;
    private EditText edtxt_name;
	private Spinner spn_city;
	private List<EventsCityItem> citiesList;
	private EventFilterCitiesSpinnerAdapter eventFilterCitiesSpinnerAdapter;

	public View getLayoutView() {
        return view;
    }

    public OrganizersFilterLayout(Context context)
    {
        super(context);
        this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.organizers_filter_screen, null);
        initViews(view);
//	    ((MainActivity)context).setOrganizersRequstObserver(this);
        applyFonts(view);
	    if(citiesList!=null && citiesList.size()>0)
	    {
		    getFilterData();
	    }
    }

    private void initViews(View view)
    {
        organizersFilterData = new OrganizersFilterData();
        edtxt_name = (EditText) view.findViewById(R.id.txtv_news_filter_time_from_value);
	    spn_city = (Spinner) view.findViewById(R.id.spn_city_filter_city_value);
	    citiesList = EventsManager.getInstance().getEventsCityList(context);
	    if(citiesList==null || citiesList.size()==0)
	    {
		    startLocationCitiesOperation();
	    }
	    bindCitiesSpinner();
    }

	private void startLocationCitiesOperation() {
		EventsCityOperation operation = new EventsCityOperation(RequestIds.EVENTS_CITY_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	private void applyFonts(View view) {
        UiEngine.applyFontsForAll(context, view, UiEngine.Fonts.HVAR);
    }

    public OrganizersFilterData getFilterData()
    {
	    UiEngine.applyFontsForAll(context,view, UiEngine.Fonts.HVAR);
        organizersFilterData.name= edtxt_name.getText().toString();

	    EventsCityItem selectedItem = ((EventsCityItem) spn_city.getSelectedItem());
	    organizersFilterData.city=selectedItem.ID;

        return organizersFilterData;
    }

	public void clearFilters() {
		edtxt_name.setText("");
		edtxt_name.setHint(context.getString(R.string.hint_name));
		bindCitiesSpinner();
		organizersFilterData = new OrganizersFilterData();
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

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if(error==null)
		{
			if ((int) requestId == RequestIds.EVENTS_CITY_REQUEST_ID && resultObject != null)
			{
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
