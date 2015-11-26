package com.secb.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

public class OrganizersDetailsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener {
    OrganizerItem organizerItem;

    ImageView imgv_organizerImg;
    TextView txtv_organizerName;
    TextView txtv_organizerDescription;
    TextView txtv_organizer_address_value;
    TextView txtv_organizer_phone_value;
    TextView txtv_organizer_email_value;
    TextView txtv_organizer_website_value;
    View view;


    public static OrganizersDetailsFragment newInstance(OrganizerItem organizerItem) {
        OrganizersDetailsFragment fragment = new OrganizersDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("locationItem", organizerItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.organizer_details));
        ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
        ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.organizer_details, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            organizerItem = (OrganizerItem) bundle.getSerializable("locationItem");
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
    private void applyFonts() {
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.txtv_news_details_newTitle)), UiEngine.Fonts.BDCN);
        UiEngine.applyFontsForAll(CTApplication.getContext(), view, UiEngine.Fonts.HVAR);
    }

    private void goBack() {
        String backStateName = this.getClass().getName();
//     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
	    if(Utilities.isTablet(getActivity()))
		    (getActivity()).finish();
        else
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
            case R.id.txtv_organizer_phone_value:
	            callOrganizerPhone();
                break;
            case R.id.txtv_organizer_website_value:
                openOrganizerWebsite();
                break;
            case R.id.txtv_organizer_email_value:
	            sendOrganizerEmail();
                break;

            default:
                break;
        }
    }
	private void sendOrganizerEmail() {
		if (organizerItem != null && !Utilities.isNullString(organizerItem.OrganizerEmail))
			Utilities.openEmail(getActivity(), organizerItem.OrganizerEmail, "", "");
	}

	private void callOrganizerPhone() {
		if (organizerItem != null && !Utilities.isNullString(organizerItem.OrganizerPhone))
			Utilities.callPhoneNumber(getActivity(), organizerItem.OrganizerPhone);
	}
    private void openOrganizerWebsite() {
        if (organizerItem != null && !Utilities.isNullString(organizerItem.OrganizerWebAddress))
            Utilities.openUrlInBrowser(getActivity(), organizerItem.OrganizerWebAddress);
    }

    private void initViews(View view) {
        imgv_organizerImg = (ImageView) view.findViewById(R.id.imgv_organizerImg);
        txtv_organizerName = (TextView) view.findViewById(R.id.txtv_organizerName);
        txtv_organizerDescription = (TextView) view.findViewById(R.id.txtv_organizerDescription);
        txtv_organizer_address_value = (TextView) view.findViewById(R.id.txtv_organizer_address_value);
        txtv_organizer_phone_value = (TextView) view.findViewById(R.id.txtv_organizer_phone_value);
        txtv_organizer_email_value = (TextView) view.findViewById(R.id.txtv_organizer_email_value);
        txtv_organizer_website_value = (TextView) view.findViewById(R.id.txtv_organizer_website_value);

	    txtv_organizer_phone_value.setOnClickListener(this);
	    txtv_organizer_email_value.setOnClickListener(this);
        txtv_organizer_website_value.setOnClickListener(this);
    }

    private void bindViews() {
        if (this.organizerItem != null) {
            if (!Utilities.isNullString(organizerItem.OrganizerImage)) {
                Glide.with(getActivity())
                        .load(organizerItem.OrganizerImage)
                        .centerCrop()
                        .placeholder(R.drawable.organizer_place_holder)
                        .into(imgv_organizerImg);
            } else
                imgv_organizerImg.setImageResource(R.drawable.organizer_place_holder);

            txtv_organizerName.setText(organizerItem.OrganizerName);
            txtv_organizer_address_value.setText(organizerItem.OrganizerAddressDescription);
            txtv_organizer_phone_value.setText(organizerItem.OrganizerPhone);
            txtv_organizer_email_value.setText(organizerItem.OrganizerEmail);
            txtv_organizer_website_value.setText(organizerItem.OrganizerWebAddress);
        }
    }


}
