package com.secb.android.view.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addthis.core.AddThis;
import com.addthis.models.ATShareItem;
import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.controller.backend.NewsDetailsOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.NewsActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.filters_layouts.NewsFilterLayout;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class NewsDetailsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener, RequestObserver {

    private static final String TAG = "NewsDetailsFragment";
    NewsItem newsItem;
    ImageView imgv_news_details_img;
    TextView txtv_news_details_newTitle;
    TextView txtv_news_details_newDate;
    TextView txtv_news_details_newBody;
    View view;
    RelativeLayout layout_detailsContainer;
    TextView txtv_noData;
	ImageView imageViewShareHeader;
    private NewsFilterLayout newsFilterLayout = null;
    private ArrayList<NewsItem> newsList;


    public static NewsDetailsFragment newInstance(NewsItem newsItem) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("newsItem", newsItem);
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

	    ((SECBBaseActivity) getActivity()).showFilterButton(Utilities.isTablet(getActivity()));

	    if( Utilities.isTablet(getActivity()) && ( (NewsActivity) getActivity()).isDoublePane)
	    {
		    ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.news));
		    ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
		    ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
	    }
	    else{
		    ((SECBBaseActivity) getActivity()).enableHeaderShareButton(newsItem);
	    }

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
        if (bundle != null) {
            newsItem = (NewsItem) bundle.getSerializable("newsItem");
        }
        initViews(view);
        getData();
        return view;
    }

    private void getData() {
        NewsItem item = NewsManager.getInstance().getNewDetails(this.newsItem.ID, getActivity());
        if (item != null) {
            newsList = new ArrayList<>();
            newsList.add(item);
            handleRequestFinished(RequestIds.NEWS_DETAILS_REQUEST_ID, null, newsList);
        } else {
            NewsFilterData newsFilterData = new NewsFilterData();
            newsFilterData.newsID = this.newsItem.ID;
            newsFilterData.newsCategory = "All";
            NewsDetailsOperation operation = new NewsDetailsOperation(RequestIds.NEWS_DETAILS_REQUEST_ID, true, getActivity(), newsFilterData, 100, 0);
            operation.addRequsetObserver(this);
            operation.execute();
        }
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {

        UiEngine.applyFontsForAll(getActivity(), view, UiEngine.Fonts.HVAR);
        if (txtv_news_details_newTitle != null)
            UiEngine.applyCustomFont(txtv_news_details_newTitle, UiEngine.Fonts.HVAR);
        if (txtv_news_details_newDate != null)
            UiEngine.applyCustomFont(txtv_news_details_newDate, UiEngine.Fonts.HVAR);
        if (txtv_news_details_newBody != null)
            UiEngine.applyCustomFont(txtv_news_details_newBody, UiEngine.Fonts.HVAR);
        if (txtv_noData != null) {
            UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
        }
    }

    private void goBack() {
        String backStateName = this.getClass().getName();
//     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity();


	    /*((SECBBaseActivity) getActivity()).finishFragmentOrActivity(backStateName);*/
	    if(((NewsActivity)getActivity()).isComingFromMenu  &&!Utilities.isTablet(getActivity()))
		    ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(backStateName);
		else
	        (getActivity()).finish();
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

            case R.id.imageViewShareHeader:
	            onShareClicked();
                break;

            default:
                break;
        }
    }

	private void onShareClicked() {
		if(newsItem==null)return;
		((SECBBaseActivity)getActivity()).initSharingConfigs();
		ATShareItem item = new ATShareItem("http://secb.gov.sa",newsItem.getSharingTitle(), newsItem.getSharingDesc());
		AddThis.presentAddThisMenu(getActivity(), item);
	}

	private void initViews(View view) {
//        NewsItem locationItem = new NewsItem();
        imgv_news_details_img = (ImageView) view.findViewById(R.id.imgv_news_details_img);
        txtv_news_details_newTitle = (TextView) view.findViewById(R.id.txtv_news_details_newTitle);
        txtv_news_details_newDate = (TextView) view.findViewById(R.id.txtv_news_details_newDate);
        txtv_news_details_newBody = (TextView) view.findViewById(R.id.txtv_news_details_newBody);
        txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
        layout_detailsContainer = (RelativeLayout) view.findViewById(R.id.layout_detailsContainer);

	    imageViewShareHeader = (ImageView) view.findViewById(R.id.imageViewShareHeader);
	    if(imageViewShareHeader!=null){
		    imageViewShareHeader.setOnClickListener(this);
	    }
    }

    private void bindViews() {
        if (this.newsItem != null) {
            ((SECBBaseActivity) getActivity()).enableHeaderShareButton(newsItem);
            layout_detailsContainer.setVisibility(View.VISIBLE);
            txtv_noData.setVisibility(View.GONE);

            txtv_news_details_newTitle.setText(newsItem.Title);

//	        String new_day = MainActivity.reFormatNewsDate(newsItem.CreationDate,MainActivity.sdf_Time);
	        Date date = null; String new_day="";
	        try {
		        date = MainActivity.sdf_Source_News.parse(newsItem.CreationDate);
	        } catch (ParseException e) {
		        e.printStackTrace();
	        }
	        if(date!=null){
		        new_day= MainActivity.sdf_day_MONTH_year.format(date);
	        }

	        txtv_news_details_newDate.setText(new_day);
            String decodedBody = Uri.decode(newsItem.NewsBody);
            txtv_news_details_newBody.setText(!Utilities.isNullString(decodedBody) ? Html.fromHtml(decodedBody) : "");

            if (!Utilities.isNullString(newsItem.ImageUrl)) {
//		        Picasso.with(getActivity())
//				        .load(newsItem.ImageUrl)
//				        .placeholder(R.drawable.news_image_place_holder)
//				        .into(imgv_news_details_img);
                Glide.with(getActivity())
                        .load(newsItem.ImageUrl)
                        .placeholder(R.drawable.news_placeholder)
                        .centerCrop()
                        .into(imgv_news_details_img);
            } else
                imgv_news_details_img.setImageResource(R.drawable.news_placeholder);
        } else {
            layout_detailsContainer.setVisibility(View.GONE);
            txtv_noData.setText(getResources().getString(R.string.details_no_details));
            txtv_noData.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
        if (error == null) {
            Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
            if ((int) requestId == RequestIds.NEWS_DETAILS_REQUEST_ID && resultObject != null) {
                newsList = (ArrayList<NewsItem>) resultObject;
                if (newsList != null && newsList.size() > 0) {
                    newsItem = newsList.get(0);
                    bindViews();
                }
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
