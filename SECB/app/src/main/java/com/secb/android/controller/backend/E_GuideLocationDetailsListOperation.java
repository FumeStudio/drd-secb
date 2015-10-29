package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.EGuideLocationManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.LocationItem;
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

public class E_GuideLocationDetailsListOperation extends BaseOperation {
	private static final String TAG = "E_GuideLocationDetailsListOperation";
	Context context;
	private int pageIndex;
	private int pageSize;
	private String locationID;


	public E_GuideLocationDetailsListOperation(int requestID, boolean isShowLoadingDialog, Context context,
	                                           String locationID , int pageSize, int pageIndex) {
		super(requestID, isShowLoadingDialog, context);
		this.context = context;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.locationID=locationID;
	}


	@Override
	public Object doMain() throws Exception {
		if(Utilities.isNullString(locationID))
			return null;
		String language = UiEngine.getCurrentAppLanguage(context);

		if(Utilities.isNullString(language))
			language=UiEngine.getCurrentDeviceLanguage(context);


		StringBuilder stringBuilder;
		stringBuilder = new StringBuilder(ServerKeys.EGUIDE_LOCATION_DETAILS_LIST);
		stringBuilder.append("?Lang=" + language + "&LocationID=" + locationID
				/*+"&pageSize=" + pageSize + "&pageIndex=" + pageIndex*/
		);

		String requestUrl = stringBuilder.toString();
		requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
		HashMap<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

		CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
		Logger.instance().v(TAG, response.response);

		Gson gson = new Gson();
		Type listType = new TypeToken<List<LocationItem>>() {}.getType();
		List<LocationItem> locationItems = gson.fromJson(response.response.toString(), listType);

//		LocationItem locationDetails = gson.fromJson(response.response.toString(), LocationItem.class);
		updateLocationsManager(locationItems);
		if(locationItems == null || locationItems.size()==0 ||locationItems.get(0)== null)
			return null;
		return locationItems.get(0);
	}

	private void updateLocationsManager(List<LocationItem> locationItems)
	{
		if (locationItems == null || locationItems.size()==0 ||locationItems.get(0)== null)
			return;
		EGuideLocationManager.getInstance().setLocationDetails(locationItems.get(0), context);
	}


}
