package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.E_ServiceStatisticsItem;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import org.apache.http.client.methods.HttpGet;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class E_ServicesStatisticsListOperation extends BaseOperation {
	private static final String TAG = "E_ServicesStatisticsListOperation";
	Context context;
	private int pageIndex;
	private int pageSize;
	public E_ServicesStatisticsListOperation(int requestID, boolean isShowLoadingDialog, Context context,
	                                         int pageSize, int pageIndex) {
		super(requestID, isShowLoadingDialog, context);
		this.context = context;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}


	@Override
	public Object doMain() throws Exception {
		String requestUserName = UserManager.getInstance().getUser().emailAddress;
		if(Utilities.isNullString(requestUserName ))
			return null;
//		String language = UiEngine.getCurrentAppLanguage(context);


		StringBuilder stringBuilder;
		stringBuilder = new StringBuilder(ServerKeys.E_Services_STATISTICS_LIST);
		stringBuilder.append(
		"?userName="+requestUserName
		/*+"&Lang=" + language*/
		/*+ "&pageSize=" + pageSize + "&pageIndex=" + pageIndex*/
		);

		String requestUrl = stringBuilder.toString();
		requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
		HashMap<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

		CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
		Logger.instance().v(TAG, response.response);

		Gson gson = new Gson();
		Type listType = new TypeToken<List<E_ServiceStatisticsItem>>() {}.getType();
		List<E_ServiceStatisticsItem> e_serviceStatisticsItems = gson.fromJson(response.response.toString(), listType);
		removeUnCompletedItems(e_serviceStatisticsItems);
		updateOrganizersManager(e_serviceStatisticsItems);
		return e_serviceStatisticsItems;
	}

	//if news Item does not contain title and brief and date remove it.
	private void removeUnCompletedItems(List<E_ServiceStatisticsItem> e_serviceStatisticsItems) {
		if (e_serviceStatisticsItems == null || e_serviceStatisticsItems.size() == 0)
			return;
		//to avoid Concurrent Modification Exception
		/*synchronized(organizerItems)*/{
			for (int i = 0 ; i< e_serviceStatisticsItems.size();i++)
			{
				E_ServiceStatisticsItem currentItem = e_serviceStatisticsItems.get(i);
				if (Utilities.isNullString(currentItem.Key) ||
						Utilities.isNullString(currentItem.Value)  )
				{
					e_serviceStatisticsItems.remove(currentItem);
				}
			}
		}
	}

	private void updateOrganizersManager(List<E_ServiceStatisticsItem> e_serviceStatisticsItems)
	{
		if (e_serviceStatisticsItems == null || e_serviceStatisticsItems.size() == 0)
			return;
		E_ServicesManager.getInstance().setEservicesStatisticsList(e_serviceStatisticsItems, context);

	}


}
