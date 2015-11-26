package com.secb.android.view;

import android.os.Bundle;
import android.view.View;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.view.fragments.E_ServiceDetailsFragment;
import com.secb.android.view.fragments.E_ServicesListFragment;
import com.secb.android.view.fragments.NewsListFragment;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;

import java.util.ArrayList;

public class EservicesActivity extends SECBBaseActivity implements RequestObserver {
	private static final String TAG = "EservicesActivity";
//this activity for e-services list fragment , e-services details fragment


	/*isComingFromMenu = true means that it  coming from
	 * side menu ,and the intent does not contain Extras
	 * so in mobile screen we should add details
	 * fragment to back stack to save history*/
	public boolean isComingFromMenu=true;
	private  E_ServiceRequestItem currentItemDetails;
	private ArrayList<E_ServiceRequestItem> ersrviceList;


	public EservicesActivity() {
		super(R.layout.eservice_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		ersrviceList = (ArrayList<E_ServiceRequestItem>) E_ServicesManager.getInstance().getEservicesRequestsUnFilteredList(this);

		applyFonts();
		if(getIntent()!=null && getIntent().getExtras()!=null &&
				getIntent().getExtras().containsKey("item") &&
				getIntent().getExtras().get("item")!=null)
		{
			isComingFromMenu=false;
			E_ServiceRequestItem item = (E_ServiceRequestItem) getIntent().getExtras().get("item");
			openEServicesDetailsFragment(item);
		}
		else
		{
			openEServicesListFragment();
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


	public void openEServicesListFragment() {
		E_ServicesListFragment fragment = E_ServicesListFragment.newInstance();
		inflateFragmentInsideLayout(fragment,R.id.list_container,false);
	}

	public void openEServicesDetailsFragment(E_ServiceRequestItem item) {
		if(item==null)
			return;
		currentItemDetails=item;
		 E_ServiceDetailsFragment fragment = E_ServiceDetailsFragment.newInstance(item);
		//in case of tablet
		//in case of mobile
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
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), EservicesActivity.this);
			} else if (statusCode == -1) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						EservicesActivity.this);
			} else {
				ErrorDialog.showMessageDialog(getString(R.string.attention), errorMsg,
						EservicesActivity.this);
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
