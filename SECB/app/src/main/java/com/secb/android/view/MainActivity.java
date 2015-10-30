package com.secb.android.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.secb.android.R;
import com.secb.android.controller.backend.E_GuideLocationListOperation;
import com.secb.android.controller.backend.E_GuideLocationTypesOperation;
import com.secb.android.controller.backend.E_GuideOrganizersListOperation;
import com.secb.android.controller.backend.E_ServicesRequestsListOperation;
import com.secb.android.controller.backend.E_ServicesStatisticsListOperation;
import com.secb.android.controller.backend.EventsCategoryOperation;
import com.secb.android.controller.backend.EventsCityOperation;
import com.secb.android.controller.backend.EventsListOperation;
import com.secb.android.controller.backend.GalleryOperation;
import com.secb.android.controller.backend.NewsCategoryOperation;
import com.secb.android.controller.backend.NewsListOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.model.E_ServiceStatisticsItem;
import com.secb.android.model.E_ServicesFilterData;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsFilterData;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.LocationsFilterData;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.model.OrganizersFilterData;
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
import com.secb.android.view.fragments.TestFragment;

import net.comptoirs.android.common.controller.backend.RequestObserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends SECBBaseActivity implements RequestObserver {
    private static final String TAG = "MainActivity";


    LinearLayout fragmentContainer;
    HomeFragment homeFragment;

    ArrayList<RequestObserver> galleryRequstObserverList;
    ArrayList<RequestObserver> newsRequstObserverList;
    ArrayList<RequestObserver> eventsRequstObserverList;
    ArrayList<RequestObserver> locationRequstObserverList;
    ArrayList<RequestObserver> organizersRequestObserverList;
    ArrayList<RequestObserver> eservicesRequestObserverList;


    public boolean isNewsLoadingFinished;
    public boolean isEventsLoadingFinished;
    public boolean isPhotoGalleryLoadingFinished;
    public boolean isVideoGalleryLoadingFinished;
    public boolean isLocationLoadingFinished;
    public boolean isOrganizerLoadingFinished;
    public boolean isEservicesRequestLoadingFinished;


    public static SimpleDateFormat sdf_Date = new SimpleDateFormat("MM/dd/yyyy", UiEngine.getCurrentAppLocale());
    public static SimpleDateFormat sdf_Time = new SimpleDateFormat("kk:mm a", UiEngine.getCurrentAppLocale());
    public static SimpleDateFormat sdf_DateTime = new SimpleDateFormat("dd/MM/yyyy kk:mm", UiEngine.getCurrentAppLocale());
    public static SimpleDateFormat sdf_Source = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss ", UiEngine.getCurrentAppLocale());


    public MainActivity() {
        super(-1, true);
    }

    @Override
    protected void doOnCreate(Bundle arg0) {
/*        showHeader(true);
        setHeaderTitleText(getResources().getString(R.string.home_fragment));*/
        initObservers();
        initiViews();
        openHomeFragment(false);

        //get server side data
//        startSync();
        isNewsLoadingFinished = true;
        isEventsLoadingFinished = true;
        isPhotoGalleryLoadingFinished = true;
        isVideoGalleryLoadingFinished = true;
        isLocationLoadingFinished = true;
        isOrganizerLoadingFinished = true;
        isEservicesRequestLoadingFinished = true;
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
        newsRequstObserverList = new ArrayList<>();
        eventsRequstObserverList = new ArrayList<>();
        locationRequstObserverList = new ArrayList<>();
        organizersRequestObserverList = new ArrayList<>();
        eservicesRequestObserverList = new ArrayList<>();
    }

    private void initiViews() {
        fragmentContainer = (LinearLayout) findViewById(R.id.simple_fragment);
    }

//==================================================================================================

    public void openHomeFragment(boolean addToBackStack) {
        homeFragment = HomeFragment.newInstance();
        addFragment(homeFragment, addToBackStack, FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openNewsListFragment() {
        NewsListFragment newsListFragment = NewsListFragment.newInstance();
        addFragment(newsListFragment, newsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openNewDetailsFragment(NewsItem newsItem) {
        NewsDetailsFragment newDetailsFragment = NewsDetailsFragment.newInstance(newsItem);
        addFragment(newDetailsFragment, newDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openEventListFragment() {
        EventsListFragment eventsListFragment = EventsListFragment.newInstance();
        addFragment(eventsListFragment, eventsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void openEventDetailsFragment(EventItem eventItem) {
        EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(eventItem);
        addFragment(eventDetailsFragment, eventDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
//	    openTestFragment(eventItem);
    }

    public void openEventsCalendarFragment() {
        EventsCalendarFragment eventsCalendarFragment = EventsCalendarFragment.newInstance();
        addFragment(eventsCalendarFragment, eventsCalendarFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void openEguideHomeFragment() {
        EguideHomeFragment eguideHomeFragment = EguideHomeFragment.newInstance();
        addFragment(eguideHomeFragment, eguideHomeFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openEguideLocationFragment() {
        LocationsListFragment locationsListFragment = LocationsListFragment.newInstance();
        addFragment(locationsListFragment, locationsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openEguideOrganizersFragment() {
        OrganizersListFragment organizersListFragment = OrganizersListFragment.newInstance();
        addFragment(organizersListFragment, organizersListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openOrganizerDetailsFragment(OrganizerItem organizerItem) {
        OrganizersDetailsFragment organizersDetailsFragment = OrganizersDetailsFragment.newInstance(organizerItem);
        addFragment(organizersDetailsFragment, organizersDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openLocationDetailsFragment(LocationItem locationItem) {
        LocationsDetailsFragment locationsDetailsFragment = LocationsDetailsFragment.newInstance(locationItem);
        addFragment(locationsDetailsFragment, locationsDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void openGalleryFragment(int galleryType, int galleryId) {
        GalleryFragment galleryFragment = GalleryFragment.newInstance(galleryType, galleryId);
        addFragment(galleryFragment, galleryFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }

    public void openAlbumFragment(int galleryType, String folderPath, String albumId) {
        AlbumFragment albumFragment = AlbumFragment.newInstance(galleryType, folderPath, albumId);
        addFragment(albumFragment, albumFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openContactUsFragment() {
        ContactUsFragment contactUsFragment = ContactUsFragment.newInstance();
        addFragment(contactUsFragment, contactUsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openE_ServicesFragment() {
        E_ServicesListFragment fragment = E_ServicesListFragment.newInstance();
        addFragment(fragment, fragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }

    public void openE_ServiceDetailsFragment(E_ServiceRequestItem e_serviceRequestItem) {
        E_ServiceDetailsFragment fragment = E_ServiceDetailsFragment.newInstance(e_serviceRequestItem);
        addFragment(fragment, fragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

//		openTestFragment(null);
    }

    public void openAboutUsFragment() {
        AboutUsFragment fragment = AboutUsFragment.newInstance();
        addFragment(fragment, fragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
    }


    public void openPlayerFragment(String videoUrl) {
//        VideoPlayerFragment videoPlayerFragment = VideoPlayerFragment.newInstance(videoUrl);
//        addFragment(videoPlayerFragment,videoPlayerFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"), "video/*");
//        intent.setDataAndType(Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo"), "video/*");

        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else
            displayToast("can't play this video file ");
    }


    public void openTestFragment(Object item) {
        TestFragment contactUsFragment = TestFragment.newInstance((EventItem) item);
        addFragment(contactUsFragment, contactUsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

    }
//==================================================================================================

    /*background tasks to
    * get the data (news list , events list , galleries,...)
    * and to update managers */
    public void startSync() {
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

	/*E-Guide Location Types List*/
        getEguideLocationTypes();

	/*E-Guide Location List*/
        getEguideLocationList();

	/*E-Guide Organizers List*/
        getEguideOrganizersList();

	/*E-Service Requests List*/
        getEserviceRequestsList();

	/*E-Service Statistics List*/
        getEserviceStatisticsList();
    }


    /**
     * observers
     */
//gallery
    public void setGalleryRequstObserver(RequestObserver galleryRequstObserver) {

        galleryRequstObserverList.add(galleryRequstObserver);
    }

    //news
    public void setNewsRequstObserver(RequestObserver newsRequstObserver) {

        newsRequstObserverList.add(newsRequstObserver);
    }

    //events
    public void setEventsRequstObserver(RequestObserver newsRequstObserver) {

        eventsRequstObserverList.add(newsRequstObserver);
    }

    //e-guide Locations
    public void setLocationRequstObserver(RequestObserver locationRequstObserver) {

        locationRequstObserverList.add(locationRequstObserver);
    }


    //e-guide organizers
    public void setOrganizersRequstObserver(RequestObserver organizersRequestObserver) {

        organizersRequestObserverList.add(organizersRequestObserver);
    }

    //e-services requsets
    public void setEservicesRequstObserver(RequestObserver eservicesRequstObserver) {

        eservicesRequestObserverList.add(eservicesRequstObserver);
    }

    /**
     * Operations
     */
//photo gallery
    private void getPhotoGallery() {
        final GalleryOperation operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY, RequestIds.PHOTO_GALLERY_REQUEST_ID, false, this, 100, 0);
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
        final GalleryOperation operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY, RequestIds.VIDEO_GALLERY_REQUEST_ID, false, this, 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //news categories
    public void getNewsCategories() {
        NewsCategoryOperation operation = new NewsCategoryOperation(RequestIds.NEWS_CATEGORY_REQUEST_ID, false, this);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //news list
    public void getNewsList() {
        NewsFilterData newsFilterData = new NewsFilterData();
        newsFilterData.newsCategory = "All";
        newsFilterData.newsID = "All";
        NewsListOperation operation = new NewsListOperation(RequestIds.NEWS_LIST_REQUEST_ID, false, this, newsFilterData, 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //events cities
    public void getEventsCities() {
        EventsCityOperation operation = new EventsCityOperation(RequestIds.EVENTS_CITY_REQUEST_ID, false, this);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //events categories
    public void getEventsCategories() {
        EventsCategoryOperation operation = new EventsCategoryOperation(RequestIds.EVENTS_CATEGORY_REQUEST_ID, false, this);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //events list
    public void getEventsList() {
        EventsFilterData eventsFilterData = new EventsFilterData();
        final EventsListOperation operation = new EventsListOperation(RequestIds.EVENTS_LIST_REQUEST_ID, false, this, eventsFilterData, 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //E-Guide Location Types List
    private void getEguideLocationTypes() {
        final E_GuideLocationTypesOperation operation = new E_GuideLocationTypesOperation(RequestIds.EGUIDE_LOCATION_TYPES_REQUEST_ID, false, this);
        operation.addRequsetObserver(this);
        operation.execute();
    }


    //E-Guide Location List
    private void getEguideLocationList() {
        E_GuideLocationListOperation operation = new E_GuideLocationListOperation(RequestIds.EGUIDE_LOCATION_LIST_REQUEST_ID, false, this, new LocationsFilterData(), 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //E-Guide Organizers List
    private void getEguideOrganizersList() {
        E_GuideOrganizersListOperation operation = new E_GuideOrganizersListOperation(RequestIds.EGUIDE_ORGANIZERS_LIST_REQUEST_ID, false, this, new OrganizersFilterData(), 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //	E-Service Requests List
    private void getEserviceRequestsList() {
        E_ServicesRequestsListOperation operation = new E_ServicesRequestsListOperation(RequestIds.E_SERVICES_REQUESTS_LIST_REQUEST_ID, false, this, new E_ServicesFilterData(), 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    //	E-Service Requests List
    private void getEserviceStatisticsList() {
        E_ServicesStatisticsListOperation operation = new E_ServicesStatisticsListOperation(RequestIds.E_SERVICES_STATISTICS_LIST_REQUEST_ID, false, this, 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();
    }


    /*let fragment handle requestFinished*/
    @Override
    public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
//gallery
        if (((int) requestId == RequestIds.PHOTO_GALLERY_REQUEST_ID ||
                (int) requestId == RequestIds.VIDEO_GALLERY_REQUEST_ID) &&
                /*galleryRequstObserver!=null  && */galleryRequstObserverList.size() > 0) {
            if ((int) requestId == RequestIds.PHOTO_GALLERY_REQUEST_ID)
                isPhotoGalleryLoadingFinished = true;
            else if ((int) requestId == RequestIds.VIDEO_GALLERY_REQUEST_ID)
                isVideoGalleryLoadingFinished = true;

            for (RequestObserver iterator : galleryRequstObserverList)
                try {
                    iterator.handleRequestFinished(requestId, error, resulObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
//news
        else if (((int) requestId == RequestIds.NEWS_LIST_REQUEST_ID ||
                (int) requestId == RequestIds.NEWS_CATEGORY_REQUEST_ID)/* &&
				newsRequstObserver!=null*/ && newsRequstObserverList.size() > 0) {
            if ((int) requestId == RequestIds.NEWS_LIST_REQUEST_ID)
                isNewsLoadingFinished = true;
            for (RequestObserver iterator : newsRequstObserverList)
                iterator.handleRequestFinished(requestId, error, resulObject);
        }
//events
        else if (((int) requestId == RequestIds.EVENTS_LIST_REQUEST_ID ||
                (int) requestId == RequestIds.EVENTS_CATEGORY_REQUEST_ID ||
                (int) requestId == RequestIds.EVENTS_CITY_REQUEST_ID
        )
				/*&& eventsRequstObserver!=null*/
                && eventsRequstObserverList.size() > 0) {
            if ((int) requestId == RequestIds.EVENTS_LIST_REQUEST_ID)
                isEventsLoadingFinished = true;
            for (RequestObserver iterator : eventsRequstObserverList)
                iterator.handleRequestFinished(requestId, error, resulObject);

            //because location use same city api used by events
            //let location observer listen finishing loading event
            if ((int) requestId == RequestIds.EVENTS_CITY_REQUEST_ID && locationRequstObserverList.size() > 0) {
                for (RequestObserver iterator : locationRequstObserverList)
                    iterator.handleRequestFinished(requestId, error, resulObject);

                //because Organizers use same city api used by events
                //let Organizers observer listen finishing loading event
                if ((int) requestId == RequestIds.EVENTS_CITY_REQUEST_ID && organizersRequestObserverList.size() > 0) {
                    for (RequestObserver iterator : organizersRequestObserverList)
                        iterator.handleRequestFinished(requestId, error, resulObject);
                }
            }
        }
//Eguide Locations
        else if (((int) requestId == RequestIds.EGUIDE_LOCATION_TYPES_REQUEST_ID ||
                (int) requestId == RequestIds.EGUIDE_LOCATION_LIST_REQUEST_ID)
                && locationRequstObserverList.size() > 0) {
            if ((int) requestId == RequestIds.EGUIDE_LOCATION_LIST_REQUEST_ID)
                isLocationLoadingFinished = true;
            for (RequestObserver iterator : locationRequstObserverList)
                iterator.handleRequestFinished(requestId, error, resulObject);
        }
//Eguide Organizers
        else if (((int) requestId == RequestIds.EGUIDE_ORGANIZERS_LIST_REQUEST_ID)
                && organizersRequestObserverList.size() > 0) {
            //this line is commented because there are no other operations in organizers page
//			if( (int)requestId == RequestIds.EGUIDE_ORGANIZERS_LIST_REQUEST_ID)
            isOrganizerLoadingFinished = true;
            for (RequestObserver iterator : organizersRequestObserverList)
                iterator.handleRequestFinished(requestId, error, resulObject);
        }
//Eservices
        else if (((int) requestId == RequestIds.E_SERVICES_REQUESTS_LIST_REQUEST_ID ||
                (int) requestId == RequestIds.E_SERVICES_STATISTICS_LIST_REQUEST_ID)
                && eservicesRequestObserverList.size() > 0) {

            if ((int) requestId == RequestIds.E_SERVICES_REQUESTS_LIST_REQUEST_ID)
                isEservicesRequestLoadingFinished = true;

            for (RequestObserver iterator : eservicesRequestObserverList)
                iterator.handleRequestFinished(requestId, error, resulObject);
        }
    }

    @Override
    public void requestCanceled(Integer requestId, Throwable error) {
    }

    @Override
    public void updateStatus(Integer requestId, String statusMsg) {
    }

    public static String reFormatDate(String oldDate, SimpleDateFormat sdf) {
        String newString = null;
        try {
            Date date = sdf_Source.parse(oldDate);
            newString = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newString;
    }


    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public ArrayList<Integer> calculateGraphsValues() {
        ArrayList<Integer> valuesList = new ArrayList<>();
        //1-get Sum of All requests
        //2-get sum of closedRequests values
        //3-get % of closedRequests in All requests

        ArrayList<E_ServiceStatisticsItem> allRequsts = (ArrayList<E_ServiceStatisticsItem>) E_ServicesManager.getInstance().getEservicesStatisticsList(this);
        if (allRequsts == null || allRequsts.size() == 0)
            return null;

        //sum of all
        int sumOfAllRequests = 0;
        //sum of Closed Requests
        int closedRequests = 0;
        //sum of Inbox Requests
        int inboxRequests = 0;
        //sum of InProgress Requests
        int progressRequests = 0;

        for (E_ServiceStatisticsItem item : allRequsts) {
            int currentValue = 0;
            try {
                currentValue = Integer.parseInt(item.Value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }

            if (currentValue <= 0)
                continue;

            sumOfAllRequests += currentValue;

            if (item.Key.equalsIgnoreCase("ClosedRequests"))
                closedRequests = currentValue;
            else if (item.Key.equalsIgnoreCase("Inbox"))
                inboxRequests = currentValue;
            else if (item.Key.equalsIgnoreCase("InProgress"))
                progressRequests = currentValue;
        }
        valuesList.add((int) (100 * ((double) closedRequests / (double) sumOfAllRequests)));
        valuesList.add((int) (100 * ((double) inboxRequests / (double) sumOfAllRequests)));
        valuesList.add((int) (100 * ((double) progressRequests / (double) sumOfAllRequests)));
        return valuesList;
    }

}
