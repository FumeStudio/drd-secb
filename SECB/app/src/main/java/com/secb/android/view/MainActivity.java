package com.secb.android.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.secb.android.R;
import com.secb.android.controller.backend.NewsCategoryOperation;
import com.secb.android.controller.backend.NewsListOperation;
import com.secb.android.model.EventItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsFilterData;
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

import net.comptoirs.android.common.controller.backend.RequestObserver;

public class MainActivity extends SECBBaseActivity implements RequestObserver {
    private static final String TAG = "MainActivity";

	private static final int NEWS_CATEGORY_REQUEST_ID = 3;
	private static final int NEWS_LIST_REQUEST_ID = 4;
	LinearLayout fragmentContainer;
    HomeFragment homeFragment;

	private RequestObserver newsRequstObserver;
	public boolean isNewsLoadingFinished;

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

	    //get server side data
	    startSync();

/*
	    //Initializes the Google Maps Android API so that its classes are ready for use
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
*/


    }

    private void initiViews()
    {
        fragmentContainer = (LinearLayout) findViewById(R.id.simple_fragment);
    }

//==================================================================================================

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

    public void openAlbumFragment(int galleryType, String folderPath , String albumId)
    {
        AlbumFragment albumFragment = AlbumFragment.newInstance(galleryType,folderPath , albumId);
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
//==================================================================================================

	/*background tasks to
	* get the data (news list , events list , ...)
	* and to update managers */
	public void startSync()
	{
	/*News Categories*/
		getNewsCategories();

	/*News UnFiltered List*/
		getNewsList();
	}

	public void setNewsRequstObserver(RequestObserver newsRequstObserver)
	{
		this.newsRequstObserver = newsRequstObserver;
	}
	public void getNewsCategories()
	{
		NewsCategoryOperation operation = new NewsCategoryOperation(NEWS_CATEGORY_REQUEST_ID ,false,this);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	private void getNewsList()
	{
		NewsFilterData newsFilterData = new NewsFilterData();
		newsFilterData.newsCategory="All";
		newsFilterData.newsID="All";
		NewsListOperation operation = new NewsListOperation(NEWS_LIST_REQUEST_ID,false,this,newsFilterData,100,0);
		operation.addRequsetObserver(this);
		operation.execute();
	}











/*let fragment handle requestFinished*/
	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject)
	{
		if((int)requestId == NEWS_LIST_REQUEST_ID && newsRequstObserver!=null)
		{
			isNewsLoadingFinished = true;
			newsRequstObserver.handleRequestFinished(requestId,error,resulObject);
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error)
	{
		if(requestId == NEWS_LIST_REQUEST_ID && newsRequstObserver!=null)
		{
			newsRequstObserver.requestCanceled(requestId, error);
		}
	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg)
	{
		if(requestId == NEWS_LIST_REQUEST_ID && newsRequstObserver!=null)
		{
			newsRequstObserver.updateStatus(requestId, statusMsg);
		}
	}
}
