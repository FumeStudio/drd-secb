package com.secb.android.view.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.secb.android.R;
import com.secb.android.model.EventItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.helper.MapsHelper;
import net.comptoirs.android.common.helper.Utilities;

public class EventDetailsFragment  extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener
        ,GoogleMap.OnMarkerClickListener,OnMapReadyCallback
{

    private static View view;
    private EventItem eventItem;
    private ImageView imgv_event_details_img;
    private TextView txtv_event_details_eventTitle;
    private TextView txtv_event_details_eventDate;
    private TextView txtv_event_details_eventPlace;
    private TextView txtv_event_details_eventCategory;
    private TextView txtv_event_details_eventDuration;
    private TextView txtv_event_details_eventRepeated;
    private TextView txtv_event_details_eventBody;
	private LinearLayout layout_map_container;


	private TextView txtv_eventImgDate_day;
	private TextView txtv_eventImgDate_month;


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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	    if (supportMapFragment != null)
		    getFragmentManager().beginTransaction().remove(supportMapFragment).commit();

    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(supportMapFragment!=null)
		{
			try {
                getFragmentManager().popBackStack("MAP",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().remove(supportMapFragment).commit();
            } catch(Exception ex) {ex.printStackTrace();}
		}
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null) {
                ((ViewGroup) oldParent).removeView(view);
            }
        }
        else
        {
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
        bindViews();

	    if(Utilities.isGooglePlayServicesInstalled(getActivity()))
	    {
		    initMap();
	    }
	    else
	    {
		    layout_map_container.setVisibility(View.GONE);
	    }
        return view;
    }


    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts()
    {

	    View body_view = view.findViewById(R.id.layout_txt_map_container);
	    View header_view = view.findViewById(R.id.layout_header);
	    UiEngine.applyFontsForAll(getActivity(),body_view, UiEngine.Fonts.HVAR);
	    UiEngine.applyFontsForAll(getActivity(),header_view, UiEngine.Fonts.HVAR);

	    if(header_view.findViewById(R.id.txtv_eventImgDate_day)!=null)
	    {
		    UiEngine.applyCustomFont(header_view.findViewById(R.id.txtv_eventImgDate_day), UiEngine.Fonts.HVAR_BOLD);
	    }
        /*if(txtv_event_details_eventTitle!=null)
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
	    if(txtv_eventImgDate_day!=null)
	    {
		    UiEngine.applyCustomFont(txtv_eventImgDate_day, UiEngine.Fonts.HVAR_BOLD);
	    }
	    if(txtv_eventImgDate_month!=null)
	    {
		    UiEngine.applyCustomFont(txtv_eventImgDate_month, UiEngine.Fonts.HVAR);
	    }*/
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

	    layout_map_container = (LinearLayout) view.findViewById(R.id.map_container);
	    txtv_eventImgDate_day = (TextView) view.findViewById(R.id.txtv_eventImgDate_day);
	    txtv_eventImgDate_month = (TextView) view.findViewById(R.id.txtv_eventImgDate_month);

	    /*if(Utilities.isGooglePlayServicesInstalled(getActivity()))
	    {
		    initMap();
	    }
	    else
	    {
		    layout_map_container.setVisibility(View.GONE);
	    }*/

    }

    public boolean initMap()
    {
	    layout_map_container.setVisibility(View.VISIBLE);
	    FragmentManager fm = getChildFragmentManager();
	    supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.mapFrame);
	    if (supportMapFragment == null)
	    {
		    supportMapFragment = SupportMapFragment.newInstance();
		    fm.beginTransaction().replace(R.id.mapFrame, supportMapFragment).addToBackStack("MAP").commit();

		    supportMapFragment.getMapAsync(this);
	    }
	    else
		    supportMapFragment.getMapAsync(this);

	   /* if (googleMap == null) {
//	    supportMapFragment = ((SupportMapFragment) getFragmentManager()
//                    .findFragmentById(R.id.events_map));
		    supportMapFragment = ((SupportMapFragment) getFragmentManager()
				    .findFragmentById(R.id.map));


		    if (supportMapFragment != null)
			    supportMapFragment.getMapAsync(this);
	    }*/
	    return (googleMap != null);
    }


    private void bindViews()
    {
        if(this.eventItem !=null)
        {

	        String evdateStr= MainActivity.reFormatDate(eventItem.EventDate,MainActivity.sdf_day_mon);
	        if(!Utilities.isNullString(evdateStr)){
		        String[] dayMonthArr = evdateStr.split("-");
		        txtv_eventImgDate_day.setText(dayMonthArr[0]);
		        txtv_eventImgDate_month.setText(dayMonthArr[1].toUpperCase());
	        }

            imgv_event_details_img.setImageResource(R.drawable.events_image_place_holder);
            txtv_event_details_eventTitle.setText(eventItem.Title);

	         evdateStr = MainActivity.reFormatDate(eventItem.EventDate,MainActivity.sdf_Time);
	        txtv_event_details_eventDate.setText(evdateStr);
            txtv_event_details_eventPlace.setText(eventItem.EventSiteCity);
            txtv_event_details_eventCategory.setText(eventItem.EventCategory);
	        if(Boolean.valueOf(eventItem.IsAllDayEvent))
	        {
		        txtv_event_details_eventDuration.setText(getResources().getString(R.string.event_all_day));
		        txtv_event_details_eventDuration.setVisibility(View.VISIBLE);
	        }
	        else
	        {
		        txtv_event_details_eventDuration.setVisibility(View.GONE);
	        }

	        if(Boolean.valueOf(eventItem.IsRecurrence))
	        {
		        txtv_event_details_eventRepeated.setText(getResources().getString(R.string.event_repeated));
		        txtv_event_details_eventRepeated.setVisibility(View.VISIBLE);
	        }
	        else
	        {
		        txtv_event_details_eventRepeated.setVisibility(View.GONE);
	        }

            txtv_event_details_eventBody.setText(eventItem.Description);
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
//        this.googleMap=googleMap;
//        putEventOnMap();

	    this.googleMap = googleMap;
	    putEventOnMap();

    }

    private void putEventOnMap()
    {
        if(this.eventItem !=null&& googleMap!=null)
        {
	        LatLng locationOnMap =null;

//          locationOnMap = new LatLng(30.0882739,31.3146007);
//	        eventItem.eventItemLatitude=30.0882739;
//	        eventItem.eventItemLongitude=31.3146007;
//	        locationOnMap = new LatLng(this.eventItem.eventItemLatitude,this.eventItem.eventItemLongitude);

	        locationOnMap = Utilities.getLatLngFromString(eventItem.SiteonMap);
	        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event_card_place_icon);

	        if(locationOnMap!=null){
		        Marker marker = MapsHelper.addMarker(googleMap,locationOnMap, bitmap);;
		        MapsHelper.zoomToSingleMarker(locationOnMap, googleMap ,11,true);
	        }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
