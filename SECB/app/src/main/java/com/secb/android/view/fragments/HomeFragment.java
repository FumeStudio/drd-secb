package com.secb.android.view.fragments;

import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.model.EventItem;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.dialogs.ProgressWheel;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;
import com.secb.android.view.components.recycler_news.NewsItemRecyclerAdapter;
import com.secb.android.view.menu.MenuItem;
import com.squareup.picasso.Picasso;

import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener, RecyclerCustomClickListener, RequestObserver {

	private static final String TAG = "HomeFragment";
	private static final int NEWS_LIST_REQUEST_ID = 4;
	private static final int EVENTS_LIST_REQUEST_ID = 6;
	ProgressWheel progressWheelClosed, progressWheelInbox, progressWheelInProgress;
    private static final int PROGRESS_WHEEL_TIME = 2 * 1000;

    int[] graphsValues;
    TextView txtv_graph_title_closed, txtv_graph_title_inProgress,txtv_graph_title_inbox,
            txtv_graph_value_inbox, txtv_graph_value_closed, txtv_graph_value_inProgress;

    TextView txtv_viewAllNews;

    TextView txtv_noData;
    RecyclerView newsRecyclerView;
    NewsItemRecyclerAdapter newsItemRecyclerAdapter;
    ArrayList<NewsItem> newsList;
    EventItem eventItem;

    View view;
    View event_card_container;
    private ImageView imgv_eventImg;
    private TextView textViewTitleHeader;
    private TextView txtv_home_last_news_title;
    private TextView txtv_eventTitle;
    private TextView txtv_eventDescription;
    private TextView txtv_event_timeValue;
    private TextView txtv_event_placeValue;
    private TextView txtv_event_categoryValue;



    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.home));
        SECBBaseActivity.setMenuItemSelected(MenuItem.MENU_HOME);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.home_fragment, container, false);

            handleButtonsEvents();

        }
	    ((MainActivity)getActivity()).setNewsRequstObserver(this);
	    ((MainActivity)getActivity()).setEventsRequstObserver(this);
        initViews(view);
	    getData();
        applyFonts();
        return view;
    }

	private void getData()
	{
	/**News */
		//if news list is loaded in the manager get it and bind
		//if not and the main activity is still loading the news list
		// wait for it and it will notify handleRequestFinished in this fragment.
		//if the main activity finished loading news list and the manager is still empty
		//start operation here.

		newsList = (ArrayList<NewsItem>) NewsManager.getInstance().getNewsUnFilteredList(getActivity());
		if(newsList!= null && newsList.size()>0){
			handleRequestFinished(NEWS_LIST_REQUEST_ID, null, newsList);
		}
		else {
			//retry
			if (((MainActivity) getActivity()).isNewsLoadingFinished==false) {
				((MainActivity) getActivity()).getNewsList(); /* (new NewsFilterData(),false);*/
			}
		}

	/**Event */
		List<EventItem> eventList = EventsManager.getInstance().getEventsUnFilteredList(getActivity());
		if(eventList!=null && eventList.size()>0){
			handleRequestFinished(EVENTS_LIST_REQUEST_ID, null, eventList);
		}
		else{
			//retry
			if (((MainActivity) getActivity()).isEventsLoadingFinished==false) {
				((MainActivity) getActivity()).getEventsList();/*startEventsListOperation(new NewsFilterData(),false);*/
			}
		}
	}

	private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */

    public void applyFonts()
    {
        if(textViewTitleHeader!=null)
        {
            UiEngine.applyCustomFont(textViewTitleHeader, UiEngine.Fonts.HVAR);
        }

        if(txtv_home_last_news_title!=null)
            UiEngine.applyCustomFont(txtv_home_last_news_title, UiEngine.Fonts.HVAR);


        if(txtv_graph_title_closed!=null)
            UiEngine.applyCustomFont(txtv_graph_title_closed, UiEngine.Fonts.HVAR);
        if(txtv_graph_value_closed!=null)
            UiEngine.applyCustomFont(txtv_graph_value_closed, UiEngine.Fonts.HVAR);
        if(txtv_graph_title_inbox!=null)
            UiEngine.applyCustomFont(txtv_graph_title_inbox, UiEngine.Fonts.HVAR);
        if(txtv_graph_value_inbox!=null)
            UiEngine.applyCustomFont(txtv_graph_value_inbox, UiEngine.Fonts.HVAR);
        if(txtv_graph_title_inProgress!=null)
            UiEngine.applyCustomFont(txtv_graph_title_inProgress, UiEngine.Fonts.HVAR);
        if(txtv_graph_value_inProgress!=null)
            UiEngine.applyCustomFont(txtv_graph_value_inProgress, UiEngine.Fonts.HVAR);
        if(txtv_viewAllNews!=null)
            UiEngine.applyCustomFont(txtv_viewAllNews, UiEngine.Fonts.HVAR);
        if(txtv_eventTitle!=null)
            UiEngine.applyCustomFont(txtv_eventTitle, UiEngine.Fonts.HVAR);
        if(txtv_eventDescription!=null)
            UiEngine.applyCustomFont(txtv_eventDescription, UiEngine.Fonts.HVAR);
        if(txtv_event_timeValue!=null)
            UiEngine.applyCustomFont(txtv_event_timeValue, UiEngine.Fonts.HVAR);
        if(txtv_event_placeValue!=null)
            UiEngine.applyCustomFont(txtv_event_placeValue, UiEngine.Fonts.HVAR);
        if(txtv_event_categoryValue!=null)
            UiEngine.applyCustomFont(txtv_event_categoryValue, UiEngine.Fonts.HVAR);
	    if(txtv_noData!=null)
	    {
		    UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
	    }
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
                ((MainActivity) getActivity()).openNewsListFragment();
                break;
            case R.id.event_card_container:
                ((MainActivity) getActivity()).openEventDetailsFragment(eventItem);
                break;
            default:
                break;
        }
    }


    private void initViews(View view)
    {
    //Service Requests
        progressWheelClosed = (ProgressWheel) view.findViewById(R.id.progressWheelClosed);
        progressWheelInbox = (ProgressWheel) view.findViewById(R.id.progressWheelInbox);
        progressWheelInProgress = (ProgressWheel) view.findViewById(R.id.progressWheelProgress);

        textViewTitleHeader = (TextView) view.findViewById(R.id.textViewTitleHeader);
        txtv_home_last_news_title = (TextView) view.findViewById(R.id.txtv_home_last_news_title);


        txtv_graph_title_closed = (TextView) view.findViewById(R.id.txtv_graph_title_closed);
        txtv_graph_value_closed = (TextView) view.findViewById(R.id.txtv_graph_value_closed);

        txtv_graph_title_inbox = (TextView) view.findViewById(R.id.txtv_graph_title_inbox);
        txtv_graph_value_inbox = (TextView) view.findViewById(R.id.txtv_graph_value_inbox);

        txtv_graph_title_inProgress = (TextView) view.findViewById(R.id.txtv_graph_title_inProgress);
        txtv_graph_value_inProgress = (TextView) view.findViewById(R.id.txtv_graph_value_inProgress);

        txtv_graph_title_closed.setText(Html.fromHtml(getString(R.string.graph_title_closed)));
        txtv_graph_title_inbox.setText(Html.fromHtml(getString(R.string.graph_title_inbox)));
        txtv_graph_title_inProgress.setText(Html.fromHtml(getString(R.string.graph_title_inProgress)));

//Event Card
        txtv_eventTitle = (TextView) view.findViewById(R.id.txtv_eventTitle);
        txtv_eventDescription = (TextView) view.findViewById(R.id.txtv_eventDescription);
        txtv_event_timeValue = (TextView) view.findViewById(R.id.txtv_event_timeValue);
        txtv_event_placeValue = (TextView) view.findViewById(R.id.txtv_event_placeValue);
        txtv_event_categoryValue = (TextView) view.findViewById(R.id.txtv_event_categoryValue);
        imgv_eventImg = (ImageView)view.findViewById(R.id.imgv_eventImg);
        event_card_container=view.findViewById(R.id.event_card_container);
	    event_card_container.setVisibility(View.GONE);
        event_card_container.setOnClickListener(this);

//	    eventItem = DevData.getEventsList().get(0);
//	    eventItem = (EventsManager.getInstance().getEventsUnFilteredList())!=null ?
//			    EventsManager.getInstance().getEventsUnFilteredList().get(0):null;
//	    bindEventCard();

//News Recycler
	    txtv_viewAllNews = (TextView) view.findViewById(R.id.txtv_viewAllNews);
	    txtv_viewAllNews.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
	    txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
/*        newsList = DevData.getNewsList();*/
	    newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
//        newsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), newsRecyclerView, this));

        graphsValues = new int[]{15, 36, 82};
        fillWheelPercentage(graphsValues[0], graphsValues[1], graphsValues[2]);

        txtv_viewAllNews.setOnClickListener(this);
    }

    private void bindEventCard()
    {
	    if(eventItem==null){
		    event_card_container.setVisibility(View.GONE);
		    return;
	    }

	    event_card_container.setVisibility(View.VISIBLE);
	    if(!Utilities.isNullString(eventItem.ImageUrl))
	    {
		    Picasso.with(getActivity())
				    .load(eventItem.ImageUrl)
				    .placeholder(R.drawable.events_image_place_holder)
				    .into(imgv_eventImg)
		    ;
	    }
	    else
		    imgv_eventImg.setImageResource(R.drawable.events_image_place_holder);

        txtv_eventTitle.setText(eventItem.Title);
        txtv_eventDescription.setText(eventItem.Description);
	    String evdateStr = MainActivity.reFormatDate(eventItem.EventDate, MainActivity.sdf_Date);
        txtv_event_timeValue.setText(evdateStr);
        txtv_event_placeValue.setText(eventItem.EventSiteCity);
        txtv_event_categoryValue.setText(eventItem.EventCategory);
    }

	private void bindNewsRecycler(){
//		newsList = (ArrayList<NewsItem>) NewsManager.getInstance().getNewsUnFilteredList();
		ArrayList<NewsItem>homeNewsList = new ArrayList<>();
		//for testing set newsList = null;
//		newsList = null;
		if (newsList != null && newsList.size() > 2) {
			homeNewsList.add(newsList.get(0));
			homeNewsList.add(newsList.get(1));
			/*while(newsList.size()>2)
			{
				newsList.remove(newsList.size()-1);
			}*/
			newsRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			newsItemRecyclerAdapter = new NewsItemRecyclerAdapter(getActivity(), homeNewsList);
			newsRecyclerView.setAdapter(newsItemRecyclerAdapter);
		}
		else
		{
			newsRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			txtv_noData.setText(getString(R.string.news_no_news));
		}

	}
    private void fillWheelPercentage(int closedScore, int inboxScore, int inProgressScore) {

        int largeRadius = (int) getResources().getDimension(R.dimen.home_graphs_wheel_size);

        txtv_graph_value_closed.setText("" + closedScore);
        txtv_graph_value_inbox.setText("" + inboxScore);
        txtv_graph_value_inProgress.setText("" + inProgressScore);

        progressWheelClosed.setColors(getResources().getColor(R.color.graph_color_closed), getResources().getColor(R.color.sceb_dark_blue));
        progressWheelInbox.setColors(getResources().getColor(R.color.graph_color_inbox), getResources().getColor(R.color.sceb_dark_blue));
        progressWheelInProgress.setColors(getResources().getColor(R.color.graph_color_inProgress), getResources().getColor(R.color.sceb_dark_blue));

        progressWheelClosed.startLoading(closedScore, PROGRESS_WHEEL_TIME, "" + closedScore, largeRadius);
        progressWheelInbox.startLoading(inboxScore, PROGRESS_WHEEL_TIME, "" + inboxScore, largeRadius);
        progressWheelInProgress.startLoading(inProgressScore, PROGRESS_WHEEL_TIME, "" + inProgressScore, largeRadius);
    }

    @Override
    public void onItemClicked(View v, int position) {
        ((MainActivity) getActivity()).openNewDetailsFragment(newsList.get(position));
    }

    @Override
    public void onItemLongClicked(View v, int position) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        ((MainActivity) getActivity()).displayToast("language changed successfully");

    }

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if (error == null)
		{
			if((int)requestId == NEWS_LIST_REQUEST_ID && resultObject!=null)
			{
				newsList= (ArrayList<NewsItem>) resultObject;
				bindNewsRecycler();
			}
			else if((int)requestId == EVENTS_LIST_REQUEST_ID && resultObject!=null)
			{
				ArrayList<EventItem> temp = (ArrayList<EventItem>) resultObject;
				if (temp!=null  &&temp.size()>0){
					eventItem=	temp.get(0);
					bindEventCard();
				}
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
