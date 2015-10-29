package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.NewsFilterData;
import com.secb.android.model.NewsItem;
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

public class NewsListOperation extends BaseOperation {
	private static final String TAG = "NewsListOperation";
	Context context;
	NewsFilterData newsFilterData;
	private int pageIndex;
	private int pageSize;

	public NewsListOperation(int requestID, boolean isShowLoadingDialog, Context context,
	                         NewsFilterData newsFilterData, int pageSize, int pageIndex) {
		super(requestID, isShowLoadingDialog, context);
		this.context = context;
		this.newsFilterData = newsFilterData;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}


	@Override
	public Object doMain() throws Exception {
		if (newsFilterData == null)
			return null;
		String language = UiEngine.getCurrentAppLanguage(context);


		StringBuilder stringBuilder;
		stringBuilder = new StringBuilder(ServerKeys.NEWS_URL);
		stringBuilder.append("?Lang=" + language + "&NewsID=" + newsFilterData.newsID +
				"&fromDate=" + newsFilterData.timeFrom + "&toDate=" + newsFilterData.timeTo +
				"NewsCategory=" + newsFilterData.selectedCategoryId +
				"&pageSize=" + pageSize + "&pageIndex=" + pageIndex);
		String requestUrl = stringBuilder.toString();
		requestUrl = Uri.encode(requestUrl, ServerKeys.ALLOWED_URI_CHARS);
		HashMap<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

		CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
		Logger.instance().v(TAG, response.response);

		Gson gson = new Gson();
		Type listType = new TypeToken<List<NewsItem>>() {}.getType();
		List<NewsItem> newsItems = gson.fromJson(response.response.toString(), listType);
		removeUnCompletedItems(newsItems);

//	    only cache the not filtered list
//	    i.e. category = all , id = all , timefrom = null , time to = null
		if (!Utilities.isNullString(newsFilterData.newsCategory) &&
				newsFilterData.newsCategory.equalsIgnoreCase("All") &&
				Utilities.isNullString(newsFilterData.timeTo) &&
				Utilities.isNullString(newsFilterData.timeFrom) &&
				newsFilterData.newsID.equalsIgnoreCase("All")) {

			updateNewsManager(newsItems);
		}
		return newsItems;
	}

	//if news Item does not contain title and brief and date remove it.
	private void removeUnCompletedItems(List<NewsItem> newsItems) {
		if (newsItems == null || newsItems.size() == 0)
			return;

		for (int i = 0 ; i< newsItems.size();i++)
		{
			NewsItem currentItem = newsItems.get(i);
			if (Utilities.isNullString(currentItem.Title) ||
					Utilities.isNullString(currentItem.NewsBrief) ||
					Utilities.isNullString(currentItem.CreationDate) ) {
				newsItems.remove(currentItem);
			}

		}
	}

	private void updateNewsManager(List<NewsItem> newsItems) {
		if (newsItems == null || newsItems.size() == 0)
			return;

		NewsManager.getInstance().setNewsUnFilteredList(newsItems,context);

	}


}
