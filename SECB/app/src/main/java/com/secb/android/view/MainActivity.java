package com.secb.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.secb.android.R;
import com.secb.android.view.fragments.HomeFragment;
import com.secb.android.view.fragments.NewsFragment;

public class MainActivity extends SECBBaseActivity
{
    LinearLayout fragmentContainer;
    HomeFragment homeFragment;
    NewsFragment newsFragment;

    public MainActivity() {
        super(-1, true);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
/*        showHeader(true);
        setHeaderTitleText(getResources().getString(R.string.home_fragment));*/
        initiViews();

    }

    private void initiViews() {
        fragmentContainer = (LinearLayout) findViewById(R.id.simple_fragment);
        homeFragment = HomeFragment.newInstance();
        addFragment(homeFragment, false, FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void startNewsActivity()
    {
        newsFragment = NewsFragment.newInstance();
        addFragment(newsFragment, true, FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }
}
