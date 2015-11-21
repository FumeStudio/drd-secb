package com.secb.android.view;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.model.EventItem;
import com.secb.android.view.fragments.EventDetailsFragment;
import com.secb.android.view.fragments.EventsCalendarFragment;
import com.secb.android.view.fragments.EventsListFragment;
import com.secb.android.view.fragments.SECBBaseFragment;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class EventsActivity extends SECBBaseActivity implements RequestObserver {
	private static final String TAG = "EventsActivity";
//this activity for events calendar , events list fragment , events details fragment

	ArrayList<RequestObserver> eventsRequstObserverList;
	public EventItem currentEventsItemDetails;
	public boolean isDoublePane=false;
	private ArrayList<EventItem> eventsList;
	private ArrayList<EventItem> first3EventsList;
	public boolean isComingFromMenu =true   ;
	private TextView txtv_viewAllEvents;

	public EventsActivity() {
		super(R.layout.events_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		isDoublePane=findViewById(R.id.events_details_container)!=null;
		eventsList = EventsManager.getInstance().getMonthEventsList();
		applyFonts();

		if(getIntent()!=null && getIntent().getExtras()!=null &&
				getIntent().getExtras().containsKey("item")/* &&
				getIntent().getExtras().get("item")!=null*/)
		{
			//coming from home
			isComingFromMenu=false;
			EventItem item = (EventItem) getIntent().getExtras().get("item");
//
//			if(Utilities.isTablet(this))
				viewAllEvents(item);
//			else
//				openEventsListFragment(eventsList);
		}
		else
		{
			openEventCalendar();
			/*openEventsListFragment();

			//load details of first item
			if(isDoublePane && eventsList != null && eventsList .size() > 0)
			{

				openEventDetailsFragment(eventsList .get(0));
			}*/
		}



	}

	private void initObservers() {
		eventsRequstObserverList = new ArrayList<>();
	}

	private void initViews() {

		txtv_viewAllEvents = (TextView) findViewById(R.id.txtv_viewAllEvents);
		if(txtv_viewAllEvents !=null)
		{
			txtv_viewAllEvents.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
			txtv_viewAllEvents.setOnClickListener(this);
		}
	}

	private void applyFonts() {
		if (txtv_viewAllEvents != null)
		UiEngine.applyCustomFont(txtv_viewAllEvents, UiEngine.Fonts.HVAR);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

			case R.id.txtv_viewAllEvents:
				viewAllEvents(null);
				break;
			default:
				break;
		}
	}

	public void viewAllEvents(EventItem item)
	{
		//1-open events list with list of this month events
		eventsList =EventsManager.getInstance().getMonthEventsList();
		openEventsListFragment(eventsList);

		//2-open eventDetails of first event in case of tablet
		if(item==null)
		{
			if(eventsList!=null && eventsList.size()>0 && isDoublePane)
			{
				item=eventsList.get(0);
				openEventDetailsFragment(item);
			}
		}
		//-open event details of an item
		else
		{
			openEventDetailsFragment(item);
		}


		//3-hide "view All Events" text
		if(txtv_viewAllEvents!=null)
			txtv_viewAllEvents.setVisibility(View.GONE);

		//4-set header title "Event Details"
		//5-enable header back button
		//6-disable header menu button
			//done by event details fragment onResume

		//7-enable filter button
	}

	public void openEventCalendar(){
		//1-open calendar
		//2-open events list with list of this ( day OR month )
		//3-show "view All Events" text
		//4-set header title "Event"
		//5-disable header back button
		//6-enable header menu button
		//7-on Day click update events list with list of this day events
		//8-on Item click call viewAllEvents()
		//9-enable header filter button


		//1-open calendar
		openEventsCalendarFragment();

		//2-open eventsList with max 3 events of this month
		// when event calendar finish loading the list it will call updateEventsList() ;

		//3-show "view All Events" text
		//updateEventsList() will handle it

		//4-set header title "Event"
		//5-disable header back button
		//6-enable header menu button
		//done by events calendar fragment in onResume()

		//7-on Day click update events list with list of this day events
		//done by events calendar fragment in onDateClick()

		//8-on Item click call viewAllEvents()

	}


	//events
	public void setEventsRequstObserver(RequestObserver newsRequstObserver) {

		eventsRequstObserverList.add(newsRequstObserver);
	}



	public void openEventsCalendarFragment() {
		EventsCalendarFragment fragment = EventsCalendarFragment.newInstance();
		if(isDoublePane){
			inflateFragmentInsideLayout(fragment, R.id.events_details_container, false);
		}
		else
		{
			inflateFragmentInsideLayout(fragment, R.id.events_list_container, false);
		}
	}

	//used only with calendar
	public void openEventListFragment(String startDate ,String endDate ) {
		EventsListFragment fragment = EventsListFragment.newInstance(startDate,endDate);
		inflateListFragment(fragment);
	}

	public void openEventsListFragment(ArrayList<EventItem> eventsList)
	{
		EventsListFragment fragment = EventsListFragment.newInstance(eventsList);
		inflateListFragment(fragment);
	}

	//in case of mobile save list fragment in back stack
	public void inflateListFragment(SECBBaseFragment fragment){
		inflateFragmentInsideLayout(fragment, R.id.events_list_container, !isDoublePane);
	}

	public void updateEventsList(ArrayList<EventItem> eventsList) {

		this.eventsList =eventsList;
		first3EventsList = new ArrayList<>();
		if(eventsList!=null && eventsList.size()>0)
		{
			for (int i = 0 ; i<eventsList.size() ; i++)
			{
				first3EventsList.add(eventsList.get(i));
				if(i==2)
					break;
			}

			if(Utilities.isTablet(this)&&eventsList!=null && eventsList.size()>3 && txtv_viewAllEvents!=null )
				txtv_viewAllEvents.setVisibility(View.VISIBLE);
			else
				txtv_viewAllEvents.setVisibility(View.GONE);
		}


		openEventsListFragment(first3EventsList);
	}
	public void openEventDetailsFragment(EventItem item)
	{
		if(item==null)
		return;
		currentEventsItemDetails=item;
		EventDetailsFragment fragment = EventDetailsFragment.newInstance(item);
		//in case of tablet
		if(isDoublePane)
		{
			inflateFragmentInsideLayout(fragment, R.id.events_details_container, true);
		}
		else //in case of mobile
		{
			inflateFragmentInsideLayout(fragment, R.id.events_list_container,true);

		}
	}



	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
		if (error == null)
		{
			if ((int) requestId == RequestIds.FORGET_PASSWORD_REQUEST_ID &&
					resulObject != null )
			{

			}
		}
		else if (error != null && error instanceof CTHttpError) {
			int statusCode = ((CTHttpError) error).getStatusCode();
			String errorMsg = ((CTHttpError) error).getErrorMsg();
			if (RequestHandler.isRequestTimedOut(statusCode)) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), EventsActivity.this);
			} else if (statusCode == -1) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						EventsActivity.this);
			} else {
				ErrorDialog.showMessageDialog(getString(R.string.attention), errorMsg,
						EventsActivity.this);
			}

			Logger.instance().v(TAG, error);
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}
}
