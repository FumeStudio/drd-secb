package com.secb.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

public class NewsActivity extends SECBBaseActivity implements RequestObserver {


	private static final String TAG = "NewsActivity";
//this activity for news list fragment , news details fragment

	public NewsActivity() {
		super(-1, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initViews();
		applyFonts();
		openNewsListFragment();
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

	public void openNewsListFragment() {
		NewsListFragment newsListFragment = NewsListFragment.newInstance();
		addFragment(newsListFragment, newsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

	}

	public void openNewDetailsFragment(NewsItem newsItem) {
		NewsDetailsFragment newDetailsFragment = NewsDetailsFragment.newInstance(newsItem);
		addFragment(newDetailsFragment, newDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

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
