package com.secb.android.view;

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
import com.secb.android.controller.manager.Engine;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.model.E_ServicesFilterData;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsFilterData;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.LocationsFilterData;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.OrganizersFilterData;
import com.secb.android.view.fragments.HomeFragment;
import com.secb.android.view.fragments.TestFragment;

import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;
import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
	public static SimpleDateFormat sdf_day_mon = new SimpleDateFormat("dd-MMMM", UiEngine.getCurrentAppLocale());
	public static SimpleDateFormat sdf_DateTime = new SimpleDateFormat("dd/MM/yyyy kk:mm", UiEngine.getCurrentAppLocale());
	public static SimpleDateFormat sdf_Source = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss ", UiEngine.getCurrentAppLocale());
	public static SimpleDateFormat sdf_Source_News = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss",  Locale.ENGLISH);
	public static SimpleDateFormat sdf_Source_EService = new SimpleDateFormat("dd MMMM yyyy", UiEngine.getCurrentAppLocale());

	public static SimpleDateFormat sdf_day_MONTH_year = new SimpleDateFormat("dd MMMM yyyy", UiEngine.getCurrentAppLocale());

    public static String STATUS_INPROGRESS = "InProgress";
    public static String STATUS_CLOSEDREQUESTS = "Closes";
    public static String STATUS_INBOX = "Inbox";
	public MainActivity() {
		super(-1, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
/*        showHeader(true);
        setHeaderTitleText(getResources().getString(R.string.home_fragment));*/
		initObservers();
		initDateFormatters();
		initiViews();
		openHomeFragment(false);

		//get server side data
		//if disabled the boolean values are initialized with true
		//and the sync operations will not be executed
		disableSync(true);
		startSyncForHome();

/*
	    //Initializes the Google Maps Android API so that its classes are ready for use
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
	}

	private void initDateFormatters() {

		sdf_Date = new SimpleDateFormat("MM/dd/yyyy", UiEngine.getCurrentAppLocale());
		sdf_Time = new SimpleDateFormat("kk:mm a", UiEngine.getCurrentAppLocale());
		sdf_day_mon = new SimpleDateFormat("dd-MMMM", UiEngine.getCurrentAppLocale());
		sdf_DateTime = new SimpleDateFormat("dd/MM/yyyy kk:mm", UiEngine.getCurrentAppLocale());
		sdf_Source = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss ", UiEngine.getCurrentAppLocale());
		sdf_Source_News = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.ENGLISH);
		sdf_Source_EService = new SimpleDateFormat("dd MMMM yyyy", UiEngine.getCurrentAppLocale());


	}

	//set all flags that are used in sync operations
	//true = don't use the flags and don't start sync operations
	private void disableSync(boolean isDisabled)
	{
		  isNewsLoadingFinished = isDisabled;
		  isEventsLoadingFinished= isDisabled;
		  isPhotoGalleryLoadingFinished= isDisabled;
		  isVideoGalleryLoadingFinished= isDisabled;
		  isLocationLoadingFinished= isDisabled;
		  isOrganizerLoadingFinished= isDisabled;
		  isEservicesRequestLoadingFinished= isDisabled;

		if(!isDisabled)
			startSync();
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

	public void startSyncForHome()
	{
		isNewsLoadingFinished = false;
		isEventsLoadingFinished= false;

		/*News UnFiltered News List*/
		getNewsList();

		/*Events UnFiltered  List*/
		getEventsList();

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
		// get events of this month only


		/*	Calendar cal = Calendar.getInstance();
		EventsManager.getInstance().refreshEventsOfThisMonth(this, cal, this, false);*/

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
        try {
            Engine.setApplicationLanguage(this, Engine.getAppConfiguration().getLanguage());
        }catch (Exception ex){}
//gallery
		if (((int) requestId == RequestIds.PHOTO_GALLERY_REQUEST_ID ||
				(int) requestId == RequestIds.VIDEO_GALLERY_REQUEST_ID) &&
				/*galleryRequstObserver!=null  && */galleryRequstObserverList.size() > 0)
		{
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
				(int) requestId == RequestIds.EVENTS_CITY_REQUEST_ID||
				(int) requestId == RequestIds.EVENTS_LIST_OF_MONTH_REQUEST_ID

		)
				/*&& eventsRequstObserver!=null*/
				&& eventsRequstObserverList.size() > 0) {
			if ((int) requestId == RequestIds.EVENTS_LIST_REQUEST_ID)
				isEventsLoadingFinished = true;
			if((int) requestId == RequestIds.EVENTS_LIST_OF_MONTH_REQUEST_ID)
			{
				isEventsLoadingFinished = true;
				EventsManager.getInstance().setMonthEventsList((ArrayList<EventItem>) resulObject);
			}
			if(isEventsLoadingFinished)
			{
				for (RequestObserver iterator : eventsRequstObserverList)
					iterator.handleRequestFinished(requestId, error, resulObject);
			}

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
				&& locationRequstObserverList.size() > 0)
		{
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
		else if ( ( (int) requestId == RequestIds.E_SERVICES_REQUESTS_LIST_REQUEST_ID||
					(int) requestId == RequestIds.E_SERVICES_STATISTICS_LIST_REQUEST_ID )
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

	public static String reFormatDate(String oldDate, SimpleDateFormat currentSDF, SimpleDateFormat newSDF) {
		String newString = null;
		try {
			Date date = currentSDF.parse(oldDate);
			newString = newSDF.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newString;
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

	public static String reFormatNewsDate(String oldDate, SimpleDateFormat sdf)
	{
        if(Utilities.isNullString(oldDate))
            return "";
		String newString = null;
		String elapsed=null;
		try {
			Date date = sdf_Source_News.parse(oldDate);
//			DateTime dateTime = new DateTime(1978, 3, 26, 12, 35, 0, 0);
			if(date==null){
				return null;
			}
			JodaTimeAndroid.init(CTApplication.getContext());
			DateTime dateTime = new DateTime(date);
			DateTime now = new DateTime();
			Period period = new Period(dateTime, now);
			String yearsStr= CTApplication.getContext().getResources().getString(R.string.years);
			String monthsStr= CTApplication.getContext().getResources().getString(R.string.months);
			String weeksStr= CTApplication.getContext().getResources().getString(R.string.weeks);
			String daysStr= CTApplication.getContext().getResources().getString(R.string.days);
			String hoursStr= CTApplication.getContext().getResources().getString(R.string.hours);
			String minutesStr= CTApplication.getContext().getResources().getString(R.string.minutes);
			String ago= CTApplication.getContext().getResources().getString(R.string.ago);

			PeriodFormatter formatter;
			formatter = new PeriodFormatterBuilder()
					.appendYears().appendSuffix(" year, ", " "+yearsStr+", ")
					.appendMonths().appendSuffix(" month, ", " "+monthsStr+", ")
					.appendWeeks().appendSuffix(" week, ", " "+weeksStr+", ")
					.appendDays().appendSuffix(" day, ", " "+daysStr+", ")
					.appendHours().appendSuffix(" hour, ", " "+hoursStr+", ")
					.appendMinutes().appendSuffix(" minute, ", " "+minutesStr+", ")
					/*.appendSeconds().appendSuffix(" second", " seconds")*/
					.printZeroNever()
					.toFormatter();

			String periodValueStr=formatter.print(period);
			if(!Utilities.isNullString(periodValueStr)&& periodValueStr.contains(","))
			{
				periodValueStr=periodValueStr.substring(0,periodValueStr.indexOf(","));
			}
			if(UiEngine.isAppLanguageArabic(CTApplication.getContext()))
			{
				elapsed = ago+" "+periodValueStr;
			}
			else{
//				elapsed = formatter.print(period)+" "+ago;
				elapsed = periodValueStr+" "+ago;
			}

			newString = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return elapsed;
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



}
