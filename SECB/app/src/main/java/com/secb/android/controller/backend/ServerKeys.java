package com.secb.android.controller.backend;

public class ServerKeys
{
	// Default page size
	public static final int PAGE_SIZE_DEFAULT = 10;

	public static final String STATUS_CODE = "statusCode";
	public static final String STATUS_MESSAGE = "statusMessage";

	//used in the url encoding
	public static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	/*Login*/
	public static final String LOGIN_URL = "http://secb.linkdev.com/_vti_bin/authentication.asmx";
	public static final String LOGEN_KEY_SOAP_ENVELOPE = "soap:Envelope";
	public static final String LOGEN_KEY_SOAP_BODY = "soap:Body";
	public static final String LOGEN_KEY_COOKIE = "CookieName";
	public static final String LOGEN_KEY_LOGIN_RESULT = "LoginResult";
	public static final String LOGEN_KEY_LOGIN = "Login";
	public static final String LOGEN_KEY_USER_NAME = "username";
	public static final String LOGEN_KEY_PASSWORD = "password";

	public static String PRODUCTION_URL = "http://secb.linkdev.com/";
	public static String DEV_URL = "http://secb.linkdev.com/";
	public static boolean isProduction = true;
	public static String serverUrl = isProduction?PRODUCTION_URL:DEV_URL;

	/*Photo Gallery*/
	public static final String PHOTO_URL =serverUrl+"webapi/api/photos/getphotos" ;

	/*Video Gallery*/
	public static final String VIDEO_URL = serverUrl+"webapi/api/Videos/GetVideos";

	/*News Categories*/
	public static final String NEWS_CATEGORIES_URL = serverUrl+"webapi/api/News/GetNewsCategories";

	/*News List*/
	public static final String NEWS_URL = serverUrl+"webapi/api/News/GetNews";

	/*Events Categories*/
	public static final String EVENTS_CATEGORIES_URL = serverUrl+"webapi/api/events/GetEventsCategories";

	/*Events List*/
	public static final String EVENTS_URL = serverUrl+"webapi/api/events/GetEvents";

	/*Events Cities*/
	public static final String EVENTS_CITIES_URL =serverUrl+ "webapi/api/events/GetCities";

	/*Forget Password*/
	public static final String FORGET_PASSWORD = serverUrl+"webapi/api/user/ForgetPassword";

	/*E-guide Location Types*/
	public static final String EGUIDE_LOCATION_TYPES = serverUrl+"webapi/api/location/GetlocationTypes";

	/*E-guide Location List*/
	public static final String EGUIDE_LOCATION_LIST= serverUrl+"WebApi/api/location/GetLocation";

	/*E-guide Location Details List*/
	public static final String EGUIDE_LOCATION_DETAILS_LIST= serverUrl+"WebApi/api/location/GetLocationByID";

	/*E-guide Organizers List*/
	public static final String EGUIDE_ORGANIZERS_LIST=serverUrl+ "WebApi/api/Organizer/Getorganizer";

	/*E-Services RequestList*/
	public static final String E_Services_REQUESTS_LIST = serverUrl+"WebApi/api/Requests/GetRequests";

	/*E-Services RequestTypes*/
	public static final String E_Services_REQUESTS_TYPES_LIST =serverUrl+ "WebApi/api/Requests/GetRequesttypes";

	/*E-Services WorkSpaceModes*/
	public static final String E_Services_Work_SPACE_MODES_LIST = serverUrl+"WebApi/api/Requests/GetWorkSpaceModes";

	/*E-Services Statistics List*/
	public static final String E_Services_STATISTICS_LIST = serverUrl+"WebApi/api/requests/GetUserTasksStatistics";

	/*Contact Us*/
	public static final String CONTACT_US = serverUrl+"webapi/api/user/ContactUs";


}
