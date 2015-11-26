package com.secb.android.view.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.E_ServicesRequestsListOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.model.E_ServiceRequestTypeItem;
import com.secb.android.model.E_ServiceStatisticsItem;
import com.secb.android.model.E_ServicesFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
import com.secb.android.view.components.dialogs.ProgressWheel;
import com.secb.android.view.components.filters_layouts.EServicesFilterLayout;
import com.secb.android.view.components.recycler_e_service.E_ServiceItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;

public class E_ServicesListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener, RecyclerCustomClickListener ,RequestObserver

{
	private static final String TAG = "E_ServicesListFragment";
	RecyclerView eServicesRecyclerView;
    E_ServiceItemRecyclerAdapter e_serviceItemRecyclerAdapter;
    ArrayList<E_ServiceRequestItem> eServicesList;
	ArrayList<E_ServiceRequestTypeItem> e_serviceRequestsTypesItems;
    E_ServicesFilterData e_servicesFilterData;
	
    LinearLayout layout_graphs_container;
    ProgressWheel progressWheelClosed, progressWheelInbox, progressWheelInProgress;
    private static final int PROGRESS_WHEEL_TIME = 2 * 1000;
	ArrayList<Integer> graphsValues;
    TextView txtv_graph_title_closed, txtv_graph_value_closed,
            txtv_graph_title_inbox, txtv_graph_value_inbox,
            txtv_graph_title_inProgress, txtv_graph_value_inProgress;

    View view;
    private EServicesFilterLayout eServicesFilterLayout = null;
	private TextView txtv_noData;
	private ProgressDialog progressDialog;
	ArrayList<E_ServiceStatisticsItem> e_ServicesStatisticsList;

	public static E_ServicesListFragment newInstance() {
        E_ServicesListFragment fragment = new E_ServicesListFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.eservices));
        ((SECBBaseActivity) getActivity()).showFilterButton(true);//for now till the filter design is received
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
        ((SECBBaseActivity) getActivity()).setClearFilterClickListener(this);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();

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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.e_services_list_fragment, container, false);
            handleButtonsEvents();

        }
//	    ((MainActivity)getActivity()).setEservicesRequstObserver(this);
        initViews(view);
        applyFonts();
        initFilterLayout();
	    getData();
        return view;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// eServicesRecyclerView.setAdapter(null);
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        eServicesRecyclerView.setAdapter(null);
    }

    public void initFilterLayout() {
	    eServicesFilterLayout = new EServicesFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(eServicesFilterLayout, false);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(eServicesFilterLayout.getLayoutView());
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
        // TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
	    if(txtv_noData!=null)
	    {
		    UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
	    }
        UiEngine.applyFontsForAll(getActivity(), layout_graphs_container, UiEngine.Fonts.HVAR);
    }

    private void goBack() {
        ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(getClass().getName(),true);
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
		((SECBBaseActivity)getActivity()).hideFilterLayout();
        e_servicesFilterData = this.eServicesFilterLayout.getFilterData();
        if (e_servicesFilterData != null)
        {
	        startEServicesRequestListOperation(e_servicesFilterData,true);
            /*((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
                    " Name: " + e_servicesFilterData.UserName + "\n" +
                    " Time From: " + e_servicesFilterData.FromDate + "\n" +
                    " Time To: " + e_servicesFilterData.ToDate+ " \n" +
                    " Type: " + e_servicesFilterData.RequestType+
                    " Status: " + e_servicesFilterData.Status);*/
        }
	}

	private void clearFilters() {
		eServicesFilterLayout.clearFilters();
	}

	private void initViews(View view) {
		progressDialog = CustomProgressDialog.getInstance(getActivity(), true);
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				bindViews();
			}
		});

		progressWheelClosed = (ProgressWheel) view.findViewById(R.id.progressWheelClosed);
		progressWheelInbox = (ProgressWheel) view.findViewById(R.id.progressWheelInbox);
		progressWheelInProgress = (ProgressWheel) view.findViewById(R.id.progressWheelProgress);


		txtv_graph_title_closed = (TextView) view.findViewById(R.id.txtv_graph_title_closed);
		txtv_graph_value_closed = (TextView) view.findViewById(R.id.txtv_graph_value_closed);

		txtv_graph_title_inbox = (TextView) view.findViewById(R.id.txtv_graph_title_inbox);
		txtv_graph_value_inbox = (TextView) view.findViewById(R.id.txtv_graph_value_inbox);

		txtv_graph_title_inProgress = (TextView) view.findViewById(R.id.txtv_graph_title_inProgress);
		txtv_graph_value_inProgress = (TextView) view.findViewById(R.id.txtv_graph_value_inProgress);


		txtv_graph_title_closed.setText(Html.fromHtml(getString(R.string.graph_title_closed)));
		txtv_graph_title_inbox.setText(Html.fromHtml(getString(R.string.graph_title_inbox)));
		txtv_graph_title_inProgress.setText(Html.fromHtml(getString(R.string.graph_title_inProgress)));
		layout_graphs_container = (LinearLayout) view.findViewById(R.id.layout_graphs_container);



		txtv_noData  = (TextView) view.findViewById(R.id.txtv_noData);
		eServicesRecyclerView = (RecyclerView) view.findViewById(R.id.eServicesRecyclerView);
		e_serviceItemRecyclerAdapter = new E_ServiceItemRecyclerAdapter(getActivity(), eServicesList);
		eServicesRecyclerView.setAdapter(e_serviceItemRecyclerAdapter);
		LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
		GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),2);

		if(Utilities.isTablet(getActivity()))
			eServicesRecyclerView.setLayoutManager(gridLayoutManager);
		else
			eServicesRecyclerView.setLayoutManager(linearLayoutManager);

		eServicesRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), eServicesRecyclerView, this));

//		eServicesList = DevData.getE_ServicesList();
		try {
			graphsValues = ((SECBBaseActivity)getActivity()).calculateGraphsValues();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(graphsValues==null || graphsValues.size()<3)
		{
			graphsValues = new ArrayList<>();
			graphsValues.add(0);graphsValues.add(0);graphsValues.add(0);
		}
		fillWheelPercentage(graphsValues.get(0), graphsValues.get(1), graphsValues.get(2));

	}

	public void bindViews(){
		if(eServicesList!=null &&eServicesList.size()>0)
		{
			eServicesRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			e_serviceItemRecyclerAdapter = new E_ServiceItemRecyclerAdapter(getActivity(), eServicesList);
			eServicesRecyclerView.setAdapter(e_serviceItemRecyclerAdapter);
		}
		else
		{
			eServicesRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			txtv_noData.setText(getString(R.string.eservices_no_eservices));
		}
	}

	@Override
	public void onItemClicked(View v, int position) {
		((SECBBaseActivity) getActivity()).openE_ServiceDetailsFragment(eServicesList.get(position));
	}

	@Override
	public void onItemLongClicked(View v, int position) {

	}

	public void getData()
	{
		//if news list is loaded in the manager get it and bind
		//if not and the main activity is still loading the events list
		// wait for it and it will notify handleRequestFinished in this fragment.
		//if the main activity finished loading events list and the manager is still empty
		//start operation here.


		eServicesList = (ArrayList<E_ServiceRequestItem>) E_ServicesManager.getInstance().getEservicesRequestsUnFilteredList(getActivity());
		if(eServicesList!= null && eServicesList.size()>0){
			handleRequestFinished(RequestIds.E_SERVICES_REQUESTS_LIST_REQUEST_ID, null, eServicesList);
		}
		else {
//			if (((MainActivity) getActivity()).isEservicesRequestLoadingFinished == false) {
//				startWaiting();
//			}
//			else
			{
				startEServicesRequestListOperation(new E_ServicesFilterData(), true);
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

	private void startEServicesRequestListOperation(E_ServicesFilterData e_servicesFilterData, boolean showDialog) {
		E_ServicesRequestsListOperation operation = new E_ServicesRequestsListOperation(RequestIds.E_SERVICES_REQUESTS_LIST_REQUEST_ID,showDialog,getActivity(),e_servicesFilterData,100,0);
		operation.addRequsetObserver(this);
		operation.execute();
	}


    private void fillWheelPercentage(int closedScore, int inboxScore, int inProgressScore) {

        int largeRadius = (int) getResources().getDimension(R.dimen.home_graphs_wheel_size);

        txtv_graph_value_closed.setText("" + closedScore);
        txtv_graph_value_inbox.setText("" + inboxScore);
        txtv_graph_value_inProgress.setText("" + inProgressScore);

        progressWheelClosed.setColors(getResources().getColor(R.color.graph_color_closed), getResources().getColor(R.color.sceb_dark_blue));
        progressWheelInbox.setColors(getResources().getColor(R.color.graph_color_inbox), getResources().getColor(R.color.sceb_dark_blue));
        progressWheelInProgress.setColors(getResources().getColor(R.color.graph_color_inProgress), getResources().getColor(R.color.sceb_dark_blue));

        progressWheelClosed.startLoading(closedScore, PROGRESS_WHEEL_TIME, "" + closedScore, largeRadius);
        progressWheelInbox.startLoading(inboxScore, PROGRESS_WHEEL_TIME, "" + inboxScore, largeRadius);
        progressWheelInProgress.startLoading(inProgressScore, PROGRESS_WHEEL_TIME, "" + inProgressScore, largeRadius);
    }


	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		Logger.instance().v("RquestFinished", "E-Serviceslist requestId: "+requestId+" error: "+error+" resultObject: "+resultObject);
		// if not attached to activity
		if(getActivity() == null && !isAdded())
			return;
		stopWaiting();
		if (error == null)
		{
			Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
			if((int)requestId == RequestIds.E_SERVICES_REQUESTS_LIST_REQUEST_ID && resultObject!=null){
				eServicesList= (ArrayList<E_ServiceRequestItem>) resultObject;
			}
			else if((int)requestId == RequestIds.E_SERVICES_STATISTICS_LIST_REQUEST_ID && resultObject!=null)
			{

				try {
					graphsValues = ((SECBBaseActivity)getActivity()).calculateGraphsValues();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(graphsValues==null || graphsValues.size()<3)
				{
					graphsValues = new ArrayList<>();
					graphsValues.add(0);graphsValues.add(0);graphsValues.add(0);
				}
				fillWheelPercentage(graphsValues.get(0),graphsValues.get(1),graphsValues.get(2));
			}


		}
		else if (error != null && error instanceof CTHttpError)
		{
			Logger.instance().v(TAG,error);
            int statusCode = ((CTHttpError) error).getStatusCode();
            String errorMsg = ((CTHttpError) error).getErrorMsg();
			if (RequestHandler.isRequestTimedOut(statusCode))
			{
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
			}
            else if(!Utilities.isNullString(errorMsg)){
                ErrorDialog.showMessageDialog(getString(R.string.attention), errorMsg, getActivity());
            }
			else if (statusCode == -1) {
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
