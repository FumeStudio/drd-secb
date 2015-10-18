package com.secb.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.filters_layouts.NewsFilterLayout;

public class NewsDetailsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener {
    NewsItem newsItem;
    ImageView imgv_news_details_img;
    TextView txtv_news_details_newTitle;
    TextView txtv_news_details_newDate;
    TextView txtv_news_details_newBody;
    View view;

    private NewsFilterLayout newsFilterLayout = null;


    public static NewsDetailsFragment newInstance(NewsItem newsItem)
    {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("locationItem",newsItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.news_details));
        ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
        ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();
        ((SECBBaseActivity) getActivity()).enableHeaderShareButton();
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
        ((SECBBaseActivity) getActivity()).disableHeaderShareButton();
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
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.news_details, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            newsItem = (NewsItem)bundle.getSerializable("locationItem");
        }
        initViews(view);
        bindViews();
        return view;
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts()
    {
        if(txtv_news_details_newTitle!=null)
            UiEngine.applyCustomFont(txtv_news_details_newTitle, UiEngine.Fonts.HVAR);
        if(txtv_news_details_newDate!=null)
            UiEngine.applyCustomFont(txtv_news_details_newDate, UiEngine.Fonts.HVAR);
        if(txtv_news_details_newBody!=null)
            UiEngine.applyCustomFont(txtv_news_details_newDate, UiEngine.Fonts.HVAR);
    }

    private void goBack()
    {
        String backStateName = this.getClass().getName();
//     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(backStateName);
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

            default:
                break;
        }
    }

    private void initViews(View view)
    {
//        NewsItem locationItem = new NewsItem();
        imgv_news_details_img = (ImageView) view.findViewById(R.id.imgv_news_details_img);
        txtv_news_details_newTitle = (TextView) view.findViewById(R.id.txtv_news_details_newTitle);
        txtv_news_details_newDate = (TextView) view.findViewById(R.id.txtv_news_details_newDate);
        txtv_news_details_newBody = (TextView) view.findViewById(R.id.txtv_news_details_newBody);
    }

    private void bindViews()
    {
        if(this.newsItem!=null){
            txtv_news_details_newTitle.setText(newsItem.newsItemTitle);
            txtv_news_details_newDate.setText(newsItem.newsItemDate);
            txtv_news_details_newBody.setText(newsItem.newsItemDescription);
            imgv_news_details_img.setImageBitmap(newsItem.newsItemImage);
        }
    }


}
