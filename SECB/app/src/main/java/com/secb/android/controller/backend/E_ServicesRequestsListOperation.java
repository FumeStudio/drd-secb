package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.model.E_ServicesFilterData;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import org.apache.http.client.methods.HttpGet;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class E_ServicesRequestsListOperation extends BaseOperation {
	private static final String TAG = "E_ServicesListOperation";
	Context context;
	private int pageIndex;
	private int pageSize;
	E_ServicesFilterData e_servicesFilterData;
	public E_ServicesRequestsListOperation(int requestID, boolean isShowLoadingDialog, Context context,
	                                       E_ServicesFilterData e_servicesFilterData, int pageSize, int pageIndex) {
		super(requestID, isShowLoadingDialog, context);
		this.context = context;
		this.e_servicesFilterData=e_servicesFilterData;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}


	@Override
	public Object doMain() throws Exception {
		if(e_servicesFilterData==null)
			return null;
		String language = UiEngine.getCurrentAppLanguage(context);

		if(Utilities.isNullString(language))
			language=UiEngine.getCurrentDeviceLanguage(context);

		StringBuilder stringBuilder;
		stringBuilder = new StringBuilder(ServerKeys.E_Services_REQUESTS_LIST);
		stringBuilder.append("?Lang=" + language
		+"&UserName="+e_servicesFilterData.UserName
		+"&FromDate="+e_servicesFilterData.FromDate
		+"&ToDate="+e_servicesFilterData.ToDate
		+"&Status="+e_servicesFilterData.Status
		+"&RequestType="+e_servicesFilterData.RequestType
		+"&RequestNumber="+e_servicesFilterData.RequestNumber
		/*+ "&pageSize=" + pageSize + "&pageIndex=" + pageIndex*/
		);

		String requestUrl = stringBuilder.toString();
		requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
		HashMap<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

		CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
		Logger.instance().v(TAG, response.response);

		if(response == null || Utilities.isNullString(response.response.toString()))
			return null;
		Gson gson = new Gson();
		Type listType = new TypeToken<List<E_ServiceRequestItem>>() {}.getType();
		List<E_ServiceRequestItem> e_serviceRequestItems = gson.fromJson(response.response.toString(), listType);
		removeUnCompletedItems(e_serviceRequestItems);

		//cach only the un filtered lists
		E_ServicesFilterData unFiltered = new E_ServicesFilterData();
		if( e_servicesFilterData.FromDate==null&&
			e_servicesFilterData.ToDate==null &&
			e_servicesFilterData.Status.equalsIgnoreCase(unFiltered.Status) &&
			e_servicesFilterData.RequestType.equalsIgnoreCase(unFiltered.RequestType) &&
			e_servicesFilterData.RequestNumber.equalsIgnoreCase(unFiltered.RequestNumber)
				)
		{
			updateManager(e_serviceRequestItems);
		}
		return e_serviceRequestItems;
	}

	//if news Item does not contain title and brief and date remove it.
	private void removeUnCompletedItems(List<E_ServiceRequestItem> e_serviceRequestItems) {
		if (e_serviceRequestItems == null || e_serviceRequestItems.size() == 0)
			return;
		//to avoid Concurrent Modification Exception
		/*synchronized(organizerItems)*/{
			for (int i = 0 ; i< e_serviceRequestItems.size();i++)
			{
				E_ServiceRequestItem currentItem = e_serviceRequestItems.get(i);
				if (Utilities.isNullString(currentItem.RequestNumber) ||
						Utilities.isNullString(currentItem.RequestDate) ||
						Utilities.isNullString(currentItem.RequestType) )
				{
					e_serviceRequestItems.remove(currentItem);
					i-=1; //to reLoop current items
				}

			}
		}
	}

	private void updateManager(List<E_ServiceRequestItem> e_serviceRequestItems)
	{
		if (e_serviceRequestItems == null || e_serviceRequestItems.size() == 0)
			return;
		E_ServicesManager.getInstance().setEservicesRequestsUnFilteredList(e_serviceRequestItems, context);

	}


}
