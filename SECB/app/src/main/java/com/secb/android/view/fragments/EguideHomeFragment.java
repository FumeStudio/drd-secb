package com.secb.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

import com.secb.android.R;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

public class EguideHomeFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener {

    Button btn_locationEguid,btn_organizersEguide;
    View view;

    public static EguideHomeFragment newInstance( )
    {
        EguideHomeFragment fragment = new EguideHomeFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.eguide));
 //        SECBBaseActivity.setMenuItemSelected(MenuItem.MENU_HOME);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null) {
                ((ViewGroup) oldParent).removeView(view);
            }
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.eguide_home, container, false);
            handleButtonsEvents();

        }
        initViews(view);
        applyFonts();
        return view;
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts()
    {
        if(btn_locationEguid!=null)
        {
            UiEngine.applyCustomFont(btn_locationEguid, UiEngine.Fonts.HVAR);
        }
        if(btn_organizersEguide!=null)
        {
            UiEngine.applyCustomFont(btn_organizersEguide, UiEngine.Fonts.HVAR);
        }
    }

    private void goBack()
    {
        String backStateName = this.getClass().getName();
//     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(backStateName);
    }

    // ////////////////////////////////////////////////////////////

    @Override
    public void onBack() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location_eguide:
                ((MainActivity) getActivity()).openEguideLocationFragment();
                break;
            case R.id.btn_organizers_eguide:
                ((MainActivity) getActivity()).openEguideOrganizersFragment();
                break;
            default:
                break;
        }
    }

    private void initViews(View view)
    {
        btn_locationEguid = (Button) view.findViewById(R.id.btn_location_eguide);
        btn_organizersEguide = (Button) view.findViewById(R.id.btn_organizers_eguide);

        btn_locationEguid.setOnClickListener(this);
        btn_organizersEguide.setOnClickListener(this);
    }



}
