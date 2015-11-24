package com.secb.android.view;

import android.os.Bundle;

import com.secb.android.R;
import com.secb.android.view.fragments.AboutUsFragment;

public class AboutUsActivity extends SECBBaseActivity{
	private static final String TAG = "NewsActivity";
//this activity for news list fragment , news details fragment

	public AboutUsActivity() {
		super(R.layout.about_us_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		openAboutUsFragment();

	}


	public void openAboutUsFragment() {
		AboutUsFragment fragment = AboutUsFragment.newInstance();
		inflateFragmentInsideLayout(fragment,R.id.list_container,false);
	}
}
