package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsFilterData;
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

public class EventsListOperation extends BaseOperation {
	private static final String TAG = "NewsListOperation";
	Context context;
	EventsFilterData eventsFilterData;
	private int pageIndex;
	private int pageSize;

	public EventsListOperation(int requestID, boolean isShowLoadingDialog, Context context,
	                           EventsFilterData eventsFilterData, int pageSize, int pageIndex) {
		super(requestID, isShowLoadingDialog, context);
		this.context = context;
		this.eventsFilterData = eventsFilterData;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}


	@Override
	public Object doMain() throws Exception {
		if (eventsFilterData == null)
			return null;
		String language = UiEngine.getCurrentAppLanguage(context);

		if(Utilities.isNullString(language))
			language=UiEngine.getCurrentDeviceLanguage(context);

		StringBuilder stringBuilder;
		stringBuilder = new StringBuilder(ServerKeys.EVENTS_URL);
		stringBuilder.append("?lang=" + language + "&EventID=" + eventsFilterData.eventID+
				"&FromDate=" + eventsFilterData.timeFrom + "&ToDate=" + eventsFilterData.timeTo +
				"&EventCity=" + eventsFilterData.city +
				"&EventCategory=" + eventsFilterData.selectedCategoryId +
				"&pageSize=" + pageSize + "&pageIndex=" + pageIndex);
		String requestUrl = stringBuilder.toString();
		requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
		HashMap<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

		CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
		Logger.instance().v(TAG, response.response);

		Gson gson = new Gson();
		Type listType = new TypeToken<List<EventItem>>() {}.getType();
		List<EventItem> eventsItems = gson.fromJson(response.response.toString(), listType);
		removeUnCompletedItems(eventsItems);

//		eventsItems.add(getTestingElement());

//	    only cache the not filtered list
//	    i.e. category = all , id = all , timefrom = null , time to = null , city=all
		if (
				eventsFilterData.eventsCategory.equalsIgnoreCase("All") &&
				eventsFilterData.city.equalsIgnoreCase("All") &&
				Utilities.isNullString(eventsFilterData.timeTo) &&
				Utilities.isNullString(eventsFilterData.timeFrom) &&
				eventsFilterData.eventID.equalsIgnoreCase("All")) {

			updateEventsManager(eventsItems);
		}
		return eventsItems;
	}

	private EventItem getTestingElement() {
		EventItem item = new EventItem();
		item.ID="333";
		item.EventDate= "10/22/2015 1:00:00 PM";
		item.EventSiteCity= "City Starts";
		item.EventSiteName= "Name Starts";
		item.SiteonMap= "12.209,12.289";
		item.Description= "Description Description Description Description " +
				"Description Description Description Description " +
				"Description Description Description Description " +
				"Description Description Description Description " +
				"Description Description Description Description ";

		item.EventCategory="Associations Meetings";
		item.Title="MY Today Title";
		item.IsAllDayEvent="true";
		item.IsRecurrence="false";
		return item ;
	}

	//if news Item does not contain title and brief and date remove it.
	private void removeUnCompletedItems(List<EventItem> eventItems) {
		if (eventItems == null || eventItems.size() == 0)
			return;

		for (int i = 0 ; i<eventItems.size();i++)
		{
			EventItem currentItem  = eventItems.get(i);
			if (Utilities.isNullString(currentItem.Title) ||
//					Utilities.isNullString(currentItem.Description) ||
					Utilities.isNullString(currentItem.EventDate)  ||
					Utilities.isNullString(currentItem.EventCategory) )
			{
				eventItems.remove(currentItem);
				i-=1; //to reLoop current items
			}
		}
	}

	private void updateEventsManager(List<EventItem> eventItems) {
		if (eventItems == null || eventItems.size() == 0)
			return;

		EventsManager.getInstance().setEventsUnFilteredList(eventItems, context);

	}


}
