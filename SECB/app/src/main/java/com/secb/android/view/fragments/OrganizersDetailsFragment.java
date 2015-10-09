package com.secb.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;

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


    public static OrganizersDetailsFragment newInstance(OrganizerItem organizerItem)
    {
        OrganizersDetailsFragment fragment = new OrganizersDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("locationItem",organizerItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.organizer_details));
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.organizer_details, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            organizerItem = (OrganizerItem)bundle.getSerializable("locationItem");
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
            case R.id.txtv_viewAllNews:
                ((MainActivity) getActivity()).openNewsListFragment();
            default:
                break;
        }
    }

    private void initViews(View view)
    {
        imgv_organizerImg = (ImageView) view.findViewById(R.id.imgv_organizerImg);
        txtv_organizerName = (TextView) view.findViewById(R.id.txtv_organizerName);
        txtv_organizerDescription = (TextView) view.findViewById(R.id.txtv_organizerDescription);
        txtv_organizer_address_value = (TextView) view.findViewById(R.id.txtv_organizer_address_value);
        txtv_organizer_phone_value = (TextView) view.findViewById(R.id.txtv_organizer_phone_value);
        txtv_organizer_email_value = (TextView) view.findViewById(R.id.txtv_organizer_email_value);
        txtv_organizer_website_value = (TextView) view.findViewById(R.id.txtv_organizer_website_value);
    }

    private void bindViews()
    {
        if(this.organizerItem !=null)
        {
            imgv_organizerImg.setImageBitmap(organizerItem.OraganizerItemImage);
            txtv_organizerName.setText(organizerItem.OraganizerItemTitle);
            txtv_organizer_address_value.setText(organizerItem.OraganizerItemAddress);
            txtv_organizer_phone_value.setText(organizerItem.OraganizerItemPhone);
            txtv_organizer_email_value.setText(organizerItem.OraganizerItemEmail);
            txtv_organizer_website_value.setText(organizerItem.OraganizerItemWebsite);
        }
    }


}
