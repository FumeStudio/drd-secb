package com.secb.android.view;

import android.os.Bundle;
import android.view.View;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.model.NewsItem;
import com.secb.android.view.fragments.NewsDetailsFragment;
import com.secb.android.view.fragments.NewsListFragment;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class NewsActivity extends SECBBaseActivity implements RequestObserver {
	private static final String TAG = "NewsActivity";
//this activity for news list fragment , news details fragment

	ArrayList<RequestObserver> newsRequstObserverList;

	NewsItem item;

	/*isComingFromMenu = true means that it's not coming from
	 * side menu ,and the intent does not contain Extras
	 * so in mobile screen we should add details
	 * fragment to back stack to save history*/
	public boolean isComingFromMenu=true;

	public NewsActivity() {
		super(R.layout.news_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		applyFonts();
		if(getIntent()!=null && getIntent().getExtras()!=null &&
				getIntent().getExtras().containsKey("item") &&
				getIntent().getExtras().get("item")!=null)
		{
			isComingFromMenu=false;
			item = (NewsItem) getIntent().getExtras().get("item");
			openNewDetailsFragment(item);
			if(Utilities.isTablet(this))
				openNewsListFragment();
		}
		else
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


	public void openNewsListFragment() {
		NewsListFragment newsListFragment = NewsListFragment.newInstance();
		inflateFragmentInsideLayout(newsListFragment,R.id.news_list_container,false);


/*		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.news_list_container, newsListFragment);
		transaction.commit();*/

//		addFragment(newsListFragment, newsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

	}

	public void openNewDetailsFragment(NewsItem newsItem) {
		NewsDetailsFragment newDetailsFragment = NewsDetailsFragment.newInstance(newsItem);
		//in case of tablet
		if(findViewById(R.id.news_details_container)!=null)
		{
			inflateFragmentInsideLayout(newDetailsFragment, R.id.news_details_container,false);

	/*		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.news_details_container, newDetailsFragment);
			transaction.commit();*/
		}
		else //in case of mobile
		{
			inflateFragmentInsideLayout(newDetailsFragment, R.id.news_list_container,isComingFromMenu);

			/*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.news_list_container, newDetailsFragment);
			transaction.addToBackStack( newDetailsFragment.getClass().getName());
			transaction.commit();*/

//			addFragment(newDetailsFragment, newDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
		}
//		addFragment(newDetailsFragment, newDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

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
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), NewsActivity.this);
			} else if (statusCode == -1) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						NewsActivity.this);
			} else {
				ErrorDialog.showMessageDialog(getString(R.string.attention), errorMsg,
						NewsActivity.this);
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
