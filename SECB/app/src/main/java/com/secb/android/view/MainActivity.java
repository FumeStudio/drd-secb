package com.secb.android.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.secb.android.R;
import com.secb.android.model.EventItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.fragments.EguideHomeFragment;
import com.secb.android.view.fragments.EventDetailsFragment;
import com.secb.android.view.fragments.EventsCalendarFragment;
import com.secb.android.view.fragments.EventsListFragment;
import com.secb.android.view.fragments.HomeFragment;
import com.secb.android.view.fragments.LocationsDetailsFragment;
import com.secb.android.view.fragments.LocationsListFragment;
import com.secb.android.view.fragments.NewsDetailsFragment;
import com.secb.android.view.fragments.NewsListFragment;
import com.secb.android.view.fragments.OrganizersDetailsFragment;
import com.secb.android.view.fragments.OrganizersListFragment;

public class MainActivity extends SECBBaseActivity implements OnMapReadyCallback {
    LinearLayout fragmentContainer;
    HomeFragment homeFragment;
    GoogleMap googleMap;


    public MapFragment mapFragment ;

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
//        initMap();


    }

    private void initMap() {
        View view = LayoutInflater.from(this).inflate(R.layout.map_fragment, null);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        addFragment(newsListFragment, newsListFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openNewDetailsFragment(NewsItem newsItem)
    {
        NewsDetailsFragment newDetailsFragment = NewsDetailsFragment.newInstance(newsItem);
        addFragment(newDetailsFragment, newDetailsFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openEventListFragment() {
        EventsListFragment eventsListFragment = EventsListFragment.newInstance();
        addFragment(eventsListFragment, eventsListFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void openEventDetailsFragment(EventItem eventItem) {
        EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(eventItem);
        addFragment(eventDetailsFragment, eventDetailsFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openEvenCalendarFragment() {
        EventsCalendarFragment eventsCalendarFragment = EventsCalendarFragment.newInstance();
        addFragment(eventsCalendarFragment, eventsCalendarFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void openEguideHomeFragment() {
        EguideHomeFragment eguideHomeFragment = EguideHomeFragment.newInstance();
        addFragment(eguideHomeFragment, eguideHomeFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openEguideLocationFragment() {
        LocationsListFragment locationsListFragment = LocationsListFragment.newInstance();
        addFragment(locationsListFragment, locationsListFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openEguideOrganizersFragment()
    {
        OrganizersListFragment organizersListFragment = OrganizersListFragment.newInstance();
        addFragment(organizersListFragment, organizersListFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openOrganizerDetailsFragment(OrganizerItem organizerItem) {
        OrganizersDetailsFragment organizersDetailsFragment = OrganizersDetailsFragment.newInstance(organizerItem);
        addFragment(organizersDetailsFragment, organizersDetailsFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openLocationDetailsFragment(LocationItem locationItem) {
        LocationsDetailsFragment locationsDetailsFragment = LocationsDetailsFragment.newInstance(locationItem);
        addFragment(locationsDetailsFragment, locationsDetailsFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public MapFragment getMapFragment() {
        return mapFragment;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

}
