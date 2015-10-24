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

import com.secb.android.R;
import com.secb.android.controller.backend.NewsDetailsOperation;
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.filters_layouts.NewsFilterLayout;
import com.squareup.picasso.Picasso;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class NewsDetailsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener, RequestObserver {
	private static final int NEWS_DETAILS_REQUEST_ID = 1;
	private static final String TAG = "NewsDetailsFragment";
	NewsItem newsItem;
    ImageView imgv_news_details_img;
    TextView txtv_news_details_newTitle;
    TextView txtv_news_details_newDate;
    TextView txtv_news_details_newBody;
    View view;
	RelativeLayout layout_detailsContainer;
	TextView txtv_noData;

    private NewsFilterLayout newsFilterLayout = null;
	private ArrayList<NewsItem> newsList;


	public static NewsDetailsFragment newInstance(NewsItem newsItem)
    {
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
            newsItem = (NewsItem)bundle.getSerializable("newsItem");
        }
        initViews(view);
	    getData();
        return view;
    }

	private void getData()
	{
		NewsItem item = NewsManager.getInstance().getNewDetails(this.newsItem.ID,getActivity());
		if(item!=null){
			newsList = new ArrayList<>();
			newsList.add(item);
			handleRequestFinished(NEWS_DETAILS_REQUEST_ID,null,newsList);
		}
		else
		{
			NewsFilterData newsFilterData = new NewsFilterData();
			newsFilterData.newsID = this.newsItem.ID;
			newsFilterData.newsCategory = "All";
			NewsDetailsOperation operation = new NewsDetailsOperation(NEWS_DETAILS_REQUEST_ID, true, getActivity(), newsFilterData, 100, 0);
			operation.addRequsetObserver(this);
			operation.execute();
		}
	}

	private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts()
    {

	    UiEngine.applyFontsForAll(getActivity(),view, UiEngine.Fonts.HVAR);
        if(txtv_news_details_newTitle!=null)
            UiEngine.applyCustomFont(txtv_news_details_newTitle, UiEngine.Fonts.HVAR);
        if(txtv_news_details_newDate!=null)
            UiEngine.applyCustomFont(txtv_news_details_newDate, UiEngine.Fonts.HVAR);
        if(txtv_news_details_newBody!=null)
            UiEngine.applyCustomFont(txtv_news_details_newDate, UiEngine.Fonts.HVAR);
	    if(txtv_noData!=null)
	    {
		    UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
	    }
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
	    txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
	    layout_detailsContainer = (RelativeLayout) view.findViewById(R.id.layout_detailsContainer);

    }

    private void bindViews()
    {
        if(this.newsItem!=null)
        {
	        layout_detailsContainer.setVisibility(View.VISIBLE);
	        txtv_noData.setVisibility(View.GONE);

	        txtv_news_details_newTitle.setText(newsItem.Title);
            txtv_news_details_newDate.setText(newsItem.CreationDate);
	        String decodedBody = Uri.decode(newsItem.NewsBody);
	        txtv_news_details_newBody.setText(Html.fromHtml(decodedBody));

	        if(!Utilities.isNullString(newsItem.ImageUrl))
	        {
		        Picasso.with(getActivity())
				        .load(newsItem.ImageUrl)
				        .placeholder(R.drawable.news_image_place_holder)
				        .into(imgv_news_details_img);
	        }
	        else
	            imgv_news_details_img.setImageResource(R.drawable.news_image_place_holder);
        }
	    else
        {
	        layout_detailsContainer.setVisibility(View.GONE);
	        txtv_noData.setText(getResources().getString(R.string.details_no_details));
	        txtv_noData.setVisibility(View.VISIBLE);
        }
    }


	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if (error == null)
		{
			Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
			if((int)requestId == NEWS_DETAILS_REQUEST_ID && resultObject!=null){
				newsList= (ArrayList<NewsItem>) resultObject;
				if (newsList!=null&&newsList.size()>0)
				{
					newsItem=newsList.get(0);
					bindViews();
				}
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
