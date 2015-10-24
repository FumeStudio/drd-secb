package com.secb.android.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.secb.android.R;
import com.secb.android.controller.backend.EventsCategoryOperation;
import com.secb.android.controller.backend.EventsCityOperation;
import com.secb.android.controller.backend.EventsListOperation;
import com.secb.android.controller.backend.GalleryOperation;
import com.secb.android.controller.backend.NewsCategoryOperation;
import com.secb.android.controller.backend.NewsListOperation;
import com.secb.android.model.E_ServiceItem;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsFilterData;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.fragments.AboutUsFragment;
import com.secb.android.view.fragments.AlbumFragment;
import com.secb.android.view.fragments.ContactUsFragment;
import com.secb.android.view.fragments.E_ServiceDetailsFragment;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends SECBBaseActivity implements RequestObserver {
    private static final String TAG = "MainActivity";

	private static final int PHOTO_GALLERY_REQUEST_ID = 1;
	private static final int VIDEO_GALLERY_REQUEST_ID = 2;
	private static final int NEWS_CATEGORY_REQUEST_ID = 3;
	private static final int NEWS_LIST_REQUEST_ID = 4;
	private static final int EVENTS_CATEGORY_REQUEST_ID = 5;
	private static final int EVENTS_LIST_REQUEST_ID = 6;
	private static final int EVENTS_CITY_REQUEST_ID = 7;

	LinearLayout fragmentContainer;
    HomeFragment homeFragment;

	ArrayList<RequestObserver> galleryRequstObserverList;
	ArrayList<RequestObserver> newsRequstObserverList;
	ArrayList<RequestObserver> eventsRequstObserverList;

	private RequestObserver newsRequstObserver;
	public boolean isNewsLoadingFinished;
	private RequestObserver eventsRequstObserver;
	public boolean isEventsLoadingFinished;
	private RequestObserver galleryRequstObserver ;
	public boolean isPhotoGalleryLoadingFinished;
	public boolean isVideoGalleryLoadingFinished;

	public static SimpleDateFormat sdf_Date = new SimpleDateFormat("MM/dd/yyyy", UiEngine.getCurrentAppLocale());
	public static SimpleDateFormat sdf_Time = new SimpleDateFormat("kk:mm a", UiEngine.getCurrentAppLocale());
	public static SimpleDateFormat sdf_DateTime = new SimpleDateFormat("dd/MM/yyyy kk:mm",  UiEngine.getCurrentAppLocale());
	public static SimpleDateFormat sdf_Source = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss ",  UiEngine.getCurrentAppLocale());




	public MainActivity() {
        super(-1, true);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
/*        showHeader(true);
        setHeaderTitleText(getResources().getString(R.string.home_fragment));*/
	    initObservers();
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

	private void initObservers() {
		galleryRequstObserverList = new ArrayList<>();
		newsRequstObserverList= new ArrayList<>();
		eventsRequstObserverList= new ArrayList<>();
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

	public void openE_ServiceDetailsFragment(E_ServiceItem e_serviceItem) {
		E_ServiceDetailsFragment fragment = E_ServiceDetailsFragment.newInstance(e_serviceItem);
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
	/*Photo Gallery*/
		getPhotoGallery();

	/*Vidoe Gallery*/
		getVideoGallery();

	/*News Categories*/
		getNewsCategories();

	/*News UnFiltered News List*/
		getNewsList();

	/*Events Categories*/
		getEventsCategories();

	/*Events UnFiltered  List*/
		getEventsList();

	/*Events Cities List*/
		getEventsCities();
	}



	/**
	 * observers
	 */
//gallery
	public void setGalleryRequstObserver(RequestObserver galleryRequstObserver)
	{
		this.galleryRequstObserver = galleryRequstObserver;
		galleryRequstObserverList.add(galleryRequstObserver);
	}
//news
	public void setNewsRequstObserver(RequestObserver newsRequstObserver)
	{
		this.newsRequstObserver = newsRequstObserver;
		newsRequstObserverList.add(newsRequstObserver);
	}
//events
	public void setEventsRequstObserver(RequestObserver newsRequstObserver)
	{
		this.eventsRequstObserver = newsRequstObserver;
		eventsRequstObserverList.add(newsRequstObserver);
	}


	/**
	 * Operations
	 */
//photo gallery
	private void getPhotoGallery()
	{
		final GalleryOperation operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY,PHOTO_GALLERY_REQUEST_ID, false,this, 100,0);
		operation.addRequsetObserver(this);
		operation.execute();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(10000);
//					operation.execute();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}
//video gallery
	private void getVideoGallery() {
		final GalleryOperation operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY,VIDEO_GALLERY_REQUEST_ID, false,this, 100,0);
		operation.addRequsetObserver(this);
		operation.execute();
	}
//news categories
	public void getNewsCategories()
	{
		NewsCategoryOperation operation = new NewsCategoryOperation(NEWS_CATEGORY_REQUEST_ID, false,this);
		operation.addRequsetObserver(this);
		operation.execute();
	}
//news list
public void getNewsList()
	{
		NewsFilterData newsFilterData = new NewsFilterData();
		newsFilterData.newsCategory="All";
		newsFilterData.newsID="All";
		NewsListOperation operation = new NewsListOperation(NEWS_LIST_REQUEST_ID,false,this,newsFilterData,100,0);
		operation.addRequsetObserver(this);
		operation.execute();
	}
//events cities
	public void getEventsCities()
	{
		EventsCityOperation operation = new EventsCityOperation(EVENTS_CITY_REQUEST_ID ,false,this);
		operation.addRequsetObserver(this);
		operation.execute();
	}
	//events categories
	public void getEventsCategories()
	{
		EventsCategoryOperation operation = new EventsCategoryOperation(EVENTS_CATEGORY_REQUEST_ID ,false,this);
		operation.addRequsetObserver(this);
		operation.execute();
	}
//events list
public void getEventsList()
	{
		EventsFilterData eventsFilterData = new EventsFilterData();
		final EventsListOperation operation = new EventsListOperation(EVENTS_LIST_REQUEST_ID,false,this,eventsFilterData,100,0);
		operation.addRequsetObserver(this);
		operation.execute();
	}












	/*let fragment handle requestFinished*/
	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject)
	{
//gallery
		if( ((int)requestId == PHOTO_GALLERY_REQUEST_ID ||
				(int)requestId == VIDEO_GALLERY_REQUEST_ID) &&
				/*galleryRequstObserver!=null  && */galleryRequstObserverList.size()>0)
		{
			if( (int)requestId == PHOTO_GALLERY_REQUEST_ID)
				isPhotoGalleryLoadingFinished = true;
			else if( (int)requestId == VIDEO_GALLERY_REQUEST_ID)
				isVideoGalleryLoadingFinished = true;

			for(RequestObserver iterator:galleryRequstObserverList)
				iterator.handleRequestFinished(requestId,error,resulObject);
		}
//news
		else if( ((int)requestId == NEWS_LIST_REQUEST_ID ||
				(int)requestId == NEWS_CATEGORY_REQUEST_ID)/* &&
				newsRequstObserver!=null*/ && newsRequstObserverList.size()>0)
		{
			if( (int)requestId == NEWS_LIST_REQUEST_ID)
				isNewsLoadingFinished = true;
			for(RequestObserver iterator:newsRequstObserverList)
				iterator.handleRequestFinished(requestId,error,resulObject);
		}
//events
		else if( (  (int)requestId == EVENTS_LIST_REQUEST_ID ||
					(int)requestId == EVENTS_CATEGORY_REQUEST_ID ||
					(int)requestId == EVENTS_CITY_REQUEST_ID
				 )
				/*&& eventsRequstObserver!=null*/
				&& eventsRequstObserverList.size()>0)
		{
			if( (int)requestId == EVENTS_LIST_REQUEST_ID)
				isEventsLoadingFinished = true;
			for(RequestObserver iterator:eventsRequstObserverList)
				iterator.handleRequestFinished(requestId,error,resulObject);
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error)
	{
//gallery
		if( (requestId == PHOTO_GALLERY_REQUEST_ID ||
				requestId == VIDEO_GALLERY_REQUEST_ID) &&
				galleryRequstObserver!=null)
		{
			galleryRequstObserver.requestCanceled(requestId, error);
		}
//news
		else if(requestId == NEWS_LIST_REQUEST_ID && newsRequstObserver!=null && newsRequstObserver!=null)
		{
			newsRequstObserver.requestCanceled(requestId, error);
		}
//events
		else if(requestId == EVENTS_LIST_REQUEST_ID &&eventsRequstObserver!=null)
		{
			eventsRequstObserver.requestCanceled(requestId, error);
		}
	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg)
	{
//gallery
		if( (requestId == PHOTO_GALLERY_REQUEST_ID ||
				requestId == VIDEO_GALLERY_REQUEST_ID) &&
				galleryRequstObserver!=null)
		{
			galleryRequstObserver.updateStatus(requestId, statusMsg);
		}
//news
		else if(requestId == NEWS_LIST_REQUEST_ID && eventsRequstObserver!=null)
		{
			newsRequstObserver.updateStatus(requestId, statusMsg);
		}
//events
		else if(requestId == EVENTS_LIST_REQUEST_ID &&eventsRequstObserver!=null)
		{
			eventsRequstObserver.updateStatus(requestId, statusMsg);
		}
	}

	public static String reFormatDate(String oldDate,SimpleDateFormat sdf){
		String newString = null;
		try
		{
			Date date  = sdf_Source.parse(oldDate);
			newString = sdf.format(date);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return newString;
	}
}
