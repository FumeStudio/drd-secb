package net.comptoirs.android.common.controller;

public interface AppProperties
{
	// Main pref file name
	public String getAppProfileName();
	
	// User pref file name
	public String getUserPrefFileName();
	// User Object key
	public String getUserPrefKey();
	// Settings object key
	public String getSettingsPrefKey();
	// Clininc key
	public String getClinicPrefKey();
	// Remember Me key
	public String getRememberMePrefKey();
}
