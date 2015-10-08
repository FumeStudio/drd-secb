package com.secb.android.view.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.NewsItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.DividerItemDecoration;
import com.secb.android.view.components.ProgressWheel;
import com.secb.android.view.components.news_recycler.NewsItemRecyclerAdapter;
import com.secb.android.view.menu.MenuItem;

import java.util.ArrayList;

public class HomeFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener
{

	ProgressWheel progressWheelClosed,progressWheelInbox,progressWheelInProgress;
	private static final int PROGRESS_WHEEL_TIME = 2 * 1000;

	int[] graphsValues;
	TextView txtv_graph_title_closed,txtv_graph_value_closed,
			txtv_graph_title_inbox,txtv_graph_value_inbox,
			txtv_graph_title_inProgress,txtv_graph_value_inProgress;

	TextView txtv_viewAllNews;

	RecyclerView newsRecyclerView;
	NewsItemRecyclerAdapter newsItemRecyclerAdapter;
	ArrayList<NewsItem> newsList;


	View view;

	
	public static HomeFragment newInstance()
	{
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}
	
	@Override
	public void onResume()
	{
	  super.onResume();
		((SECBBaseActivity) getActivity()).addBackObserver(this);
		((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.home));
	  SECBBaseActivity.setMenuItemSelected(MenuItem.MENU_HOME);
	}
	
	@Override
	public void onPause()
	{
	  super.onPause();
	  ((SECBBaseActivity) getActivity()).removeBackObserver(this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (view != null)
		{
			ViewParent oldParent = (ViewParent) view.getRootView();
			if (oldParent != container && oldParent != null)
			{
				((ViewGroup) oldParent).removeView(view);
			}
		}
		else
		{
			view = LayoutInflater.from(getActivity()).inflate(R.layout.home_fragment, container, false);
			
			handleButtonsEvents();
			applyFonts();
		}
		initViews(view);
		return view;
	}
	
	private void handleButtonsEvents()
  {
  }
	
	/*
	 * Apply Fonts
	 */
	private void applyFonts()
  {
		// TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
  }
	
	private void goBack()
  {
		((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
  }
	
	// ////////////////////////////////////////////////////////////
	
	@Override
  public void onBack()
  {
		goBack();
  }

	@Override
  public void onClick(View v)
  {
	  switch (v.getId())
    {
		case R.id.txtv_viewAllNews:
			((MainActivity)getActivity()).openNewsListFragment();
			default:
				break;
		}
  }


	private void initViews(View view) {
		progressWheelClosed=  (ProgressWheel) view.findViewById(R.id.progressWheelClosed);
		progressWheelInbox=  (ProgressWheel) view.findViewById(R.id.progressWheelInbox);
		progressWheelInProgress=  (ProgressWheel) view.findViewById(R.id.progressWheelProgress);


		txtv_graph_title_closed= (TextView) view.findViewById(R.id.txtv_graph_title_closed);
		txtv_graph_value_closed= (TextView) view.findViewById(R.id.txtv_graph_value_closed);

		txtv_graph_title_inbox= (TextView) view.findViewById(R.id.txtv_graph_title_inbox);
		txtv_graph_value_inbox= (TextView) view.findViewById(R.id.txtv_graph_value_inbox);

		txtv_graph_title_inProgress= (TextView) view.findViewById(R.id.txtv_graph_title_inProgress);
		txtv_graph_value_inProgress= (TextView) view.findViewById(R.id.txtv_graph_value_inProgress);

		txtv_viewAllNews= (TextView) view.findViewById(R.id.txtv_viewAllNews);
		txtv_viewAllNews.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

		txtv_graph_title_closed.setText(Html.fromHtml(getString(R.string.graph_title_closed)));
		txtv_graph_title_inbox.setText(Html.fromHtml(getString(R.string.graph_title_inbox)));
		txtv_graph_title_inProgress.setText(Html.fromHtml(getString(R.string.graph_title_inProgress)));

		newsList = new ArrayList<>();
		NewsItem newsItem = new NewsItem();
		newsItem.newsItemTitle = "Saudi Exhibition and Convention";
		newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
		newsItem.newsItemDate="2 days ago";
		newsList.add(newsItem);

		newsItem.newsItemTitle = "Saudi Exhibition and Convention";
		newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
		newsItem.newsItemDate="2 days ago";

		newsList.add(newsItem);

		newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsItemRecyclerView);
		newsItemRecyclerAdapter = new NewsItemRecyclerAdapter(getActivity(),newsList);
		newsRecyclerView.setAdapter(newsItemRecyclerAdapter);
		newsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		graphsValues = new int[]{15,36,82};
		fillWheelPercentage(graphsValues[0], graphsValues[1], graphsValues[2]);

		txtv_viewAllNews.setOnClickListener(this);
	}

	private void fillWheelPercentage(int closedScore, int inboxScore, int inProgressScore)
	{

		int largeRadius = (int) getResources().getDimension(R.dimen.home_graphs_wheel_size);

		txtv_graph_value_closed.setText(""+closedScore);
		txtv_graph_value_inbox.setText(""+inboxScore);
		txtv_graph_value_inProgress.setText("" + inProgressScore);

		progressWheelClosed.setColors(getResources().getColor(R.color.graph_color_closed), getResources().getColor(R.color.sceb_dark_blue));
		progressWheelInbox.setColors(getResources().getColor(R.color.graph_color_inbox),getResources().getColor(R.color.sceb_dark_blue));
		progressWheelInProgress.setColors(getResources().getColor(R.color.graph_color_inProgress), getResources().getColor(R.color.sceb_dark_blue));

		progressWheelClosed.startLoading(closedScore, PROGRESS_WHEEL_TIME, "" + closedScore , largeRadius );
		progressWheelInbox.startLoading(inboxScore, PROGRESS_WHEEL_TIME, "" + inboxScore, largeRadius);
		progressWheelInProgress.startLoading(inProgressScore, PROGRESS_WHEEL_TIME, "" + inProgressScore, largeRadius);
	}
}
