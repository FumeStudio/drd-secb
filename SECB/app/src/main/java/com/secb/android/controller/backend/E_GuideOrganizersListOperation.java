package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.EGuideOrganizersManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.OrganizerItem;
import com.secb.android.model.OrganizersFilterData;
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

public class E_GuideOrganizersListOperation extends BaseOperation {
	private static final String TAG = "E_GuideOrganizersListOperation";
	Context context;
	OrganizersFilterData organizersFilterData;
	private int pageIndex;
	private int pageSize;

	public E_GuideOrganizersListOperation(int requestID, boolean isShowLoadingDialog, Context context,
	                                      OrganizersFilterData organizersFilterData, int pageSize, int pageIndex) {
		super(requestID, isShowLoadingDialog, context);
		this.context = context;
		this.organizersFilterData = organizersFilterData;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}


	@Override
	public Object doMain() throws Exception {
		if (organizersFilterData == null)
			return null;
		String language = UiEngine.getCurrentAppLanguage(context);

		if(Utilities.isNullString(language))
			language=UiEngine.getCurrentDeviceLanguage(context);

		StringBuilder stringBuilder;
		stringBuilder = new StringBuilder(ServerKeys.EGUIDE_ORGANIZERS_LIST);
		stringBuilder.append("?Lang=" + language + "&Name=" + organizersFilterData.name+
				"&OrganizerCity=" + organizersFilterData.city +
				"&pageSize=" + pageSize + "&pageIndex=" + pageIndex);

		String requestUrl = stringBuilder.toString();
		requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
		HashMap<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

		CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
		Logger.instance().v(TAG, response.response);

		Gson gson = new Gson();
		Type listType = new TypeToken<List<OrganizerItem>>() {}.getType();
		List<OrganizerItem> organizerItems = gson.fromJson(response.response.toString(), listType);
		removeUnCompletedItems(organizerItems);

//	    only cache the not filtered list
//	    i.e. name = all , id = all , city = all , selectedType = all , capacity = all
		if (    !Utilities.isNullString(organizersFilterData.name) &&
				organizersFilterData.name.equalsIgnoreCase("All") &&

				!Utilities.isNullString(organizersFilterData.city) &&
				organizersFilterData.city.equalsIgnoreCase("All") )
		{

			updateOrganizersManager(organizerItems);
		}
		return organizerItems;
	}

	//if news Item does not contain title and brief and date remove it.
	private void removeUnCompletedItems(List<OrganizerItem> organizerItems) {
		if (organizerItems == null || organizerItems.size() == 0)
			return;
		//to avoid Concurrent Modification Exception
		/*synchronized(organizerItems)*/{
			for (int i = 0 ; i<organizerItems.size();i++)
			{
				OrganizerItem currentItem =organizerItems.get(i);
				if (Utilities.isNullString(currentItem.OrganizerName) ||
						Utilities.isNullString(currentItem.OrganizerEmail) ||
						Utilities.isNullString(currentItem.OrganizerDescription) )
				{
					organizerItems.remove(currentItem);
					i-=1; //to reLoop current items
				}

			}
		}
	}

	private void updateOrganizersManager(List<OrganizerItem> organizerItemList) {
		if (organizerItemList == null || organizerItemList.size() == 0)
			return;

		EGuideOrganizersManager.getInstance().setOrganizersUnFilteredList(organizerItemList,context);

	}


}
