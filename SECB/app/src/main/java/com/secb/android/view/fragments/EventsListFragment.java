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
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.events_recycler.EventItemRecyclerAdapter;
import com.secb.android.view.components.filters_layouts.EventsFilterLayout;
import com.secb.android.view.components.recycler_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_click_handlers.RecyclerCustomItemTouchListener;

import java.util.ArrayList;

public class EventsListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener ,RecyclerCustomClickListener

{
    RecyclerView eventsRecyclerView;
    EventItemRecyclerAdapter eventItemRecyclerAdapter;
    ArrayList<EventItem> eventsList;
    EventsFilterData eventsFilterData;

    View view;
    private EventsFilterLayout eventsFilterLayout=null;


    public static EventsListFragment newInstance() {
        EventsListFragment fragment = new EventsListFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.events));
        ((SECBBaseActivity) getActivity()).showFilterButton(true);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
//        ((SECBBaseActivity) getActivity()).setFilterIconClickListener(this);
//        SECBBaseActivity.setMenuItemSelected(MenuItem.MENU_HOME);
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
            if (oldParent != container && oldParent != null)
            {
                ((ViewGroup) oldParent).removeView(view);
            }
        }
        else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.events_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        initViews(view);
        initFilterLayout();
        return view;
    }

    public void initFilterLayout()
    {
        eventsFilterLayout= new EventsFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()). setFilterLayout(eventsFilterLayout,false);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(eventsFilterLayout.getLayoutView());
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
        switch (v.getId()) {
            case R.id.txtv_viewAllNews:
                ((MainActivity) getActivity()).openNewsListFragment();
           /* case R.id.imgv_filter:
                ((SECBBaseActivity) getActivity()).displayToast("NewsFragment + filterIconClicked");
                */
            case R.id.btn_applyFilter:
                getFilterDataObject();
            default:
                break;
        }
    }

    private void getFilterDataObject() {
        eventsFilterData =this.eventsFilterLayout.getFilterData();
        if(eventsFilterData !=null){
            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
                    " city: "+ eventsFilterData.city +"\n" +
                    " Time From: "+ eventsFilterData.timeFrom+"\n" +
                    " Time To: "+ eventsFilterData.timeTo+" \n" +
                    " Type: "+ eventsFilterData.type);
        }
    }


    private void initViews(View view)
    {
        eventsList = DevData.getEventsList();
        eventsRecyclerView = (RecyclerView) view.findViewById(R.id.eventsRecyclerView);
        eventItemRecyclerAdapter = new EventItemRecyclerAdapter(getActivity(), eventsList);
        eventsRecyclerView.setAdapter(eventItemRecyclerAdapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        eventsRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), eventsRecyclerView, this));
    }

    @Override
    public void onItemClicked(View v, int position)
    {
        ((MainActivity) getActivity()).openEventDetailsFragment(eventsList.get(position));
    }

    @Override
    public void onItemLongClicked(View v, int position)
    {

    }
}
