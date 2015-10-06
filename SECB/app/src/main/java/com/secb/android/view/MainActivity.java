package com.secb.android.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.NewsItem;
import com.secb.android.view.components.DividerItemDecoration;
import com.secb.android.view.components.ProgressWheel;
import com.secb.android.view.components.news_recycler.NewsItemRecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends SECBBaseActivity
{
    ProgressWheel progressWheelClosed,progressWheelInbox,progressWheelInProgress;
    private static final int PROGRESS_WHEEL_TIME = 2 * 1000;

    int[] graphsValues;
    TextView txtv_graph_title_closed,txtv_graph_value_closed,
             txtv_graph_title_inbox,txtv_graph_value_inbox,
             txtv_graph_title_inProgress,txtv_graph_value_inProgress;

    RecyclerView newsRecyclerView;
    NewsItemRecyclerAdapter newsItemRecyclerAdapter;
    ArrayList<NewsItem> newsList;


    public MainActivity() {
        super(R.layout.home, true);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
        showHeader(true);
        setHeaderTitleText(getResources().getString(R.string.home));
        initViews();
    }

    private void initViews() {
        progressWheelClosed=  (ProgressWheel) findViewById(R.id.progressWheelClosed);
        progressWheelInbox=  (ProgressWheel) findViewById(R.id.progressWheelInbox);
        progressWheelInProgress=  (ProgressWheel) findViewById(R.id.progressWheelProgress);


        txtv_graph_title_closed= (TextView) findViewById(R.id.txtv_graph_title_closed);
        txtv_graph_value_closed= (TextView) findViewById(R.id.txtv_graph_value_closed);

        txtv_graph_title_inbox= (TextView) findViewById(R.id.txtv_graph_title_inbox);
        txtv_graph_value_inbox= (TextView) findViewById(R.id.txtv_graph_value_inbox);

        txtv_graph_title_inProgress= (TextView) findViewById(R.id.txtv_graph_title_inProgress);
        txtv_graph_value_inProgress= (TextView) findViewById(R.id.txtv_graph_value_inProgress);

        txtv_graph_title_closed.setText(Html.fromHtml(getString(R.string.graph_title_closed)));
        txtv_graph_title_inbox.setText(Html.fromHtml(getString(R.string.graph_title_inbox)));
        txtv_graph_title_inProgress.setText(Html.fromHtml(getString(R.string.graph_title_inProgress)));

        newsList = new ArrayList<>();
        NewsItem newsItem = new NewsItem();
        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate="2 days ago";
        newsList.add(newsItem);
        newsList.add(newsItem);

        newsRecyclerView = (RecyclerView) findViewById(R.id.newsItemRecyclerView);
        newsItemRecyclerAdapter = new NewsItemRecyclerAdapter(this,newsList);
        newsRecyclerView.setAdapter(newsItemRecyclerAdapter);
        newsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        graphsValues = new int[]{15,36,82};
        fillWheelPercentage(graphsValues[0], graphsValues[1], graphsValues[2]);
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
