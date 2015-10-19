package com.secb.android.controller.backend;

public class ServerKeys
{
	public static final String STATUS_CODE = "statusCode";
	public static final String STATUS_MESSAGE = "statusMessage";

	/*Login*/
	public static final String LOGIN_URL = "http://secb.linkdev.com/_vti_bin/authentication.asmx";
	public static final String LOGEN_KEY_SOAP_ENVELOPE = "soap:Envelope";
	public static final String LOGEN_KEY_SOAP_BODY = "soap:Body";
	public static final String LOGEN_KEY_COOKIE = "CookieName";
	public static final String LOGEN_KEY_LOGIN_RESULT = "LoginResult";
	public static final String LOGEN_KEY_LOGIN = "Login";
	public static final String LOGEN_KEY_USER_NAME = "username";
	public static final String LOGEN_KEY_PASSWORD = "password";

	/*Photo Gallery*/
	public static final String PHOTO_URL ="http://secb.linkdev.com/webapi/api/photos/getphotos" ;
//			"?lang=ar&pageSize=100&pageIndex=0";
}
