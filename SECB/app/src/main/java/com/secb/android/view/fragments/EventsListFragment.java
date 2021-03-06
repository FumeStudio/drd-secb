package com.secb.android.view.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.EventsListOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.backend.ServerKeys;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.controller.manager.PagingManager;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsFilterData;
import com.secb.android.view.EventsActivity;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.RecyclerViewScrollListener;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
import com.secb.android.view.components.filters_layouts.EventsFilterLayout;
import com.secb.android.view.components.recycler_events.EventItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class EventsListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener, RecyclerCustomClickListener, RequestObserver {
    private static final String TAG = "EventsListFragment";
    RecyclerView eventsRecyclerView;
    EventItemRecyclerAdapter eventItemRecyclerAdapter;
    ArrayList<EventItem> eventsList;
    EventsFilterData eventsFilterData;

    View view;
    TextView txtv_noData;
    private EventsFilterLayout eventsFilterLayout = null;
    private ProgressDialog progressDialog;
    private String startDate, endDate;
	private boolean isEnablePaging;

	public static EventsListFragment newInstance() {
        EventsListFragment fragment = new EventsListFragment();
        return fragment;
    }

    public static EventsListFragment newInstance(ArrayList<EventItem> eventsList)
    {
        EventsListFragment fragment = new EventsListFragment();
	    Bundle bundle = new Bundle();
	    bundle.putSerializable("eventsList", eventsList);
	    fragment.setArguments(bundle);
        return fragment;
    }

	//to get list of events in dates
    public static EventsListFragment newInstance(String startDate, String endDate) {
        EventsListFragment fragment = new EventsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("startDate", startDate);
        bundle.putString("endDate", endDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.events));
        ((SECBBaseActivity) getActivity()).showFilterButton(true);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);

	    if(!Utilities.isTablet(getActivity()))
	    {
		    ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
		    ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();
	    }
	    else{
		    ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
		    ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
	    }
        ((SECBBaseActivity) getActivity()).setClearFilterClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
	    ((SECBBaseActivity) getActivity()).showFilterButton(Utilities.isTablet(getActivity()));

	    if(Utilities.isTablet(getActivity()))
	    {
		    ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
		    ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();
	    }
	    else{
		    ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
		    ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
	    }

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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.events_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            startDate = bundle.getString("startDate");
            endDate = bundle.getString("endDate");
	        if(bundle.containsKey("eventsList") && Utilities.isTablet(getActivity()))
	        {
		        this.eventsList = (ArrayList<EventItem>) bundle.get("eventsList");
		        isEnablePaging=true;
	        }
	        else isEnablePaging=false;

        }

        initViews(view);
        ((EventsActivity) getActivity()).setEventsRequstObserver(this);
        initFilterLayout();
	    getData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        eventsRecyclerView.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsRecyclerView.setAdapter(null);
    }

    public void initFilterLayout() {
        eventsFilterLayout = new EventsFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(eventsFilterLayout, true);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(eventsFilterLayout.getLayoutView());
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {

        if (txtv_noData != null) {
            UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
        }
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
            case R.id.imageViewBackHeader:
                onBack();
                break;

            case R.id.btn_applyFilter:
                getFilterDataObject();
                break;
            case R.id.btn_clearFilter:
                clearFilters();
                break;

            default:
                break;
        }
    }

    private void getFilterDataObject() {
        eventsFilterData = this.eventsFilterLayout.getFilterData();

        ((SECBBaseActivity) getActivity()).hideFilterLayout();
        if (eventsFilterData != null) {
            startEventsListOperation(eventsFilterData, true, 0);
//            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
//                    " city: "+ eventsFilterData.city +"\n" +
//                    " Time From: "+ eventsFilterData.timeFrom+"\n" +
//                    " Time To: "+ eventsFilterData.timeTo+" \n" +
//                    " Type: "+ eventsFilterData.selectedCategoryId);
        }
    }


    private void clearFilters() {
        this.eventsFilterLayout.clearFilters();

    }

    private void initViews(View view) {
//        eventsList = DevData.getEventsList();
        progressDialog = CustomProgressDialog.getInstance(getActivity(), true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                bindViews();
            }
        });
        eventsRecyclerView = (RecyclerView) view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
        eventsRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), eventsRecyclerView, this));

	    if(isEnablePaging)
	    {
		    eventsRecyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
			    @Override
			    public void onScrollUp() {

			    }

			    @Override
			    public void onScrollDown() {

			    }

			    @Override
			    public void onLoadMore() {
//				    loadMoreData();
			    }
		    });
	    }
    }

    private void loadMoreData() {

        eventItemRecyclerAdapter.showLoading(true);
        eventItemRecyclerAdapter.notifyDataSetChanged();

        startEventsListOperation(eventsFilterData != null ? eventsFilterData : new EventsFilterData(), false, (PagingManager.getLastPageNumber(eventsList) + 1));
    }

    public void bindViews() {
        if (eventsList != null && eventsList.size() > 0) {
            eventsRecyclerView.setVisibility(View.VISIBLE);
            txtv_noData.setVisibility(View.GONE);
            if(eventItemRecyclerAdapter == null) {
                eventItemRecyclerAdapter = new EventItemRecyclerAdapter(getActivity(), eventsList);
                eventsRecyclerView.setAdapter(eventItemRecyclerAdapter);
            } else {
                eventItemRecyclerAdapter.setItemsList(eventsList);
                eventItemRecyclerAdapter.showLoading(false);
                eventItemRecyclerAdapter.notifyItemRangeChanged(0, eventsList.size());
            }
        } else {
            eventsRecyclerView.setVisibility(View.GONE);
            txtv_noData.setVisibility(View.VISIBLE);
            txtv_noData.setText(getString(R.string.events_no_events));
        }
    }

    @Override
    public void onItemClicked(View v, int position) {
        ((EventsActivity) getActivity()).openEventDetailsFragment(eventsList.get(position));
	    int id = (eventsList.get(position).ID).hashCode();
	    eventItemRecyclerAdapter.setItemSelected(v,id);
    }

    @Override
    public void onItemLongClicked(View v, int position) {

    }

    public void getData() {
        //if news list is loaded in the manager get it and bind
        //if not and the main activity is still loading the events list
        // wait for it and it will notify handleRequestFinished in this fragment.
        //if the main activity finished loading events list and the manager is still empty
        //start operation here.


	    //in case of tablets
	    if(Utilities.isTablet(getActivity()) && eventsList!=null)
	    {
		    handleRequestFinished(RequestIds.EVENTS_LIST_REQUEST_ID, null, eventsList);
	    }
        else if (!Utilities.isNullString(startDate) && !Utilities.isNullString(endDate)) {
        /*from calendar*/
			/*eventsFilterData = new EventsFilterData();
			eventsFilterData.timeFrom=startDate;
			eventsFilterData.timeTo=endDate;
			startEventsListOperation(eventsFilterData, true);*/
            eventsList = EventsManager.getInstance().getMonthEventsList();
            bindViews();

        } else {
            eventsList = (ArrayList<EventItem>) EventsManager.getInstance().getEventsUnFilteredList(getActivity());
            if (eventsList != null && eventsList.size() > 0) {
                handleRequestFinished(RequestIds.EVENTS_LIST_REQUEST_ID, null, eventsList);
            } else
            {
//                if (((MainActivity) getActivity()).isEventsLoadingFinished == false)
//                {
//                    startWaiting();
//                }
//                else
                {
                    startEventsListOperation(new EventsFilterData(), true, 0);
                }
            }
        }
    }


    private void startWaiting() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void stopWaiting() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void startEventsListOperation(EventsFilterData eventFilterData, boolean showDialog, int pageIndex) {
        EventsListOperation operation = new EventsListOperation(RequestIds.EVENTS_LIST_REQUEST_ID, showDialog, getActivity(), eventFilterData, ServerKeys.PAGE_SIZE_DEFAULT, pageIndex);
        operation.addRequsetObserver(this);
        operation.execute();
    }

    @Override
    public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
        if(getActivity() == null)
            return;
        stopWaiting();
        if(eventItemRecyclerAdapter != null) {
            eventItemRecyclerAdapter.showLoading(false);
            eventItemRecyclerAdapter.notifyDataSetChanged();
        }
        if (error == null) {
            Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
            if ((int) requestId == RequestIds.EVENTS_LIST_REQUEST_ID && resultObject != null) {
                ArrayList<EventItem> _eventsList = (ArrayList<EventItem>) resultObject;
                int pageIndex = PagingManager.getLastPageNumber(_eventsList);
                if (eventsList == null || pageIndex == 0)
                    eventsList = new ArrayList<>();
                eventsList.addAll(_eventsList);
            }


        } else if (error != null && error instanceof CTHttpError) {
            Logger.instance().v(TAG, error);
            int statusCode = ((CTHttpError) error).getStatusCode();
            if (RequestHandler.isRequestTimedOut(statusCode)) {
                ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
            } else if (statusCode == -1) {
                ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
                        getActivity());
            }
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
