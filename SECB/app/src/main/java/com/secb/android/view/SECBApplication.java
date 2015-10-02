package com.secb.android.view;

import com.secb.android.controller.manager.Engine;

import net.comptoirs.android.common.view.CTApplication;

public class SECBApplication extends CTApplication
{
	@Override
	public void onCreate()
	{
	  super.onCreate();
	  
	  Engine.initialize(this);
		Engine.validateCachedData(this);
		Engine.setApplicationLanguage(this, Engine.getAppConfiguration().getLanguage());	
		UiEngine.initialize(getApplicationContext());
	}
}
