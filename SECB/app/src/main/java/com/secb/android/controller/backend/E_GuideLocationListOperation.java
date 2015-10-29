package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.EGuideLocationManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.LocationItem;
import com.secb.android.model.LocationsFilterData;
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

public class E_GuideLocationListOperation extends BaseOperation {
	private static final String TAG = "E_GuideLocationListOperation";
	Context context;
	LocationsFilterData locationsFilterData;
	private int pageIndex;
	private int pageSize;

	public E_GuideLocationListOperation(int requestID, boolean isShowLoadingDialog, Context context,
	                                    LocationsFilterData FilterData, int pageSize, int pageIndex) {
		super(requestID, isShowLoadingDialog, context);
		this.context = context;
		this.locationsFilterData = FilterData;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}


	@Override
	public Object doMain() throws Exception {
		if (locationsFilterData == null)
			return null;
		String language = UiEngine.getCurrentAppLanguage(context);

		if(Utilities.isNullString(language))
			language=UiEngine.getCurrentDeviceLanguage(context);

		StringBuilder stringBuilder;
		stringBuilder = new StringBuilder(ServerKeys.EGUIDE_LOCATION_LIST);
		stringBuilder.append("?Lang=" + language + "&Name=" + locationsFilterData.name+
				"&SiteCity=" + locationsFilterData.city + "&SiteType=" + locationsFilterData.selectedType +
				"&Capacity=" + locationsFilterData.capacity +
				"&pagesize=" + pageSize + "&pageindex=" + pageIndex);

		String requestUrl = stringBuilder.toString();
		requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
		HashMap<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

		CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
		Logger.instance().v(TAG, response.response);

		Gson gson = new Gson();
		Type listType = new TypeToken<List<LocationItem>>() {}.getType();
		List<LocationItem> locationItems = gson.fromJson(response.response.toString(), listType);
		removeUnCompletedItems(locationItems);

//	    only cache the not filtered list
//	    i.e. name = all , id = all , city = all , selectedType = all , capacity = all
		if (    !Utilities.isNullString(locationsFilterData.name) &&
				locationsFilterData.name.equalsIgnoreCase("All") &&

				!Utilities.isNullString(locationsFilterData.city) &&
				locationsFilterData.city.equalsIgnoreCase("All") &&

				!Utilities.isNullString(locationsFilterData.selectedType) &&
				locationsFilterData.selectedType.equalsIgnoreCase("All") &&

				!Utilities.isNullString(locationsFilterData.capacity) &&
				locationsFilterData.capacity.equalsIgnoreCase("All"))
		{

			updateLocationsManager(locationItems);
		}
		return locationItems;
	}

	//if news Item does not contain title and brief and date remove it.
	private void removeUnCompletedItems(List<LocationItem> locationItems) {
		if (locationItems == null || locationItems.size() == 0)
			return;

		{
			for (int i = 0 ; i <locationItems.size();i++)
			{
				LocationItem currentItem = locationItems.get(i);
				if (Utilities.isNullString(currentItem.ID) ||
						Utilities.isNullString(currentItem.SiteName) ||
						Utilities.isNullString(currentItem.SiteDescription) )
				{
					locationItems.remove(currentItem);
				}

			}
		}
	}

	private void updateLocationsManager(List<LocationItem> newsItems)
	{
		if (newsItems == null || newsItems.size() == 0)
			return;
		EGuideLocationManager.getInstance().setLocationsUnFilteredList(newsItems,context);
	}


}
