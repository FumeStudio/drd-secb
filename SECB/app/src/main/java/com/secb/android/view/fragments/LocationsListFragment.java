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
import com.secb.android.controller.backend.E_GuideLocationListOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EGuideLocationManager;
import com.secb.android.model.LocationItem;
import com.secb.android.model.LocationsFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
import com.secb.android.view.components.filters_layouts.LocationsFilterLayout;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;
import com.secb.android.view.components.recycler_locations.LocationsItemRecyclerAdapter;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;

import java.util.ArrayList;

public class LocationsListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener ,RecyclerCustomClickListener , RequestObserver

{
	private static final String TAG = "LocationsListFragment";
    RecyclerView locationsRecyclerView;
    LocationsItemRecyclerAdapter locationsItemRecyclerAdapter;
    ArrayList<LocationItem> locationItems;
    LocationsFilterData locationsFilterData;
	TextView txtv_noData;
    View view;
    private LocationsFilterLayout locationsFilterLayout =null;
	private ProgressDialog progressDialog;


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
	    ((MainActivity)getActivity()).setLocationRequstObserver(this);
	    initViews(view);
        initFilterLayout();
	    getData();
        return view;
    }

    public void initFilterLayout()
    {
        locationsFilterLayout = new LocationsFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(locationsFilterLayout, false);
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
	    ((MainActivity)getActivity()).hideFilterLayout();
        locationsFilterData =this.locationsFilterLayout.getFilterData();
//        if(locationsFilterData !=null){
//            StringBuilder types = new StringBuilder();
//            for(int i : locationsFilterData.types){
//                types.append(i+" , ");
//            }
//            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
//                    " name: "+ locationsFilterData.city +"\n" +
//                    " city: "+ locationsFilterData.city +"\n" +
//                    " Capacity From: "+ locationsFilterData.totalCapacityFrom+"\n" +
//                    " Capacity To: "+ locationsFilterData.totalCapacityTo+" \n" +
//                    " Types : "+ types.toString());
//        }
    }


    private void initViews(View view)
    {
//        locationItems = DevData.getLocationsList();
	    progressDialog = CustomProgressDialog.getInstance(getActivity(), true);
	    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		    @Override
		    public void onCancel(DialogInterface dialog) {
			    bindViews();
		    }
	    });
	    txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
	    locationsRecyclerView = (RecyclerView) view.findViewById(R.id.locationsRecyclerView);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        locationsRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), locationsRecyclerView, this));
    }

	private void bindViews() {
		if(locationItems!=null && locationItems.size()>0)
		{
			locationsRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			locationsItemRecyclerAdapter = new LocationsItemRecyclerAdapter(getActivity(), locationItems);
			locationsRecyclerView.setAdapter(locationsItemRecyclerAdapter);
		}
		else {
			locationsRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			txtv_noData.setText(getString(R.string.locations_no_locations));
		}
	}


	public void getData()
	{
		//if news list is loaded in the manager get it and bind
		//if not and the main activity is still loading the news list
		// wait for it and it will notify handleRequestFinished in this fragment.
		//if the main activity finished loading news list and the manager is still empty
		//start operation here.


		locationItems = (ArrayList<LocationItem>) EGuideLocationManager.getInstance().getLocationsUnFilteredList(getActivity());
		if(locationItems!= null && locationItems.size()>0){
			handleRequestFinished(RequestIds.EGUIDE_LOCATION_LIST_REQUEST_ID, null, locationItems);
		}
		else {
			if (((MainActivity) getActivity()).isLocationLoadingFinished == false) {
				startWaiting();
			}
			else{
				startLocationsListOperation(new LocationsFilterData(), true);
			}
		}

	}


	private void startWaiting() {
		if(progressDialog!=null&& !progressDialog.isShowing())
		{
			progressDialog.show();
		}
	}

	private void stopWaiting() {
		if(progressDialog!=null&& progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
	}
	private void startLocationsListOperation(LocationsFilterData locationsFilterData, boolean showDialog)
	{
		E_GuideLocationListOperation operation = new E_GuideLocationListOperation(RequestIds.EGUIDE_LOCATION_LIST_REQUEST_ID,showDialog,getActivity(),locationsFilterData,100,0);
		operation.addRequsetObserver(this);
		operation.execute();
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

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		stopWaiting();
		if (error == null)
		{
			Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
			if((int)requestId == RequestIds.EGUIDE_LOCATION_LIST_REQUEST_ID && resultObject!=null){
				locationItems= (ArrayList<LocationItem>) resultObject;

			}

		}
		else if (error != null && error instanceof CTHttpError)
		{
			Logger.instance().v(TAG,error);
			int statusCode = ((CTHttpError) error).getStatusCode();
			if (RequestHandler.isRequestTimedOut(statusCode))
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
			}
			else if (statusCode == -1)
			{
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
