package com.secb.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.model.EventItem;
import com.secb.android.view.fragments.EventDetailsFragment;
import com.secb.android.view.fragments.EventsCalendarFragment;
import com.secb.android.view.fragments.EventsListFragment;
import com.secb.android.view.fragments.NewsListFragment;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;

import java.util.ArrayList;

public class EventsActivity extends SECBBaseActivity implements RequestObserver {
	private static final String TAG = "EventsActivity";
//this activity for events calendar , events list fragment , events details fragment

	ArrayList<RequestObserver> newsRequstObserverList;

	public EventsActivity() {
		super(R.layout.events_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		applyFonts();
		openNewsListFragment();
	}

	private void initObservers() {
		newsRequstObserverList = new ArrayList<>();
	}

	private void initViews() {

	}

	private void applyFonts() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			default:
				break;
		}
	}

	//news
	public void setNewsRequstObserver(RequestObserver newsRequstObserver) {
		newsRequstObserverList.add(newsRequstObserver);
	}


	public void openEventsCalendarFragment() {
		EventsCalendarFragment eventsCalendarFragment = EventsCalendarFragment.newInstance();
		addFragment(eventsCalendarFragment, eventsCalendarFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
	}


	public void openNewsListFragment() {
		EventsListFragment fragment = EventsListFragment.newInstance();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.events_list_container, fragment);
		transaction.commit();

//		addFragment(newsListFragment, newsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

	}

	public void openNewDetailsFragment(EventItem item)
	{
		EventDetailsFragment fragment = EventDetailsFragment.newInstance(item);
		//in case of tablet
		if(findViewById(R.id.events_details_container)!=null)
		{
			inflateFragmentInsideLayout(fragment, R.id.events_details_container, false);


		/*	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.events_details_container, fragment);
			transaction.commit();*/
		}
		else //in case of mobile
		{
			inflateFragmentInsideLayout(fragment, R.id.events_list_container,false);

/*			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.events_list_container, fragment);
			transaction.addToBackStack(fragment.getClass().getName());
			transaction.commit();*/

		}
	}



	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
		if (error == null) {
			if ((int) requestId == RequestIds.FORGET_PASSWORD_REQUEST_ID &&
					resulObject != null &&
					((CTHttpResponse) resulObject).statusCode == 200) {

			}
		} else if (error != null && error instanceof CTHttpError) {
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

	public void setNewsRequstObserver(NewsListFragment newsListFragment) {

	}
}
