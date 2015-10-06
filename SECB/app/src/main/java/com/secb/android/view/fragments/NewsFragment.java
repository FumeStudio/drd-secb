package com.secb.android.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.DividerItemDecoration;
import com.secb.android.view.components.filters_layouts.NewsFilterLayout;
import com.secb.android.view.components.news_recycler.NewsItemRecyclerAdapter;

import java.util.ArrayList;

public class NewsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener {
    RecyclerView newsRecyclerView;
    NewsItemRecyclerAdapter newsItemRecyclerAdapter;
    ArrayList<NewsItem> newsList;
    NewsFilterData newsFilterData;

    View view;
    private NewsFilterLayout newsFilterLayout=null;


    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.news));
        ((SECBBaseActivity) getActivity()).showFilterButton(true);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
//        ((SECBBaseActivity) getActivity()).setFilterIconClickListener(this);
//        SECBBaseActivity.setMenuItemSelected(MenuItem.MENU_HOME);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        initViews(view);
        initFilterLayout();
        return view;
    }

    public void initFilterLayout()
    {
        newsFilterLayout= new NewsFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(newsFilterLayout);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(newsFilterLayout.getLayoutView());
    }
    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
        // TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
    }

    private void goBack() {
        ((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
    }

    // ////////////////////////////////////////////////////////////

    @Override
    public void onBack() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtv_viewAllNews:
                ((MainActivity) getActivity()).startNewsActivity();
           /* case R.id.imgv_filter:
                ((SECBBaseActivity) getActivity()).displayToast("NewsFragment + filterIconClicked");
                */
            case R.id.btn_applyFilter:
                getFilterDataObject();
            default:
                break;
        }
    }

    private void getFilterDataObject() {
        newsFilterData =this.newsFilterLayout.getFilterData();
        if(newsFilterData !=null){
            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n Time From: "+ newsFilterData.timeFrom+"\n" +
                    " Time To: "+ newsFilterData.timeTo+" \n" +
                    " Type: "+ newsFilterData.type);
        }
    }


    private void initViews(View view) {
        newsList = new ArrayList<>();
        NewsItem newsItem = new NewsItem();

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsItemRecyclerView);
        newsItemRecyclerAdapter = new NewsItemRecyclerAdapter(getActivity(), newsList);
        newsRecyclerView.setAdapter(newsItemRecyclerAdapter);
        newsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }


}
