package com.secb.android.view.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.NewsListOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.backend.ServerKeys;
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.controller.manager.PagingManager;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.NewsActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.RecyclerViewScrollListener;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
import com.secb.android.view.components.filters_layouts.NewsFilterLayout;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;
import com.secb.android.view.components.recycler_news.NewsItemRecyclerAdapter;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class NewsListFragment extends SECBBaseFragment
		implements FragmentBackObserver, View.OnClickListener, RecyclerCustomClickListener, RequestObserver

{
	private static final String TAG = "NewsListFragment";
	RecyclerView newsRecyclerView;
	NewsItemRecyclerAdapter newsItemRecyclerAdapter;
	ArrayList<NewsItem> newsList;
	NewsFilterData newsFilterData;

	View view;
	TextView txtv_noData;
	private NewsFilterLayout newsFilterLayout = null;
	private ProgressDialog progressDialog;

	public static NewsListFragment newInstance() {
		NewsListFragment fragment = new NewsListFragment();
		return fragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		((SECBBaseActivity) getActivity()).addBackObserver(this);
		((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.news));
		((SECBBaseActivity) getActivity()).disableHeaderBackButton();
		((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
		((SECBBaseActivity) getActivity()).showFilterButton(true);
		((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
		((SECBBaseActivity) getActivity()).setClearFilterClickListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		((SECBBaseActivity) getActivity()).removeBackObserver(this);
		((SECBBaseActivity) getActivity()).showFilterButton(Utilities.isTablet(getActivity()));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
			ViewParent oldParent = (ViewParent) view.getRootView();
			if (oldParent != container && oldParent != null) {
				((ViewGroup) oldParent).removeView(view);
			}
		}
		else
		{
			view = LayoutInflater.from(getActivity()).inflate(R.layout.news_list_fragment, container, false);

			handleButtonsEvents();
			applyFonts();
		}
//		((MainActivity) getActivity()).setNewsRequstObserver(this);
		//for tablet
		((NewsActivity) getActivity()).setNewsRequstObserver(this);
		initViews(view);
		initFilterLayout();
		getData();
		return view;
	}

	public void onCreateForTablet(){
/*		NewsListFragment newFragment = new NewsListFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.list_frag, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();*/


	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		//		newsRecyclerView.setAdapter(null);
//		((SECBBaseActivity) getActivity()).hideFilterLayout();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		newsRecyclerView.setAdapter(null);
		((SECBBaseActivity) getActivity()).hideFilterLayout();
	}

	public void initFilterLayout() {
		newsFilterLayout = new NewsFilterLayout(getActivity());
		((SECBBaseActivity) getActivity()).setFilterLayout(newsFilterLayout, true);
		((SECBBaseActivity) getActivity()).setFilterLayoutView(newsFilterLayout.getLayoutView());
	}

	private void handleButtonsEvents() {
	}

	/*
	 * Apply Fonts
	 */
	private void applyFonts() {

		if (txtv_noData != null) {
			UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
		}
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
	}

	private void goBack() {
		if(((SECBBaseActivity)getActivity()).isFilterLayoutOpened)
		{
			((SECBBaseActivity)getActivity()).hideFilterLayout();
			return;
		}
		(getActivity()).finish();
//		((SECBBaseActivity) getActivity()).finishFragmentOrActivity(getClass().getName(), true);
	}

	// ////////////////////////////////////////////////////////////

	@Override
	public void onBack() {
		goBack();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imageViewBackHeader:
				onBack();
				break;
			case R.id.btn_applyFilter:
				applyFilters();
				break;
			case R.id.btn_clearFilter:
				clearFilters();
				break;
			default:
				break;
		}
	}


	private void applyFilters() {
		newsFilterData = this.newsFilterLayout.getFilterData();
		((SECBBaseActivity) getActivity()).hideFilterLayout();
		if (newsFilterData != null) {
	      /*  ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n Time From: "+ newsFilterData.timeFrom+"\n" +
                    " Time To: "+ newsFilterData.timeTo+" \n" +
                    " Type: "+ newsFilterData.selectedCategoryId+
                    " Selected Category: "+ newsFilterData.newsCategory);*/
			startNewsListOperation(newsFilterData, true, 0);
		}


	}

	private void clearFilters() {
		this.newsFilterLayout.clearFilters();

	}

	private void initViews(View view) {
		if(view==null)
			return;
		progressDialog = CustomProgressDialog.getInstance(getActivity(), true);
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				bindViews();
			}
		});
		newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
		txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
//        newsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		newsRecyclerView.setLayoutManager(linearLayoutManager);
		newsRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), newsRecyclerView, this));

		newsRecyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
			@Override
			public void onScrollUp() {

			}

			@Override
			public void onScrollDown() {

			}

			@Override
			public void onLoadMore() {
				loadMoreData();
			}
		});
	}

	private void loadMoreData() {

		newsItemRecyclerAdapter.showLoading(true);
		newsItemRecyclerAdapter.notifyDataSetChanged();

		startNewsListOperation(newsFilterData != null ? newsFilterData : new NewsFilterData(), false, (PagingManager.getLastPageNumber(newsList) + 1));
	}

	public void bindViews() {
		if (newsList != null && newsList.size() > 0) {

			//if the current screen is tablet , display the first item by default
			if(Utilities.isTablet(getActivity())){
				((NewsActivity)getActivity()).currentNewsItemDetails=(newsList.get(0));
			}

			newsRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			if(newsItemRecyclerAdapter == null) {
				newsItemRecyclerAdapter = new NewsItemRecyclerAdapter(getActivity(), newsList);
				newsItemRecyclerAdapter.setItemsList(newsList);
				newsRecyclerView.setAdapter(newsItemRecyclerAdapter);
			} else {
				newsItemRecyclerAdapter.setItemsList(newsList);
//                lastFirstVisiblePosition = Utilities.getScrollYOfRecycler(organizerRecyclerView);
				newsItemRecyclerAdapter.showLoading(false);
				newsItemRecyclerAdapter.notifyItemRangeChanged(0, newsList.size());
			}
		} else {
			newsRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			txtv_noData.setText(getString(R.string.news_no_news));
		}
	}

	public void getData() {
		//if news list is loaded in the manager get it and bind
		//if not and the main activity is still loading the news list
		// wait for it and it will notify handleRequestFinished in this fragment.
		//if the main activity finished loading news list and the manager is still empty
		//start operation here.


		newsList = (ArrayList<NewsItem>) NewsManager.getInstance().getNewsUnFilteredList(getActivity());
		if (newsList != null && newsList.size() > 0)
		{
			handleRequestFinished(RequestIds.NEWS_LIST_REQUEST_ID, null, newsList);
		}
		else
		{
			/*if (((MainActivity) getActivity()).isNewsLoadingFinished == false)
			{
				startWaiting();
			}
			else*/
			{
				startNewsListOperation(new NewsFilterData(), true, 0);
			}
		}

	}


	private void startWaiting() {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	private void stopWaiting() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	private void startNewsListOperation(NewsFilterData newsFilterData, boolean showDialog, int pageIndex) {
		NewsListOperation operation = new NewsListOperation(RequestIds.NEWS_LIST_REQUEST_ID, showDialog, getActivity(), newsFilterData, ServerKeys.PAGE_SIZE_DEFAULT, pageIndex);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	@Override
	public void onItemClicked(View v, int position) {
//		((MainActivity) getActivity()).openNewDetailsFragment(newsList.get(position));
		//for tablet
//		UiEngine.setListItemSelected(v);

		newsItemRecyclerAdapter.setItemSelected(v,position);

		((NewsActivity) getActivity()).openNewDetailsFragment(newsList.get(position));
	}

	@Override
	public void onItemLongClicked(View v, int position) {

	}


	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if(getActivity() == null)
			return;
		stopWaiting();
		if(newsItemRecyclerAdapter != null) {
            newsItemRecyclerAdapter.showLoading(false);
            newsItemRecyclerAdapter.notifyDataSetChanged();
        }
		if (error == null) {
			Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
			if ((int) requestId == RequestIds.NEWS_LIST_REQUEST_ID && resultObject != null) {
				ArrayList<NewsItem> _newsList = (ArrayList<NewsItem>) resultObject;
				int pageIndex = PagingManager.getLastPageNumber(_newsList);
				if (newsList == null || pageIndex == 0)
					newsList = new ArrayList<>();
				newsList.addAll(_newsList);
				bindViews();
			}

		} else if (error != null && error instanceof CTHttpError) {
			Logger.instance().v(TAG, error);
			int statusCode = ((CTHttpError) error).getStatusCode();
			if (RequestHandler.isRequestTimedOut(statusCode)) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
			} else if (statusCode == -1) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						getActivity());
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
