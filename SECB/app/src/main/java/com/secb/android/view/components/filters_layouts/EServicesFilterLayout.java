package com.secb.android.view.components.filters_layouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.E_ServicesRequestTypesOperation;
import com.secb.android.controller.backend.E_ServicesRequestWorkSpaceModesOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.model.Consts;
import com.secb.android.model.E_ServiceRequestTypeItem;
import com.secb.android.model.E_ServiceRequestWorkSpaceModeItem;
import com.secb.android.model.E_ServicesFilterData;
import com.secb.android.view.MainActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.dialogs.DateTimePickerDialogView;
import com.secb.android.view.components.recycler_e_service.E_ServiceRequestTypeRecyclerAdapter;
import com.secb.android.view.components.recycler_e_service.E_ServiceRequestWorkSpaceModeRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.Utilities;

import java.util.Date;
import java.util.List;

public class EServicesFilterLayout extends LinearLayout implements View.OnClickListener, RequestObserver ,RecyclerCustomClickListener{

	private final View view;

    private E_ServicesFilterData e_servicesFilterData;

    private TextView txtv_timeFrom, txtv_timeTo;

    private TextView txtv_noData_status,txtv_noData_requestType;
	private RecyclerView requestStatusRecyclerView,requestTypeRecyclerView;

	private E_ServiceRequestTypeRecyclerAdapter requestTypeRecyclerAdapter;
	private E_ServiceRequestWorkSpaceModeRecyclerAdapter requestStatusRecyclerAdapter;
	private EditText edtTxt_requestNumber;

    private Context context;
	private List<E_ServiceRequestWorkSpaceModeItem> requestStatusList ;
	private List<E_ServiceRequestTypeItem> requestTypesList ;

	private boolean isRequestStatusOperationDone;
	private boolean isRequestTypesOperationDone;


	public View getLayoutView() {
        return view;
    }

    public EServicesFilterLayout(Context context) {
        super(context);
        this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.e_services_filter_screen, null);
		view.setMinimumHeight(getResources().getDisplayMetrics().heightPixels);
        initViews(view);

	    ((MainActivity)context).setEventsRequstObserver(this);
        applyFonts();
	    requestTypesList = E_ServicesManager.getInstance().getEservicesRequestsTypesList(context);
	    if(requestTypesList==null || requestTypesList.size()==0)
	    {
		    startRequestTypesListOperation();
	    }
	    else{
		    bindRequestsTypesRecycler();
	    }

	    requestStatusList = E_ServicesManager.getInstance().getEservicesRequestsWorkSpaceModesList(context);
	    if(requestStatusList==null || requestStatusList.size()==0)
	    {
		    startEservicesRequestsWorkSpaceModesOperation();
	    }
	    else{
		    bindRequestsStatusRecycler();
	    }


	    if(requestStatusList!=null && requestTypesList!=null &&
			    requestStatusList.size()>0&& requestTypesList.size()>0)
	    {
		    getFilterData();
	    }
    }

	private void startEservicesRequestsWorkSpaceModesOperation() {
		E_ServicesRequestWorkSpaceModesOperation operation = new E_ServicesRequestWorkSpaceModesOperation(RequestIds.E_SERVICES_REQUESTS_WORKSPACE_MODES_LIST_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}

	private void startRequestTypesListOperation() {
		E_ServicesRequestTypesOperation operation = new E_ServicesRequestTypesOperation(RequestIds.E_SERVICES_REQUESTS_TYPES_LIST_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}


	public void initViews(View view) {
        txtv_timeFrom = (TextView) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_timeTo = (TextView) view.findViewById(R.id.txtv_news_filter_time_to_value);

		txtv_noData_status = (TextView) view.findViewById(R.id.txtv_noData_status);
        txtv_noData_requestType = (TextView) view.findViewById(R.id.txtv_noData_request_types);

		requestStatusRecyclerView = (RecyclerView) view.findViewById(R.id.eservicesStatusRecyclerView);
		requestTypeRecyclerView = (RecyclerView) view.findViewById(R.id.eservicesRequestTypesRecyclerView);

		requestStatusRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		requestTypeRecyclerView.setLayoutManager(new LinearLayoutManager(context));

		requestStatusRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(context, requestStatusRecyclerView, this));
		requestTypeRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(context, requestTypeRecyclerView, this));


		edtTxt_requestNumber = (EditText) view.findViewById(R.id.edtTxt_filter_requestNumber_value);

	    txtv_timeFrom.setOnClickListener(this);
        txtv_timeTo.setOnClickListener(this);

    }

	private void bindRequestsTypesRecycler() {
		if (requestTypesList  != null && requestTypesList.size() > 0)
		{
			requestTypeRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData_requestType.setVisibility(View.GONE);
			requestTypeRecyclerAdapter = new E_ServiceRequestTypeRecyclerAdapter(context,requestTypesList);
			requestTypeRecyclerView.setAdapter(requestTypeRecyclerAdapter);
		}
		else
		{
			requestTypeRecyclerView.setVisibility(View.GONE);
			txtv_noData_requestType.setVisibility(View.VISIBLE);
			if(isRequestTypesOperationDone)
				txtv_noData_requestType.setText(context.getString(R.string.news_no_types));
		}
	}

	private void bindRequestsStatusRecycler() {
		if (requestStatusList  != null && requestStatusList.size() > 0)
		{
			requestStatusRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData_status.setVisibility(View.GONE);
			requestStatusRecyclerAdapter = new E_ServiceRequestWorkSpaceModeRecyclerAdapter(context,requestStatusList);
			requestStatusRecyclerView.setAdapter(requestStatusRecyclerAdapter);
		}
		else
		{
			requestStatusRecyclerView.setVisibility(View.GONE);
			txtv_noData_status.setVisibility(View.VISIBLE);
			if(isRequestTypesOperationDone)
				txtv_noData_status.setText(context.getString(R.string.news_no_status));
		}
	}


	private void applyFonts()
    {
	    UiEngine.applyFontsForAll(context,view,UiEngine.Fonts.HVAR);
        /*if(spn_city !=null)
        {
            UiEngine.applyCustomFont(spn_city, UiEngine.Fonts.HVAR);
        }
        if(txtv_timeFrom!=null)
        {
            UiEngine.applyCustomFont(txtv_timeFrom, UiEngine.Fonts.HVAR);
        }
        if(txtv_timeTo!=null)
        {
            UiEngine.applyCustomFont(txtv_timeTo, UiEngine.Fonts.HVAR);
        }
        if(txtv_news_filter_category_title!=null)
        {
            UiEngine.applyCustomFont(txtv_news_filter_category_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_filter_city_title!=null)
        {
            UiEngine.applyCustomFont(txtv_event_filter_city_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_filter_time_from_title!=null)
        {
            UiEngine.applyCustomFont(txtv_event_filter_time_from_title, UiEngine.Fonts.HVAR);
        }
        if(radbtn_allTypes!=null)
        {
            UiEngine.applyCustomFont(radbtn_allTypes, UiEngine.Fonts.HVAR);
        }
        if(radbtn_economicType!=null)
        {
            UiEngine.applyCustomFont(radbtn_economicType, UiEngine.Fonts.HVAR);
        }
        if(radbtn_politicalType!=null)
        {
            UiEngine.applyCustomFont(radbtn_politicalType, UiEngine.Fonts.HVAR);
        }
        if(radbtn_publicType!=null)
        {
            UiEngine.applyCustomFont(radbtn_publicType, UiEngine.Fonts.HVAR);
        }
        if(btn_applyFilter!=null)
        {
            UiEngine.applyCustomFont(btn_applyFilter, UiEngine.Fonts.HVAR);
        }
	    if(txtv_noData!=null)
	    {
		    UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
	    }*/
    }

    public E_ServicesFilterData getFilterData()
    {
        e_servicesFilterData = new E_ServicesFilterData();

	    if(!Utilities.isNullString(txtv_timeFrom.getText().toString())
			    && !Utilities.isNullString(txtv_timeFrom.getText().toString()))
	    {
		    e_servicesFilterData.FromDate = txtv_timeFrom.getText().toString();
		    e_servicesFilterData.ToDate = txtv_timeTo.getText().toString();
	    }

	    E_ServiceRequestTypeItem selectedRequestType = E_ServicesManager.getInstance().getSelectedRequestType();
	    if( selectedRequestType!=null)
	    {
		    if(selectedRequestType.Value.equalsIgnoreCase("All") ||
				    selectedRequestType.Value.equalsIgnoreCase("الكل"))
		    {
			    e_servicesFilterData.RequestType= selectedRequestType.Value;
		    }
		    else
			    e_servicesFilterData.RequestType=selectedRequestType.Key;
	    }
	    E_ServiceRequestWorkSpaceModeItem selectedRequestMode = E_ServicesManager.getInstance().getSelectedRequestWorkSpaceModeType();
	    if( selectedRequestMode!=null)
	    {
		    if(selectedRequestMode.NameEn.equalsIgnoreCase("All") ||
				    selectedRequestMode.NameAr.equalsIgnoreCase("الكل"))
		    {
			    e_servicesFilterData.RequestType= "All";
		    }
		    e_servicesFilterData.Status=selectedRequestType.Value;
	    }

	    if(!Utilities.isNullString(edtTxt_requestNumber.getText().toString()))
	    {
		    e_servicesFilterData.RequestNumber=edtTxt_requestNumber.getText().toString();
	    }
        return e_servicesFilterData;
    }


	public void clearFilters(){
		txtv_timeFrom.setText("");
		txtv_timeFrom.setHint(context.getString(R.string.filter_time_from));
		txtv_timeTo.setText("");
		txtv_timeTo.setHint(context.getString(R.string.filter_time_to));
		edtTxt_requestNumber.setText("");

		this.e_servicesFilterData = new E_ServicesFilterData();
		requestTypesList = E_ServicesManager.getInstance().getEservicesRequestsTypesList(context);
		requestStatusList = E_ServicesManager.getInstance().getEservicesRequestsWorkSpaceModesList(context);
		bindRequestsTypesRecycler();
		bindRequestsStatusRecycler();

	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtv_news_filter_time_from_value:
                showDateTimePicker(txtv_timeFrom);
                break;
            case R.id.txtv_news_filter_time_to_value:
                showDateTimePicker(txtv_timeTo);
            break;
        }
    }

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		requestStatusRecyclerView.setAdapter(null);
		requestTypeRecyclerView.setAdapter(null);
	}
    public void showDateTimePicker(final TextView textView)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DateTimePickerDialogView dialogView = new DateTimePickerDialogView(context);
        builder.setView(dialogView);

        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence time = Consts.APP_DEFAULT_DATE_TIME_FORMAT.format(new Date(dialogView.getSelectedDateTime().getTimeInMillis()));
                textView.setText(time);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if (error == null)
		{
			if((int)requestId == RequestIds.E_SERVICES_REQUESTS_TYPES_LIST_REQUEST_ID )
			{
				isRequestTypesOperationDone =true;
				requestTypesList = E_ServicesManager.getInstance().getEservicesRequestsTypesList(context);
				bindRequestsTypesRecycler();
			}
			else if ((int) requestId == RequestIds.E_SERVICES_REQUESTS_WORKSPACE_MODES_LIST_REQUEST_ID )
			{
				isRequestStatusOperationDone =true;
				requestStatusList = E_ServicesManager.getInstance().getEservicesRequestsWorkSpaceModesList(context);
				bindRequestsStatusRecycler();
			}
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}

	@Override
	public void onItemClicked(View v, int position) {

	}

	@Override
	public void onItemLongClicked(View v, int position) {

	}
}
