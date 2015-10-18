package com.secb.android.view;

import com.secb.android.controller.manager.Engine;

import net.comptoirs.android.common.view.CTApplication;

import java.util.Locale;

public class SECBApplication extends CTApplication {
    private Locale locale = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Engine.initialize(this);
        Engine.validateCachedData(this);
        Engine.setApplicationLanguage(this, Engine.getAppConfiguration().getLanguage());
        UiEngine.initialize(getApplicationContext());
    }
/*

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);


        String savedLanguage = UiEngine.getCurrentAppLanguage(this);
        if(!Utilities.isNullString(savedLanguage))
        {
            */
/*newConfig.orientation ==-1*//*

//          made this to prevent listening to screen orientation changes

            if(! newConfig.locale.getLanguage().equalsIgnoreCase(savedLanguage)){
                Intent i = new Intent(this, LanguageActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(i);
            }
        }


        */
/*if(!Utilities.isNullString(savedLanguage))
            locale=new Locale(savedLanguage);
        else
            locale=new Locale(UiEngine.getCurrentDeviceLanguage(this));

        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }*//*

    }
*/

}
