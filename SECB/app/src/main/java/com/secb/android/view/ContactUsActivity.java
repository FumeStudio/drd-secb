package com.secb.android.view;

import android.os.Bundle;

import com.secb.android.R;
import com.secb.android.view.fragments.ContactUsFragment;

public class ContactUsActivity extends SECBBaseActivity{
	private static final String TAG = "NewsActivity";
//this activity for news list fragment , news details fragment

	public ContactUsActivity() {
		super(R.layout.contact_us_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		openAboutUsFragment();

	}


	public void openAboutUsFragment() {
		ContactUsFragment fragment = ContactUsFragment.newInstance();
		inflateFragmentInsideLayout(fragment,R.id.list_container,false);
	}
}
