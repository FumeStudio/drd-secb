package net.comptoirs.android.common.view;

import android.app.Application;
import android.content.Context;

public class CTApplication extends Application
{
	private static Context context = null;
	private static CTApplication application = null;
	
	@Override
	public void onCreate(){
		super.onCreate();

		context = getApplicationContext();
		
		application = this;
	}

	public static Context getContext(){
		return context;
	}
	
	static public CTApplication getApplication(){
		return application;
	}
	
}
