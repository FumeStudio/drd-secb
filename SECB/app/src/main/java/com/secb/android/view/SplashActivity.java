package com.secb.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.secb.android.R;

public class SplashActivity extends SECBBaseActivity {

    Handler redirectHandler;
    Runnable redirectRunnable;
    private static final int TIME_OUT = 2 * 1000;// in Millis

    public SplashActivity() {
        super(R.layout.splash, false);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
        // Wait X seconds and redirect
        redirectHandler = new Handler();
        redirectRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                redirect();
            }
        };
        redirectHandler.postDelayed(redirectRunnable, TIME_OUT);

    }

    /*
	 * Redirect to home page
	 */
    private void redirect()
    {
//		// User logged in -> open home
//		if (UserManager.getInstance().isUserLoggedIn())
//			startActivity(new Intent(SplashActivity.this, MainActivity.class)); // TestActivity
//
//      User not logged in -> open Login
//      else
        startActivity(new Intent(SplashActivity.this, LoginActivity.class)); // TestActivity
        overridePendingTransition(-1, -1);
        finish();
    }
}
