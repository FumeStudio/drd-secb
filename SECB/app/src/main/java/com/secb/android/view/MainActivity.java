package com.secb.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.secb.android.R;
import com.secb.android.model.NewsItem;
import com.secb.android.view.fragments.HomeFragment;
import com.secb.android.view.fragments.NewsDetailsFragment;
import com.secb.android.view.fragments.NewsListFragment;

public class MainActivity extends SECBBaseActivity
{
    LinearLayout fragmentContainer;
    HomeFragment homeFragment;

    public MainActivity() {
        super(-1, true);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
/*        showHeader(true);
        setHeaderTitleText(getResources().getString(R.string.home_fragment));*/
        initiViews();
        openHomeFragment();

    }

    private void initiViews()
    {
        fragmentContainer = (LinearLayout) findViewById(R.id.simple_fragment);
    }


    private void openHomeFragment()
    {
        homeFragment = HomeFragment.newInstance();
        addFragment(homeFragment, false, FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openNewsListFragment()
    {
        NewsListFragment newsListFragment = NewsListFragment.newInstance();
        addFragment(newsListFragment, true, FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openNewDetailsFragment(NewsItem newsItem)
    {
        NewsDetailsFragment newDetailsFragment = NewsDetailsFragment.newInstance(newsItem);
        addFragment(newDetailsFragment, newDetailsFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }
}
