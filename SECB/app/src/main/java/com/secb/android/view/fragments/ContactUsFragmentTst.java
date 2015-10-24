package com.secb.android.view.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.secb.android.R;
import com.secb.android.model.CompanyProfile;
import com.secb.android.model.EventItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.helper.MapsHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactUsFragmentTst extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener, OnMapReadyCallback {


    View view;
	CompanyProfile companyProfile;
    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;
	private EventItem eventItem;


	public static ContactUsFragmentTst newInstance(EventItem eventItem) {
        ContactUsFragmentTst fragment = new ContactUsFragmentTst();
	    Bundle bundle = new Bundle();
	    bundle.putSerializable("eventsItem", eventItem);
	    fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.contactus));
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
        ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();

    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
        if (supportMapFragment != null)
            getFragmentManager().beginTransaction().remove(supportMapFragment).commit();

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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.maptest, container, false);
            handleButtonsEvents();
            applyFonts(view);
        }


	    Bundle bundle = getArguments();
	    if(bundle!=null)
	    {
		    eventItem = (EventItem)bundle.getSerializable("eventsItem");
	    }

        initViews(view);
        getCompanyProfile();
	    bindViews();
        applyFonts(view);
        return view;
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts(View view) {
        UiEngine.applyFontsForAll(getActivity(), view, UiEngine.Fonts.HVAR);
    }

    private void goBack() {
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
            case R.id.imageViewBackHeader:
                onBack();
                break;
            default:
                break;
        }
    }

    private void initViews(View view) {
	    initMap();
    }

    public boolean initMap() {
        if (googleMap == null) {
            supportMapFragment = ((SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map));

            if (supportMapFragment != null)
                supportMapFragment.getMapAsync(this);
        }
        return (googleMap != null);
    }


    public CompanyProfile getCompanyProfile() {

        companyProfile = new CompanyProfile();
        companyProfile.location = getResources().getString(R.string.secb_location_address);
        companyProfile.email = getResources().getString(R.string.secb_email_address);
        companyProfile.phone = getResources().getString(R.string.secb_phone_number);
        companyProfile.fax = getResources().getString(R.string.secb_fax_number);
        companyProfile.facebook = getResources().getString(R.string.secb_facebook);
        companyProfile.twitter = getResources().getString(R.string.secb_twitter);
        companyProfile.google = getResources().getString(R.string.secb_google);
        companyProfile.linkedin = getResources().getString(R.string.secb_linkedIn);
        companyProfile.locationOnMap = new LatLng(30.0882739, 31.3146007);

        return companyProfile;
    }

    private void bindViews() {
    }



    public void putCompanyOnMap() {
        if (companyProfile != null && companyProfile.locationOnMap != null && googleMap != null) {
            List<LatLng> list = new ArrayList<>();
            list.add(companyProfile.locationOnMap);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.secb_map_marker);
            Marker marker = MapsHelper.addMarker(googleMap, companyProfile.locationOnMap, bitmap);
            MapsHelper.setMapCenter(true, googleMap, companyProfile.locationOnMap);
//                MapsHelper.zoomToAllMarkers(list, googleMap, 5, 15);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(supportMapFragment!=null)
//            getFragmentManager().beginTransaction().remove(supportMapFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        putCompanyOnMap();
    }

}
