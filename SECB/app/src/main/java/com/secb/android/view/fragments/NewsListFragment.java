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
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
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

import java.util.ArrayList;

public class NewsListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener ,RecyclerCustomClickListener, RequestObserver

{
	private static final String TAG = "NewsListFragment";
	RecyclerView newsRecyclerView;
    NewsItemRecyclerAdapter newsItemRecyclerAdapter;
    ArrayList<NewsItem> newsList;
    NewsFilterData newsFilterData;

    View view;
	TextView txtv_noData;
    private NewsFilterLayout newsFilterLayout=null;
	private ProgressDialog progressDialog;

    public static NewsListFragment newInstance() {
        NewsListFragment fragment = new NewsListFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.news));
        ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
        ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();
        ((SECBBaseActivity) getActivity()).showFilterButton(true);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null)
            {
                ((ViewGroup) oldParent).removeView(view);
            }
        }
        else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.news_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
	    ((MainActivity)getActivity()).setNewsRequstObserver(this);
        initViews(view);
        initFilterLayout();
	    getData();
        return view;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		newsRecyclerView.setAdapter(null);
	}

    public void initFilterLayout()
    {
        newsFilterLayout= new NewsFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(newsFilterLayout ,true);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(newsFilterLayout.getLayoutView());
    }
    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {

	    if(txtv_noData!=null)
	    {
		    UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
	    }
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
    }

    private void goBack() {
        ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(getClass().getName());
    }

    // ////////////////////////////////////////////////////////////

    @Override
    public void onBack() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageViewBackHeader:
                onBack();
                break;
            case R.id.btn_applyFilter:
                applyFilters();
                break;
            default:
                break;
        }
    }

    private void applyFilters()
    {
        newsFilterData =this.newsFilterLayout.getFilterData();
	    ((SECBBaseActivity)getActivity()).hideFilterLayout();
        if(newsFilterData !=null){
          /*  ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n Time From: "+ newsFilterData.timeFrom+"\n" +
                    " Time To: "+ newsFilterData.timeTo+" \n" +
                    " Type: "+ newsFilterData.selectedCategoryId+
                    " Selected Category: "+ newsFilterData.newsCategory);*/
	        startNewsListOperation(newsFilterData,true);
        }


    }


    private void initViews(View view)
    {
//        newsList = DevData.getNewsList();
	    progressDialog = CustomProgressDialog.getInstance(getActivity(),true);
	    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		    @Override
		    public void onCancel(DialogInterface dialog) {
			    bindViews();
		    }
	    });
        newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
	    txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
//        newsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), newsRecyclerView, this));
    }

	public void bindViews()
	{
		if(newsList!=null && newsList.size()>0)
		{
			newsRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			newsItemRecyclerAdapter = new NewsItemRecyclerAdapter(getActivity(), newsList);
			newsRecyclerView.setAdapter(newsItemRecyclerAdapter);
		}
		else {
			newsRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			txtv_noData.setText(getString(R.string.news_no_news));
		}
	}

	public void getData()
	{
		//if news list is loaded in the manager get it and bind
		//if not and the main activity is still loading the news list
			// wait for it and it will notify handleRequestFinished in this fragment.
		//if the main activity finished loading news list and the manager is still empty
			//start operation here.


		newsList = (ArrayList<NewsItem>) NewsManager.getInstance().getNewsUnFilteredList(getActivity());
		if(newsList!= null && newsList.size()>0){
			handleRequestFinished(RequestIds.NEWS_LIST_REQUEST_ID, null, newsList);
		}
		else {
			if (((MainActivity) getActivity()).isNewsLoadingFinished == false) {
				startWaiting();
			}
			else{
				startNewsListOperation(new NewsFilterData(),true);
			}
		}

	}


	private void startWaiting() {
		if(progressDialog!=null&& !progressDialog.isShowing())
		{
			progressDialog.show();
		}
	}

	private void stopWaiting() {
		if(progressDialog!=null&& progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
	}
	private void startNewsListOperation(NewsFilterData newsFilterData ,boolean showDialog)
	{
		NewsListOperation operation = new NewsListOperation(RequestIds.NEWS_LIST_REQUEST_ID,showDialog,getActivity(),newsFilterData,100,0);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	@Override
	public void onItemClicked(View v, int position)
	{
		((MainActivity) getActivity()).openNewDetailsFragment(newsList.get(position));
	}

	@Override
    public void onItemLongClicked(View v, int position)
    {

    }


	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject)
	{
		stopWaiting();
		if (error == null)
		{
			Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
			if((int)requestId == RequestIds.NEWS_LIST_REQUEST_ID && resultObject!=null){
				newsList= (ArrayList<NewsItem>) resultObject;
				bindViews();
			}

		}
		else if (error != null && error instanceof CTHttpError)
		{
			Logger.instance().v(TAG,error);
			int statusCode = ((CTHttpError) error).getStatusCode();
			if (RequestHandler.isRequestTimedOut(statusCode))
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
			}
			else if (statusCode == -1)
			{
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
