package com.secb.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.view.CTApplication;

public class AboutUsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener {


    ImageView imgv_aboutusImg;
    TextView txtv_aboutusVisionTitle;
    TextView txtv_aboutusVisionValue;
    TextView txtv_aboutusMissionTitle;
    TextView txtv_aboutusMissionValue;
    View view;


    public static AboutUsFragment newInstance()
    {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.aboutus));
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.about_us, container, false);
            handleButtonsEvents();
            applyFonts();
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
        UiEngine.applyFontsForAll(CTApplication.getContext(), view, UiEngine.Fonts.HVAR);
    }

    private void goBack()
    {
        String backStateName = this.getClass().getName();
//     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(backStateName,true);
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
        imgv_aboutusImg = (ImageView) view.findViewById(R.id.imgv_aboutusImg);
        txtv_aboutusVisionTitle = (TextView) view.findViewById(R.id.txtv_aboutusVisionTitle);
        txtv_aboutusVisionValue = (TextView) view.findViewById(R.id.txtv_aboutusVisionValue);
        txtv_aboutusMissionTitle = (TextView) view.findViewById(R.id.txtv_aboutusMissionTitle);
        txtv_aboutusMissionValue = (TextView) view.findViewById(R.id.txtv_aboutusMissionValue);
    }

    private void bindViews()
    {
    }


}
