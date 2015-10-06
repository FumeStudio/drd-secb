package com.secb.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.menu.MenuItem;

public class TestFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener
{
	View view;

	
	public static TestFragment newInstance()
	{
		TestFragment fragment = new TestFragment();
		return fragment;
	}
	
	@Override
	public void onResume()
	{
	  super.onResume();
		((SECBBaseActivity) getActivity()).addBackObserver(this);
//	  ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.test));
	  SECBBaseActivity.setMenuItemSelected(MenuItem.MENU_HOME);
	}
	
	@Override
	public void onPause()
	{
	  super.onPause();
	  ((SECBBaseActivity) getActivity()).removeBackObserver(this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (view != null)
		{
			ViewParent oldParent = view.getParent();
			if (oldParent != container)
			{
				((ViewGroup) oldParent).removeView(view);
			}
		}
		else
		{
			view = LayoutInflater.from(getActivity()).inflate(R.layout.home, container, false);
			
			handleButtonsEvents();
			applyFonts();
		}
		return view;
	}
	
	private void handleButtonsEvents()
  {
  }
	
	/*
	 * Apply Fonts
	 */
	private void applyFonts()
  {
		// TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
  }
	
	private void goBack()
  {
		((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
  }
	
	// ////////////////////////////////////////////////////////////
	
	@Override
  public void onBack()
  {
		goBack();
  }

	@Override
  public void onClick(View v)
  {
	  switch (v.getId())
    {
			default:
				break;
		}
  }
}
