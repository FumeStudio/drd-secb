package com.secb.android.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.controller.backend.E_GuideLocationDetailsListOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EGuideLocationManager;
import com.secb.android.model.LocationItem;
import com.secb.android.model.RoomItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class LocationsDetailsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener, RequestObserver {
    LocationItem bundleLocationItem; // sent with bundle from locationList
    LocationItem locationItem; // returned from requestDetails Operation
    String LocationId;

    ImageView imgv_locationImg;
    TextView txtv_location_nameTitle;
    TextView txtv_location_nameValue;
    TextView txtv_location_typeTitle;
    TextView txtv_location_typeValue;
    TextView txtv_location_description;
    TextView txtv_location_capacityValue;
    TextView txtv_location_spaceValue;
    TextView txtv_location_address_value;
    TextView txtv_location_phone_value;
    TextView txtv_location_email_value;

    TextView txtv_location_roomTypeValue;
    TextView txtv_location_roomCapacityValue;
    TextView txtv_location_roomSpaceValue;
    TextView txtv_location_roomCountValue;
    View view;


//    ImageButton btn_nextRoom, btn_PreviousRoom;
    int currentRoomIndex;
    private TextView txtv_noData;
    private LinearLayout location_details_root;
    private ArrayList<RoomItem> locationRooms;

	private LinearLayout layout_rooms_container;
	View location_room_item;

    public static LocationsDetailsFragment newInstance(LocationItem organizerItem) {
        LocationsDetailsFragment fragment = new LocationsDetailsFragment();
        Bundle bundle = new Bundle();
	    bundle.putSerializable("locationItem", organizerItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.location_details));
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.location_details, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundleLocationItem = (LocationItem) bundle.getSerializable("locationItem");
        }
        if (bundleLocationItem != null)
            LocationId = bundleLocationItem.ID;
        initViews(view);
        getData();
        return view;
    }

    private void getData() {
        if (bundleLocationItem == null)
            return;

        locationItem = EGuideLocationManager.getInstance().getLocationDetails(bundleLocationItem.ID, getActivity());
        if (locationItem != null)
            handleRequestFinished(RequestIds.EGUIDE_LOCATION_DETAILS_REQUEST_ID, null, locationItem);

        else
            startLocationDetailsOperation(true);
    }

    private void startLocationDetailsOperation(boolean showDialog) {
        E_GuideLocationDetailsListOperation operation = new E_GuideLocationDetailsListOperation(RequestIds.EGUIDE_LOCATION_DETAILS_REQUEST_ID,
                showDialog, getActivity(), LocationId, 100, 0);
        operation.addRequsetObserver(this);
        operation.execute();


    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.txtv_news_details_newTitle)), UiEngine.Fonts.BDCN);
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
            case R.id.txtv_location_phone_value:
	            callLocationPhone();
                break;
            case R.id.txtv_location_email_value:
	            sendLocationEmail();
                break;
//            case R.id.btn_nextRoom:
//                getNextRoom();
//                break;
//            case R.id.btn_previousRoom:
//                getPreviousRoom();
//                break;

            default:
                break;

        }
    }




	private void sendLocationEmail() {
		if (locationItem != null && !Utilities.isNullString(locationItem.SiteEmail))
			Utilities.openEmail(getActivity(), locationItem.SiteEmail, "", "");
	}

	private void callLocationPhone() {
		if (locationItem != null && !Utilities.isNullString(locationItem.SitePhone))
			Utilities.callPhoneNumber(getActivity(), locationItem.SitePhone);
	}

    private void initViews(View view) {
        txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
        location_details_root = (LinearLayout) view.findViewById(R.id.location_details_root);

        imgv_locationImg = (ImageView) view.findViewById(R.id.imgv_locationImg);
        txtv_location_nameValue = (TextView) view.findViewById(R.id.txtv_location_nameValue);
        txtv_location_typeValue = (TextView) view.findViewById(R.id.txtv_location_typeValue);
        txtv_location_description = (TextView) view.findViewById(R.id.txtv_location_description);
        txtv_location_capacityValue = (TextView) view.findViewById(R.id.txtv_location_capacityValue);
        txtv_location_spaceValue = (TextView) view.findViewById(R.id.txtv_location_spaceValue);
        txtv_location_roomTypeValue = (TextView) view.findViewById(R.id.txtv_location_roomTypeValue);
        txtv_location_roomCapacityValue = (TextView) view.findViewById(R.id.txtv_location_roomCapacityValue);
        txtv_location_roomSpaceValue = (TextView) view.findViewById(R.id.txtv_location_roomSpaceValue);
        txtv_location_roomCountValue = (TextView) view.findViewById(R.id.txtv_location_roomCountValue);
        txtv_location_address_value = (TextView) view.findViewById(R.id.txtv_location_address_value);
        txtv_location_phone_value = (TextView) view.findViewById(R.id.txtv_location_phone_value);
        txtv_location_email_value = (TextView) view.findViewById(R.id.txtv_location_email_value);

	    location_room_item = view.findViewById(R.id.location_room_item);
	    layout_rooms_container= (LinearLayout) view.findViewById(R.id.layout_rooms_container);

//        btn_nextRoom = (ImageButton) view.findViewById(R.id.btn_nextRoom);
//        btn_PreviousRoom = (ImageButton) view.findViewById(R.id.btn_previousRoom);
//
//        btn_nextRoom.setOnClickListener(this);
//        btn_PreviousRoom.setOnClickListener(this);

	    txtv_location_email_value.setOnClickListener(this);
	    txtv_location_phone_value.setOnClickListener(this);

    }


    private void bindViews() {
        if (this.locationItem != null) {
            location_details_root.setVisibility(View.VISIBLE);
            txtv_noData.setVisibility(View.GONE);
//            imgv_locationImg.setImageBitmap(bundleLocationItem.LoccationItemImage);

            if (!Utilities.isNullString(locationItem.SiteImage)) {
                Glide.with(getActivity())
                        .load(locationItem.SiteImage)
                        .centerCrop()
                        .placeholder(R.drawable.location_image_place_holder)
                        .into(imgv_locationImg);
            } else
                imgv_locationImg.setImageResource(R.drawable.location_image_place_holder);

            txtv_location_nameValue.setText(locationItem.SiteName);
            txtv_location_typeValue.setText(locationItem.SiteType);
            txtv_location_description.setText(locationItem.SiteDescription);
            txtv_location_capacityValue.setText(locationItem.SiteCapacity + "");
            txtv_location_spaceValue.setText(locationItem.SiteArea + "");
	        String address ="";
	        if(!Utilities.isNullString(locationItem.SiteStreet))
		        address =address+locationItem.SiteStreet;
	        if(!Utilities.isNullString(locationItem.SiteDistrict))
		        address =address+","+locationItem.SiteDistrict;
	        if(!Utilities.isNullString(locationItem.SiteCity))
		        address =address+","+locationItem.SiteCity;

            txtv_location_address_value.setText(address);
            txtv_location_phone_value.setText(locationItem.SitePhone);
            txtv_location_email_value.setText(locationItem.SiteEmail);

            if (locationRooms != null & locationRooms.size() > 0)
            {
                if (locationRooms.size() > 1) {
//                    btn_nextRoom.setVisibility(View.VISIBLE);
//                    btn_PreviousRoom.setVisibility(View.VISIBLE);

	                location_room_item.setVisibility(View.GONE);
	                layout_rooms_container.setBackgroundColor(getResources().getColor(R.color.white));
	                for (int currentRoomIndex=0; currentRoomIndex<locationRooms.size();currentRoomIndex++)
	                {
		                final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		                if(inflater!=null){
			               View roomItemView = inflater.inflate(R.layout.location_room_info,null/*layout_rooms_container,false*/);
			                roomItemView.setId(currentRoomIndex);
			                LinearLayout layout = (LinearLayout)roomItemView.findViewById(R.id.layout_room_item);
			                layout.setBackgroundColor(getResources().getColor(R.color.off_white));
			                TextView roomTypeValue = (TextView) roomItemView.findViewById(R.id.txtv_location_roomTypeValue);
			                TextView roomCapacityValue = (TextView) roomItemView.findViewById(R.id.txtv_location_roomCapacityValue);
			                TextView roomSpaceValue = (TextView) roomItemView.findViewById(R.id.txtv_location_roomSpaceValue);
			                TextView roomCountValue = (TextView) roomItemView.findViewById(R.id.txtv_location_roomCountValue);

			                roomTypeValue.setText(locationRooms.get(currentRoomIndex).RoomType + "");
			                roomCapacityValue.setText(locationRooms.get(currentRoomIndex).RoomCapacity + "");
			                roomSpaceValue.setText(locationRooms.get(currentRoomIndex).RoomArea + "");
			                roomCountValue.setText(locationRooms.get(currentRoomIndex).RoomsCount + "");

			                UiEngine.applyFontsForAll(getActivity(),roomItemView, UiEngine.Fonts.HVAR);
			                layout_rooms_container.addView(roomItemView);
		                }
	                }

                }
                else
                {
//                    btn_nextRoom.setVisibility(View.GONE);
//                    btn_PreviousRoom.setVisibility(View.GONE);

	                //first Room Item
	                location_room_item.setVisibility(View.VISIBLE);
	                layout_rooms_container.setBackgroundColor(getResources().getColor(R.color.off_white));
	                txtv_location_roomTypeValue.setText(locationRooms.get(currentRoomIndex).RoomType + "");
	                txtv_location_roomCapacityValue.setText(locationRooms.get(currentRoomIndex).RoomCapacity + "");
	                txtv_location_roomSpaceValue.setText(locationRooms.get(currentRoomIndex).RoomArea + "");
	                txtv_location_roomCountValue.setText(locationRooms.get(currentRoomIndex).RoomsCount + "");
                }

            }
            else {
//                btn_nextRoom.setVisibility(View.GONE);
//                btn_PreviousRoom.setVisibility(View.GONE);
            }


        } else {
            location_details_root.setVisibility(View.GONE);
            txtv_noData.setVisibility(View.VISIBLE);
	        txtv_noData.setText(getString(R.string.details_no_details));
        }
    }

    /*public void getNextRoom() {
        if (locationRooms.size() > 1) {
            btn_PreviousRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_left));
        } else {
            btn_PreviousRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_left_disabled));
        }


        if (currentRoomIndex == locationRooms.size() - 1) {
            btn_nextRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_right_disabled));
            return;
        } else {
            currentRoomIndex++;
            if (currentRoomIndex == locationRooms.size() - 1)
                btn_nextRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_right_disabled));
            else
                btn_nextRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_right));

            bindViews();
        }
    }

    public void getPreviousRoom() {
        if (locationRooms.size() > 1) {
            btn_nextRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_right));
        } else {
            btn_nextRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_right_disabled));
        }


        if (currentRoomIndex == 0) {
            btn_PreviousRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_left_disabled));
            return;
        } else {
            currentRoomIndex--;
            if (currentRoomIndex == 0)
                btn_PreviousRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_left_disabled));
            else
                btn_PreviousRoom.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_to_left));

            bindViews();
        }
    }*/

    public void handleRoomNavigationButtons() {
    }

    @Override
    public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
        if (error == null) {
            if ((int) requestId == RequestIds.EGUIDE_LOCATION_DETAILS_REQUEST_ID && resulObject != null) {
                locationItem = (LocationItem) resulObject;
                locationRooms = (ArrayList<RoomItem>) locationItem.LocationRooms;

            }

        } else {

        }
        bindViews();
    }

    @Override
    public void requestCanceled(Integer requestId, Throwable error) {

    }

    @Override
    public void updateStatus(Integer requestId, String statusMsg) {

    }
}
