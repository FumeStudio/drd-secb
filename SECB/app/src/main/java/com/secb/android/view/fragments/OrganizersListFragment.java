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
import com.secb.android.controller.backend.E_GuideOrganizersListOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.EGuideOrganizersManager;
import com.secb.android.model.OrganizerItem;
import com.secb.android.model.OrganizersFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
import com.secb.android.view.components.filters_layouts.OrganizersFilterLayout;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;
import com.secb.android.view.components.recycler_organizers.OrganizerItemRecyclerAdapter;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;

import java.util.ArrayList;

public class OrganizersListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener ,RecyclerCustomClickListener , RequestObserver

{
	private static final String TAG = "OrganizersListFragment";
	RecyclerView organizerRecyclerView;
    OrganizerItemRecyclerAdapter organizerItemRecyclerAdapter;

    OrganizersFilterData organizerFilterData;
	private ProgressDialog progressDialog;
    View view;
    private OrganizersFilterLayout organizersFilterLayout =null;
	ArrayList<OrganizerItem> organizerList;
	private TextView txtv_noData;


	public static OrganizersListFragment newInstance() {
        OrganizersListFragment fragment = new OrganizersListFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.organizers_eguide));
        ((SECBBaseActivity) getActivity()).showFilterButton(true);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
        ((SECBBaseActivity) getActivity()).setClearFilterClickListener(this);
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.organizers_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
	    ((MainActivity)getActivity()).setOrganizersRequstObserver(this);
        initViews(view);
        initFilterLayout();
	    getData();
        return view;
    }

    public void initFilterLayout()
    {
        organizersFilterLayout = new OrganizersFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(organizersFilterLayout, true);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(organizersFilterLayout.getLayoutView());
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


	private void initViews(View view)
	{
//        organizerList = DevData.getOrganizersList();
		progressDialog = CustomProgressDialog.getInstance(getActivity(), true);
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				bindViews();
			}
		});

		organizerRecyclerView = (RecyclerView) view.findViewById(R.id.organizerRecyclerView);
		txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
//        organizerRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		organizerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		organizerRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), organizerRecyclerView, this));
	}

	private void bindViews() {
		if(organizerList!=null && organizerList.size()>0)
		{
			organizerRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			organizerItemRecyclerAdapter = new OrganizerItemRecyclerAdapter(getActivity(), organizerList);
			organizerRecyclerView.setAdapter(organizerItemRecyclerAdapter);
		}
		else {
			organizerRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			txtv_noData.setText(getString(R.string.organizer_no_organizers));
		}
	}

	public void getData()
	{
		//if news list is loaded in the manager get it and bind
		//if not and the main activity is still loading the news list
		// wait for it and it will notify handleRequestFinished in this fragment.
		//if the main activity finished loading news list and the manager is still empty
		//start operation here.


		organizerList = (ArrayList<OrganizerItem>) EGuideOrganizersManager.getInstance().getOrganizersUnFilteredList(getActivity());
		if(organizerList!= null && organizerList.size()>0){
			handleRequestFinished(RequestIds.EGUIDE_LOCATION_LIST_REQUEST_ID, null, organizerList);
		}
		else {
			if (((MainActivity) getActivity()).isOrganizerLoadingFinished == false) {
				startWaiting();
			}
			else{
				startOrganizeListOperation(new OrganizersFilterData(), true);
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

	//E-Guide Organizers List
	private void startOrganizeListOperation(OrganizersFilterData organizersFilterData, boolean showDialog)
	{
		E_GuideOrganizersListOperation operation = new E_GuideOrganizersListOperation(RequestIds.EGUIDE_ORGANIZERS_LIST_REQUEST_ID,showDialog,getActivity(), organizersFilterData,100,0);
		operation.addRequsetObserver(this);
		operation.execute();
	}


    private void getFilterDataObject() {
	    ((MainActivity)getActivity()).hideFilterLayout();
        organizerFilterData = this.organizersFilterLayout.getFilterData();
        if(organizerFilterData !=null){
	            startOrganizeListOperation(organizerFilterData, true);
//            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
//                    "Name : "+ organizerFilterData.name+"\n" +
//                    "City : "+ organizerFilterData.city+" \n" );
        }
    }


	private void clearFilters() {
		this.organizersFilterLayout.clearFilters();
	}


	@Override
    public void onItemClicked(View v, int position)
    {
        ((MainActivity) getActivity()).openOrganizerDetailsFragment(organizerList.get(position));
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
			if((int)requestId == RequestIds.EGUIDE_ORGANIZERS_LIST_REQUEST_ID && resultObject!=null){
				organizerList= (ArrayList<OrganizerItem>) resultObject;

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
