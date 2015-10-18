package com.secb.android.view.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.secb.android.R;
import com.secb.android.model.EventItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.helper.MapsHelper;

public class EventDetailsFragment  extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener
        ,GoogleMap.OnMarkerClickListener,OnMapReadyCallback
{

    private View view;
    private EventItem eventItem;
    private ImageView imgv_event_details_img;
    private TextView txtv_event_details_eventTitle;
    private TextView txtv_event_details_eventDate;
    private TextView txtv_event_details_eventPlace;
    private TextView txtv_event_details_eventCategory;
    private TextView txtv_event_details_eventDuration;
    private TextView txtv_event_details_eventRepeated;
    private TextView txtv_event_details_eventBody;

    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;

    public static EventDetailsFragment newInstance(EventItem eventItem)
    {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("eventsItem", eventItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.event_details));
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
        if(supportMapFragment!=null)
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.event_details, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            eventItem = (EventItem)bundle.getSerializable("eventsItem");
        }
        initViews(view);
        initMap();
        bindViews();
        return view;
    }


    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts()
    {
        if(txtv_event_details_eventTitle!=null)
        {
            UiEngine.applyCustomFont(txtv_event_details_eventTitle, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_details_eventDate!=null)
        {
            UiEngine.applyCustomFont(txtv_event_details_eventDate, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_details_eventPlace!=null)
        {
            UiEngine.applyCustomFont(txtv_event_details_eventPlace, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_details_eventCategory!=null)
        {
            UiEngine.applyCustomFont(txtv_event_details_eventCategory, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_details_eventDuration!=null)
        {
            UiEngine.applyCustomFont(txtv_event_details_eventDuration, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_details_eventRepeated!=null)
        {
            UiEngine.applyCustomFont(txtv_event_details_eventRepeated, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_details_eventBody!=null)
        {
            UiEngine.applyCustomFont(txtv_event_details_eventBody, UiEngine.Fonts.HVAR);
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
            case R.id.imageViewBackHeader:
                onBack();
                break;

            default:
                break;
        }
    }

    private void initViews(View view)
    {
        imgv_event_details_img = (ImageView) view.findViewById(R.id.imgv_eventImg);
        txtv_event_details_eventTitle = (TextView) view.findViewById(R.id.txtv_eventTitle);
        txtv_event_details_eventDate = (TextView) view.findViewById(R.id.txtv_event_timeValue);
        txtv_event_details_eventPlace = (TextView) view.findViewById(R.id.txtv_event_placeValue);
        txtv_event_details_eventCategory = (TextView) view.findViewById(R.id.txtv_event_categoryValue);
        txtv_event_details_eventDuration = (TextView) view.findViewById(R.id.txtv_event_durationValue);
        txtv_event_details_eventRepeated = (TextView) view.findViewById(R.id.txtv_event_repeatedValue);
        txtv_event_details_eventBody = (TextView) view.findViewById(R.id.event_details_body);

/*        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapFrame);
        if (fragment == null)
        {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapFrame, fragment).commit();
        }*/

    }

    public boolean initMap()
    {
        if (googleMap == null)
        {
            supportMapFragment = ((SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map));

            if(supportMapFragment!=null)
                supportMapFragment.getMapAsync(this);
        }
        return (googleMap != null);
    }


    private void bindViews()
    {
        if(this.eventItem !=null)
        {
            imgv_event_details_img.setImageBitmap(eventItem.eventItemImage);
            txtv_event_details_eventTitle.setText(eventItem.eventItemTitle);
            txtv_event_details_eventDate.setText(eventItem.eventItemTime);
            txtv_event_details_eventPlace.setText(eventItem.eventItemLocation);
            txtv_event_details_eventCategory.setText(eventItem.eventItemCategory);
            txtv_event_details_eventDuration.setText(eventItem.eventItemDuration);
            txtv_event_details_eventRepeated.setText(eventItem.eventItemRepeating);
            txtv_event_details_eventBody.setText(eventItem.eventItemDescription);


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
        this.googleMap=googleMap;
        putEventOnMap();
    }

    private void putEventOnMap()
    {
        if(this.eventItem !=null&& googleMap!=null)
        {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_card_place_icon);
            LatLng locationOnMap = new LatLng(this.eventItem.eventItemLatitude,this.eventItem.eventItemLongitude);
            Marker marker = MapsHelper.addMarker(googleMap,locationOnMap, bitmap);
            MapsHelper.setMapCenter(true,googleMap,locationOnMap);

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
