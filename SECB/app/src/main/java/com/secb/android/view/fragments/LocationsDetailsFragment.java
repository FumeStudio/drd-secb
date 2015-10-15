package com.secb.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.LocationItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

public class LocationsDetailsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener {
    LocationItem locationItem;

    ImageView imgv_locationImg;
    TextView txtv_location_nameTitle;
    TextView txtv_location_nameValue;
    TextView txtv_location_typeTitle;
    TextView txtv_location_typeValue;
    TextView txtv_location_description;
    TextView txtv_location_capacityValue;
    TextView txtv_location_spaceValue;
    TextView txtv_location_roomTypeValue;
    TextView txtv_location_roomCapacityValue;
    TextView txtv_location_roomSpaceValue;
    TextView txtv_location_roomCountValue;
    TextView txtv_location_address_value;
    TextView txtv_location_phone_value;
    TextView txtv_location_email_value;
    View view;


    public static LocationsDetailsFragment newInstance(LocationItem organizerItem)
    {
        LocationsDetailsFragment fragment = new LocationsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("locationItem",organizerItem);
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
        if(bundle!=null)
        {
            locationItem = (LocationItem)bundle.getSerializable("locationItem");
        }
        initViews(view);
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
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.txtv_news_details_newTitle)), UiEngine.Fonts.BDCN);
        UiEngine.applyFontsForAll(getActivity(),view,UiEngine.Fonts.HVAR);
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
    }



    private void bindViews()
    {
        if(this.locationItem !=null)
        {
            imgv_locationImg.setImageBitmap(locationItem.LoccationItemImage);
            txtv_location_nameValue.setText(locationItem.LoccationItemTitle);
            txtv_location_typeValue.setText(locationItem.LoccationItemType);
            txtv_location_description.setText(locationItem.LoccationItemDescription);
            txtv_location_capacityValue.setText(locationItem.LoccationItemCapacity+"");
            txtv_location_spaceValue.setText(locationItem.LoccationItemSpace+"");
            txtv_location_roomTypeValue.setText(locationItem.LoccationItemRoomType+"");
            txtv_location_roomCapacityValue.setText(locationItem.LoccationItemRoomCapacity+"");
            txtv_location_roomSpaceValue.setText(locationItem.LoccationItemRoomSpace+"");
            txtv_location_roomCountValue.setText(locationItem.LoccationItemNumberOrRooms+"");
            txtv_location_address_value.setText(locationItem.LoccationItemAddress);
            txtv_location_phone_value.setText(locationItem.LoccationItemPhone);
            txtv_location_email_value.setText(locationItem.LoccationItemEmail);
        }
    }


}
