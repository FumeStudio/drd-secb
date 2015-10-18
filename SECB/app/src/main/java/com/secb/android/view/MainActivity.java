package com.secb.android.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.secb.android.R;
import com.secb.android.model.EventItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.fragments.AboutUsFragment;
import com.secb.android.view.fragments.AlbumFragment;
import com.secb.android.view.fragments.ContactUsFragment;
import com.secb.android.view.fragments.E_ServicesListFragment;
import com.secb.android.view.fragments.EguideHomeFragment;
import com.secb.android.view.fragments.EventDetailsFragment;
import com.secb.android.view.fragments.EventsCalendarFragment;
import com.secb.android.view.fragments.EventsListFragment;
import com.secb.android.view.fragments.GalleryFragment;
import com.secb.android.view.fragments.HomeFragment;
import com.secb.android.view.fragments.LocationsDetailsFragment;
import com.secb.android.view.fragments.LocationsListFragment;
import com.secb.android.view.fragments.NewsDetailsFragment;
import com.secb.android.view.fragments.NewsListFragment;
import com.secb.android.view.fragments.OrganizersDetailsFragment;
import com.secb.android.view.fragments.OrganizersListFragment;

public class MainActivity extends SECBBaseActivity {
    private static final String TAG = "MainActivity";
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
        openHomeFragment(false);
//        initMap();

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private void initiViews()
    {
        fragmentContainer = (LinearLayout) findViewById(R.id.simple_fragment);
    }


    public void openHomeFragment(boolean addToBackStack)
    {
        homeFragment = HomeFragment.newInstance();
        addFragment(homeFragment, addToBackStack, FragmentTransaction.TRANSIT_EXIT_MASK, true);
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

    public void openEventsCalendarFragment() {
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


    public void openGalleryFragment(int galleryType, int galleryId)
    {
        GalleryFragment galleryFragment = GalleryFragment.newInstance(galleryType,galleryId);
        addFragment(galleryFragment, galleryFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openAlbumFragment(int galleryType, int galleryId)
    {
        AlbumFragment albumFragment = AlbumFragment.newInstance(galleryType,galleryId);
        addFragment(albumFragment, albumFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openContactUsFragment(){
        ContactUsFragment contactUsFragment = ContactUsFragment.newInstance();
        addFragment(contactUsFragment, contactUsFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openE_ServicesFragment(){
        E_ServicesListFragment fragment = E_ServicesListFragment.newInstance();
        addFragment(fragment, fragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openAboutUsFragment(){
        AboutUsFragment fragment = AboutUsFragment.newInstance();
        addFragment(fragment, fragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void openPlayerFragment(String videoUrl) {
//        VideoPlayerFragment videoPlayerFragment = VideoPlayerFragment.newInstance(videoUrl);
//        addFragment(videoPlayerFragment,videoPlayerFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"), "video/*");
//        intent.setDataAndType(Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo"), "video/*");

        if (intent.resolveActivity(getPackageManager())!=null)
            startActivity(intent);
        else
            displayToast("can't play this video file ");
    }


}
