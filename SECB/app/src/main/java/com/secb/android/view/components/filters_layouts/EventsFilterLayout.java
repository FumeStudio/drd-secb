package com.secb.android.view.components.filters_layouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.EventsCategoryOperation;
import com.secb.android.controller.backend.EventsCityOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.model.Consts;
import com.secb.android.model.EventsCategoryItem;
import com.secb.android.model.EventsCityItem;
import com.secb.android.model.EventsFilterData;
import com.secb.android.view.MainActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.EventFilterCitiesSpinnerAdapter;
import com.secb.android.view.components.dialogs.DateTimePickerDialogView;
import com.secb.android.view.components.recycler_events.EventsCategoryFilterRecyclerAdapter;

import net.comptoirs.android.common.controller.backend.RequestObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventsFilterLayout extends LinearLayout implements View.OnClickListener, RequestObserver {

	private final View view;

    private EventsFilterData eventsFilterData;
    private Spinner spn_city;
    private TextView txtv_timeFrom, txtv_timeTo;
    private TextView txtv_news_filter_category_title,
            txtv_event_filter_city_title ,
            txtv_event_filter_time_from_title ,
            txtv_event_filter_time_to_title ;

	private String selectedDateFrom,selectedDateTo;
    private Button btn_applyFilter;

	private TextView txtv_noData;
	private RecyclerView eventsCategoriesRecyclerView;
	private EventsCategoryFilterRecyclerAdapter eventsCategoryFilterRecyclerAdapter;



    private Context context;
	private List<EventsCategoryItem> categoriesList ;
	private List<EventsCityItem> citiesList ;
	private EventFilterCitiesSpinnerAdapter eventFilterCitiesSpinnerAdapter;
	private boolean isCategoryOperationDone;
	private boolean isCityOperationDone;


	public View getLayoutView() {
        return view;
    }

    public EventsFilterLayout(Context context) {
        super(context);
        this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.events_filter_screen, null);
        initViews(view);
	    ((MainActivity)context).setEventsRequstObserver(this);
        applyFonts();
	    categoriesList = EventsManager.getInstance().getEventsCategoryList(context);
	    if(categoriesList==null || categoriesList.size()==0)
	    {
		    startEventsCategoriesOperation();
	    }
	    citiesList = EventsManager.getInstance().getEventsCityList(context);
	    if(citiesList==null || citiesList.size()==0)
	    {
		    startEventsCitiesOperation();
	    }

	    if(citiesList!=null && categoriesList!=null &&
			    citiesList.size()>0&& categoriesList.size()>0)
	    {
		    getFilterData();
	    }
    }

	private void startEventsCitiesOperation() {
		EventsCityOperation operation = new EventsCityOperation(RequestIds.EVENTS_CITY_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	private void startEventsCategoriesOperation() {
		EventsCategoryOperation operation = new EventsCategoryOperation(RequestIds.EVENTS_CATEGORY_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}


	public void initViews(View view) {
        spn_city = (Spinner) view.findViewById(R.id.spn_city_filter_city_value);
        txtv_timeFrom = (TextView) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_timeTo = (TextView) view.findViewById(R.id.txtv_news_filter_time_to_value);

        txtv_news_filter_category_title = (TextView) view.findViewById(R.id.txtv_news_filter_category_title);
        txtv_event_filter_city_title = (TextView) view.findViewById(R.id.txtv_event_filter_city_title);
        txtv_event_filter_time_from_title = (TextView) view.findViewById(R.id.txtv_event_filter_time_from_title);
        txtv_event_filter_time_to_title = (TextView) view.findViewById(R.id.txtv_event_filter_time_to_title);


        btn_applyFilter = (Button) view.findViewById(R.id.btn_applyFilter);

	    txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
	    eventsCategoriesRecyclerView = (RecyclerView) view.findViewById(R.id.eventsCategoriesRecyclerView);
	    eventsCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
	    categoriesList= EventsManager.getInstance().getEventsCategoryList(context);
	    citiesList = EventsManager.getInstance().getEventsCityList(context);

	    bindCategoriesRecycler();
	    bindCitiesSpinner();

	    txtv_timeFrom.setOnClickListener(this);
        txtv_timeTo.setOnClickListener(this);

    }

	private void bindCategoriesRecycler() {
		if (categoriesList != null && categoriesList.size() > 0)
		{
			eventsCategoriesRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			eventsCategoryFilterRecyclerAdapter = new EventsCategoryFilterRecyclerAdapter(context,categoriesList);
			eventsCategoriesRecyclerView.setAdapter(eventsCategoryFilterRecyclerAdapter);
		}
		else
		{
			eventsCategoriesRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			if(isCategoryOperationDone)
				txtv_noData.setText(context.getString(R.string.news_no_types));
		}
	}

	private void bindCitiesSpinner()
	{
		if(citiesList!=null && citiesList.size()>0){
			/*ArrayAdapter adapter = new ArrayAdapter(context,
					R.layout.spinner_simple_row,
					citiesList);
		spn_city.setAdapter(adapter);*/

			/*android.R.layout.simple_spinner_dropdown_item,*/
			eventFilterCitiesSpinnerAdapter =
					new EventFilterCitiesSpinnerAdapter(context,
							R.layout.spinner_simple_row,
							(ArrayList<EventsCityItem>) citiesList);
			spn_city.setAdapter(eventFilterCitiesSpinnerAdapter);

//			ArrayAdapter adapter = ArrayAdapter.createFromResource(context, R.array.cities_array, R.layout.spinner_simple_row);
//			spn_city.setAdapter(adapter);

		}
	}
	private void applyFonts()
    {
	    UiEngine.applyFontsForAll(context,view,UiEngine.Fonts.HVAR);
        /*if(spn_city !=null)
        {
            UiEngine.applyCustomFont(spn_city, UiEngine.Fonts.HVAR);
        }
        if(txtv_timeFrom!=null)
        {
            UiEngine.applyCustomFont(txtv_timeFrom, UiEngine.Fonts.HVAR);
        }
        if(txtv_timeTo!=null)
        {
            UiEngine.applyCustomFont(txtv_timeTo, UiEngine.Fonts.HVAR);
        }
        if(txtv_news_filter_category_title!=null)
        {
            UiEngine.applyCustomFont(txtv_news_filter_category_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_filter_city_title!=null)
        {
            UiEngine.applyCustomFont(txtv_event_filter_city_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_filter_time_from_title!=null)
        {
            UiEngine.applyCustomFont(txtv_event_filter_time_from_title, UiEngine.Fonts.HVAR);
        }
        if(radbtn_allTypes!=null)
        {
            UiEngine.applyCustomFont(radbtn_allTypes, UiEngine.Fonts.HVAR);
        }
        if(radbtn_economicType!=null)
        {
            UiEngine.applyCustomFont(radbtn_economicType, UiEngine.Fonts.HVAR);
        }
        if(radbtn_politicalType!=null)
        {
            UiEngine.applyCustomFont(radbtn_politicalType, UiEngine.Fonts.HVAR);
        }
        if(radbtn_publicType!=null)
        {
            UiEngine.applyCustomFont(radbtn_publicType, UiEngine.Fonts.HVAR);
        }
        if(btn_applyFilter!=null)
        {
            UiEngine.applyCustomFont(btn_applyFilter, UiEngine.Fonts.HVAR);
        }
	    if(txtv_noData!=null)
	    {
		    UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
	    }*/
    }

    public EventsFilterData getFilterData()
    {
        eventsFilterData = new EventsFilterData();

//        eventsFilterData.city = ((EventsCityItem)spn_city.getSelectedItem()).CityArabic.toString();

	    EventsCityItem selectedItem = ((EventsCityItem) spn_city.getSelectedItem());
	    eventsFilterData.city=selectedItem.ID;
	    eventsFilterData.timeFrom =selectedDateFrom /*txtv_timeFrom.getText().toString()*/;
        eventsFilterData.timeTo = selectedDateTo/*txtv_timeTo.getText().toString()*/;
	    EventsCategoryItem selectedCategory = EventsManager.getInstance().getSelectedCategory();
	    if( selectedCategory!=null)
	    {
		    eventsFilterData.eventsCategory = (UiEngine.isAppLanguageArabic(context)?
				    selectedCategory.TitleArabic:selectedCategory.TitleEnglish);
		    eventsFilterData.selectedCategoryId = selectedCategory.ID;

	    }
       /* switch (radgro_newsTypes.getCheckedRadioButtonId()) {
            case R.id.radbtn_allTypes:
                eventsFilterData.type = EventsFilterData.TYPE_ALL;
                break;
            case R.id.radbtn_economicType:
                eventsFilterData.type = EventsFilterData.TYPE_ECONOMIC;
                break;
            case R.id.radbtn_politicalType:
                eventsFilterData.type = EventsFilterData.TYPE_POLITICAL;
                break;
            case R.id.radbtn_publicType:
                eventsFilterData.type = EventsFilterData.TYPE_PUBLIC;
                break;
        }*/
        return eventsFilterData;
    }


	public void clearFilters(){
		txtv_timeFrom.setText("");
		txtv_timeFrom.setHint(context.getString(R.string.filter_time_from));
		txtv_timeTo.setText("");
		txtv_timeTo.setHint(context.getString(R.string.filter_time_to));

		this.eventsFilterData = new EventsFilterData();
		categoriesList= EventsManager.getInstance().getEventsCategoryList(context);
//		citiesList = EventsManager.getInstance().getEventsCityList(context);

		bindCategoriesRecycler();
		bindCitiesSpinner();
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtv_news_filter_time_from_value:
                showDateTimePicker(txtv_timeFrom , true );
                break;
            case R.id.txtv_news_filter_time_to_value:
                showDateTimePicker(txtv_timeTo , false);
            break;
        }
    }

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		eventsCategoriesRecyclerView.setAdapter(null);
	}
    public void showDateTimePicker(final TextView textView , final boolean isDateFrom)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DateTimePickerDialogView dialogView = new DateTimePickerDialogView(context);
        builder.setView(dialogView);

        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
	            Calendar selectedDateCalendar = dialogView.getSelectedDateTime();
	            if(isDateFrom)
	            {
		            selectedDateCalendar.set(selectedDateCalendar.get(Calendar.YEAR) ,
				            selectedDateCalendar.get(Calendar.MONTH),
				            selectedDateCalendar.get(Calendar.DAY_OF_MONTH) ,
				            1 ,0 ,0 /*hr, min , sec*/
				            );
		            selectedDateFrom = MainActivity.sdf_Source_News.format(new Date(selectedDateCalendar.getTimeInMillis()));

	            }
	            else{/*is Date to*/
		            selectedDateCalendar.set(selectedDateCalendar.get(Calendar.YEAR) ,
				            selectedDateCalendar.get(Calendar.MONTH),
				            selectedDateCalendar.get(Calendar.DAY_OF_MONTH) ,
				            23 ,59,59 /*hr, min , sec*/
		            );
		            selectedDateTo = MainActivity.sdf_Source_News.format(new Date(selectedDateCalendar.getTimeInMillis()));

	            }

	             CharSequence time = Consts.APP_DEFAULT_DATE_TIME_FORMAT.format(new Date(selectedDateCalendar.getTimeInMillis()));
                textView.setText(time);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if (error == null)
		{
			if((int)requestId == RequestIds.EVENTS_CATEGORY_REQUEST_ID )
			{
				isCategoryOperationDone =true;
				categoriesList= EventsManager.getInstance().getEventsCategoryList(context);
				bindCategoriesRecycler();
			}
			else if ((int) requestId == RequestIds.EVENTS_CITY_REQUEST_ID )
			{
				isCityOperationDone =true;
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
