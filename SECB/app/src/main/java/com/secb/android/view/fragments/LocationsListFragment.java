package com.secb.android.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.controller.manager.DevData;
import com.secb.android.model.LocationItem;
import com.secb.android.model.LocationsFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.filters_layouts.LocationsFilterLayout;
import com.secb.android.view.components.locations_recycler.LocationsItemRecyclerAdapter;
import com.secb.android.view.components.recycler_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_click_handlers.RecyclerCustomItemTouchListener;

import java.util.ArrayList;

public class LocationsListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener ,RecyclerCustomClickListener

{
    RecyclerView locationsRecyclerView;
    LocationsItemRecyclerAdapter locationsItemRecyclerAdapter;
    ArrayList<LocationItem> locationItems;
    LocationsFilterData locationsFilterData;

    View view;
    private LocationsFilterLayout locationsFilterLayout =null;


    public static LocationsListFragment newInstance() {
        LocationsListFragment fragment = new LocationsListFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.location_eguide));
        ((SECBBaseActivity) getActivity()).showFilterButton(true);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
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
            if (oldParent != container && oldParent != null)
            {
                ((ViewGroup) oldParent).removeView(view);
            }
        }
        else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.location_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        initViews(view);
        initFilterLayout();
        return view;
    }

    public void initFilterLayout()
    {
        locationsFilterLayout = new LocationsFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(locationsFilterLayout,false);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(locationsFilterLayout.getLayoutView());
    }
    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
        // TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
    }

    private void goBack() {
        ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(getClass().getName());
    }

    // ////////////////////////////////////////////////////////////

    @Override
    public void onBack() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageViewBackHeader:
                onBack();
                break;

            case R.id.btn_applyFilter:
                getFilterDataObject();
                break;

            default:
                break;
        }
    }

    private void getFilterDataObject() {
        locationsFilterData =this.locationsFilterLayout.getFilterData();
        if(locationsFilterData !=null){
            StringBuilder types = new StringBuilder();
            for(int i : locationsFilterData.types){
                types.append(i+" , ");
            }

            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
                    " name: "+ locationsFilterData.city +"\n" +
                    " city: "+ locationsFilterData.city +"\n" +
                    " Capacity From: "+ locationsFilterData.totalCapacityFrom+"\n" +
                    " Capacity To: "+ locationsFilterData.totalCapacityTo+" \n" +
                    " Types : "+ types.toString());
        }
    }


    private void initViews(View view)
    {
        locationItems = DevData.getLocationsList();
        locationsRecyclerView = (RecyclerView) view.findViewById(R.id.locationsRecyclerView);
        locationsItemRecyclerAdapter = new LocationsItemRecyclerAdapter(getActivity(), locationItems);
        locationsRecyclerView.setAdapter(locationsItemRecyclerAdapter);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        locationsRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), locationsRecyclerView, this));
    }

    @Override
    public void onItemClicked(View v, int position)
    {
        ((MainActivity) getActivity()).openLocationDetailsFragment(locationItems.get(position));
    }

    @Override
    public void onItemLongClicked(View v, int position)
    {

    }
}
