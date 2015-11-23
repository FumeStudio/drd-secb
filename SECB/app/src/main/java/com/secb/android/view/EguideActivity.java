package com.secb.android.view;

import android.os.Bundle;
import android.view.View;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EGuideLocationManager;
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
	private OrganizerItem currentOrganizerItemDetails;


	public EguideActivity() {
		super(R.layout.activity_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		isDoublePane=findViewById(R.id.details_container)!=null;
		locationsList = (ArrayList<LocationItem>) EGuideLocationManager.getInstance().getLocationsUnFilteredList(this);

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
				openLocationDetailsFragment(item);
				if(Utilities.isTablet(this))
					openLocationsListFragment();
			}
			else if(object instanceof OrganizerItem){
				OrganizerItem item=(OrganizerItem)object;
				openOrganizerDetailsFragment(item);
				if(Utilities.isTablet(this))
					openOrganizersListFragment();
			}
		}
		else
		{
			openEguideHomeFragment();

			//load details of first item
			if(isDoublePane && locationsList != null && locationsList.size() > 0) {

				openLocationDetailsFragment(locationsList.get(0));
			}
		}
	}

	private void initObservers() {
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


	public void openEguideHomeFragment() {
		if(!isDoublePane)
		{
			EguideHomeFragment fragment = EguideHomeFragment.newInstance();
			inflateFragmentInsideLayout(fragment,R.id.list_container,false);
		}
		else{
			openLocationsListFragment();
		}

	}

	public void openLocationsListFragment() {
		LocationsListFragment fragment = LocationsListFragment.newInstance();
		inflateFragmentInsideLayout(fragment,R.id.list_container,false);
	}
	public void openOrganizersListFragment() {
		OrganizersListFragment fragment = OrganizersListFragment.newInstance();
		inflateFragmentInsideLayout(fragment,R.id.list_container,false);
	}


	public void openLocationDetailsFragment(LocationItem item) {
		if (item == null)
			return;
		currentLocationItemDetails = item;
		LocationsDetailsFragment fragment = LocationsDetailsFragment.newInstance(item);
		//in case of tablet
		if (isDoublePane) {
			inflateFragmentInsideLayout(fragment, R.id.details_container, false);
		} else //in case of mobile
		{
			inflateFragmentInsideLayout(fragment, R.id.list_container, isComingFromMenu);
		}
	}
	public void openOrganizerDetailsFragment(OrganizerItem item) {
		if(item==null)
			return;
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
}
