package com.secb.android.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EGuideLocationManager;
import com.secb.android.controller.manager.EGuideOrganizersManager;
import com.secb.android.model.LocationItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.fragments.EguideHomeFragment;
import com.secb.android.view.fragments.LocationsDetailsFragment;
import com.secb.android.view.fragments.LocationsListFragment;
import com.secb.android.view.fragments.NewsListFragment;
import com.secb.android.view.fragments.OrganizersDetailsFragment;
import com.secb.android.view.fragments.OrganizersListFragment;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class EguideActivity extends SECBBaseActivity implements RequestObserver {
	private static final String TAG = "EguideActivity";
//this activity for e-services list fragment , e-services details fragment

	public boolean isDoublePane=false;

	/*isComingFromMenu = true means that it  coming from
	 * side menu ,and the intent does not contain Extras
	 * so in mobile screen we should add details
	 * fragment to back stack to save history*/
	public boolean isComingFromMenu=true;
	private LocationItem currentLocationItemDetails;
	private ArrayList<LocationItem> locationsList;
	private ArrayList<OrganizerItem> organizersList;
	private OrganizerItem currentOrganizerItemDetails;
	TextView txtv_location_tab,txtv_organizer_tab;

	int indexOfCurrentOrganizer;
	int indexOfCurrentLocation;

	boolean isLocationTabActive=true;
	public EguideActivity() {
		super(R.layout.eguide_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		isDoublePane=findViewById(R.id.details_container)!=null;
		locationsList = (ArrayList<LocationItem>) EGuideLocationManager.getInstance().getLocationsUnFilteredList(this);
		organizersList = (ArrayList<OrganizerItem>) EGuideOrganizersManager.getInstance().getOrganizersUnFilteredList(this);

		applyFonts();
		if(getIntent()!=null && getIntent().getExtras()!=null &&
				getIntent().getExtras().containsKey("item") &&
				getIntent().getExtras().get("item")!=null)
		{
			isComingFromMenu=false;
			Object object = getIntent().getExtras().get("item");
			if(object instanceof LocationItem)
			{
				LocationItem item = (LocationItem) object;
				openLocationDetailsFragment(item, -1);
				if(Utilities.isTablet(this))
					openLocationsListFragment();
			}
			else if(object instanceof OrganizerItem){
				OrganizerItem item=(OrganizerItem)object;
				openOrganizerDetailsFragment(item,-1);
				if(Utilities.isTablet(this))
					openOrganizersListFragment();
			}
		}
		else
		{
			openEguideHomeFragment();

			//load details of first item
			if(isDoublePane && locationsList != null && locationsList.size() > 0) {

				openLocationDetailsFragment(locationsList.get(0), 0);
			}
		}
	}

	private void initObservers() {
	}

	private void initViews() {
		txtv_location_tab = (TextView) findViewById(R.id.txtv_location_tab);
		txtv_organizer_tab = (TextView) findViewById(R.id.txtv_organizer_tab);

		if(txtv_location_tab!=null)
			txtv_location_tab.setOnClickListener(this);
		if(txtv_organizer_tab!=null)
			txtv_organizer_tab.setOnClickListener(this);
	}

	private void applyFonts() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId())
		{
			case R.id.txtv_location_tab:
				openLocations();
				break;
			case R.id.txtv_organizer_tab:
				openOrganizer();
				break;
			default:
				break;
		}
	}

	private void openLocations() {
		handleTabsBg(true);
		//open list
		openLocationsListFragment();
		//open details of first (or last opened )Item
		if(locationsList!=null&&locationsList.size()>0)
		{
			if(locationsList.size()>indexOfCurrentLocation)
				openLocationDetailsFragment(locationsList.get(indexOfCurrentLocation), indexOfCurrentLocation);
			else
				openLocationDetailsFragment(locationsList.get(0), 0);
		}


	}

	private void openOrganizer() {
		handleTabsBg(false);
		openOrganizersListFragment();
		//open details of first Item
		if(organizersList!=null&&organizersList.size()>0)
		{
			if(organizersList.size()>indexOfCurrentOrganizer)
				openOrganizerDetailsFragment(organizersList.get(indexOfCurrentOrganizer),indexOfCurrentOrganizer);
			else
				openOrganizerDetailsFragment(organizersList.get(0),0);
		}
	}

	private void handleTabsBg(boolean isLocationActive)
	{
		isLocationTabActive=isLocationActive;
		if(isLocationActive)
		{
			txtv_location_tab.setBackgroundResource(R.drawable.eguid_tab_active);
			txtv_organizer_tab.setBackgroundResource(R.drawable.eguid_tab_deactive);
		}
		else
		{
			txtv_location_tab.setBackgroundResource(R.drawable.eguid_tab_deactive);
			txtv_organizer_tab.setBackgroundResource(R.drawable.eguid_tab_active);
		}
	}


	public void openEguideHomeFragment() {
		if(!isDoublePane)
		{
			EguideHomeFragment fragment = EguideHomeFragment.newInstance();
			inflateFragmentInsideLayout(fragment, R.id.list_container, false);
		}
		else{
			openLocationsListFragment();
		}

	}

	public void openLocationsListFragment() {
		LocationsListFragment fragment = LocationsListFragment.newInstance();
		inflateFragmentInsideLayout(fragment,R.id.list_container,true);
	}
	public void openOrganizersListFragment() {
		OrganizersListFragment fragment = OrganizersListFragment.newInstance();
		inflateFragmentInsideLayout(fragment,R.id.list_container,true);
	}


	public void openLocationDetailsFragment(LocationItem item, int position)
	{
		if (item == null)
			return;
		currentLocationItemDetails = item;
		// -1 means it's not coming from list
		if(position>-1)
			indexOfCurrentLocation=position;

		LocationsDetailsFragment fragment = LocationsDetailsFragment.newInstance(item);
		//in case of tablet
		if (isDoublePane) {
			inflateFragmentInsideLayout(fragment, R.id.details_container, false);
		} else //in case of mobile
		{
			inflateFragmentInsideLayout(fragment, R.id.list_container, isComingFromMenu);
		}
	}
	public void openOrganizerDetailsFragment(OrganizerItem item,int position) {
		if(item==null)
			return;
		if(position>-1)
			indexOfCurrentOrganizer=position;

		currentOrganizerItemDetails =item;
		 OrganizersDetailsFragment fragment = OrganizersDetailsFragment.newInstance(item);
		//in case of tablet
		if(isDoublePane)
		{
			inflateFragmentInsideLayout(fragment, R.id.details_container,false);
		}
		else //in case of mobile
		{
			inflateFragmentInsideLayout(fragment, R.id.list_container,isComingFromMenu);
		}

	}

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
		if (error == null) {
			if ((int) requestId == RequestIds.NEWS_LIST_REQUEST_ID &&
					resulObject != null)
			{

			}
		} else if (error != null && error instanceof CTHttpError) {
			int statusCode = ((CTHttpError) error).getStatusCode();
			String errorMsg = ((CTHttpError) error).getErrorMsg();
			if (RequestHandler.isRequestTimedOut(statusCode)) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), EguideActivity.this);
			} else if (statusCode == -1) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						EguideActivity.this);
			} else {
				ErrorDialog.showMessageDialog(getString(R.string.attention), errorMsg,
						EguideActivity.this);
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

	//if the list wasn't cached when opening ListFragment
	//load the list and if is Tablet open the details of first item
	public void locationListReady()
	{
		if(!Utilities.isTablet(this))
			return;
		if(isLocationTabActive)
		{
			locationsList = (ArrayList<LocationItem>) EGuideLocationManager.getInstance().getLocationsUnFilteredList(this);
			if(locationsList!=null && locationsList.size()>indexOfCurrentLocation
					&& locationsList.get(indexOfCurrentLocation)!=null)
			{
				openLocationDetailsFragment(locationsList.get(indexOfCurrentLocation), indexOfCurrentLocation);
			}
		}
	}

	//if the list wasn't cached when opening ListFragment
	//load the list and if is Tablet open the details of first item
	public void organizersListReady()
	{
		if(!Utilities.isTablet(this))
			return;
		if(!isLocationTabActive)
		{
			organizersList = (ArrayList<OrganizerItem>) EGuideOrganizersManager.getInstance().getOrganizersUnFilteredList(this);
			if(organizersList!=null &&
					organizersList.size()>indexOfCurrentOrganizer&&
					organizersList.get(indexOfCurrentOrganizer)!=null)
			{
				openOrganizerDetailsFragment(organizersList.get(indexOfCurrentOrganizer),indexOfCurrentOrganizer);
			}
		}
	}

}
