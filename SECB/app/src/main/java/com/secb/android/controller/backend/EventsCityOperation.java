package com.secb.android.controller.backend;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.EventsCityItem;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;

import org.apache.http.client.methods.HttpGet;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class EventsCityOperation extends BaseOperation {
    private static final String TAG = "NewsCategoryOperation";
    Context context;

    public EventsCityOperation(int requestID, boolean isShowLoadingDialog, Context context)
    {
        super(requestID, isShowLoadingDialog, context);
	       this.context = context;
    }


    @Override
    public Object doMain() throws Exception
    {
        StringBuilder stringBuilder;
	    stringBuilder = new StringBuilder(ServerKeys.EVENTS_CITIES_URL);
        String requestUrl = stringBuilder.toString();

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

        CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG, response.response);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<EventsCityItem>>() {}.getType();
        List<EventsCityItem> eventsCityItems= gson.fromJson(response.response.toString(), listType);

        updateEventsManager(eventsCityItems);
        return eventsCityItems;
    }



    private void updateEventsManager(List<EventsCityItem> eventsCityItems)
    {
        if(eventsCityItems==null ||eventsCityItems.size()==0)
            return;

        //all events categories
	    EventsManager.getInstance().setEventsCityList(eventsCityItems, context);
    }


}
