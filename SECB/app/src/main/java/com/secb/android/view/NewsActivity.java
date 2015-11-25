package com.secb.android.view;

import android.os.Bundle;
import android.view.View;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.model.NewsItem;
import com.secb.android.view.fragments.NewsDetailsFragment;
import com.secb.android.view.fragments.NewsListFragment;

import net.comptoirs.android.common.controller.backend.CTHttpError;
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
	public NewsItem currentNewsItemDetails;

	public boolean isDoublePane=false;

	/*isComingFromMenu = true means that it  coming from
	 * side menu ,and the intent does not contain Extras
	 * so in mobile screen we should add details
	 * fragment to back stack to save history*/
	public boolean isComingFromMenu=true;

	private ArrayList<NewsItem> newsList;

	public NewsActivity() {
		super(R.layout.news_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		isDoublePane=findViewById(R.id.news_details_container)!=null;
		newsList = (ArrayList<NewsItem>) NewsManager.getInstance().getNewsUnFilteredList(this);

		applyFonts();
		if(getIntent()!=null && getIntent().getExtras()!=null &&
				getIntent().getExtras().containsKey("item") &&
				getIntent().getExtras().get("item")!=null)
		{
			isComingFromMenu=false;
			NewsItem item = (NewsItem) getIntent().getExtras().get("item");
			openNewDetailsFragment(item);
			if(Utilities.isTablet(this))
				openNewsListFragment();
		}
		else
		{
			openNewsListFragment();

			//load details of first item
			if(isDoublePane && newsList != null && newsList.size() > 0)
			{

				openNewDetailsFragment(newsList.get(0));
			}
		}
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
	}

	public void openNewDetailsFragment(NewsItem newsItem) {
		if (newsItem == null)
			return;
		currentNewsItemDetails = newsItem;
		NewsDetailsFragment newDetailsFragment = NewsDetailsFragment.newInstance(newsItem);
		// in case of tablet
		if (isDoublePane) {
			inflateFragmentInsideLayout(newDetailsFragment, R.id.news_details_container, false);
		} else //in case of mobile
		{
			inflateFragmentInsideLayout(newDetailsFragment, R.id.news_list_container, isComingFromMenu);
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
