package com.secb.android.controller.backend;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.E_ServiceRequestTypeItem;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;

import org.apache.http.client.methods.HttpGet;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class E_ServicesRequestTypesOperation extends BaseOperation {
    private static final String TAG = "E_ServicesRequestTypesOperation";
    Context context;

    public E_ServicesRequestTypesOperation(int requestID, boolean isShowLoadingDialog, Context context)
    {
        super(requestID, isShowLoadingDialog, context);
	       this.context = context;
    }


    @Override
    public Object doMain() throws Exception
    {
        StringBuilder stringBuilder;
	    stringBuilder = new StringBuilder(ServerKeys.E_Services_REQUESTS_TYPES_LIST);
	    stringBuilder.append("?Lang="+ UiEngine.getCurrentAppLanguage(context));
        String requestUrl = stringBuilder.toString();

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

        CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG, response.response);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<E_ServiceRequestTypeItem>>() {}.getType();
        List<E_ServiceRequestTypeItem> items= gson.fromJson(response.response.toString(), listType);

	    updateManager(items);
        return items;
    }



    private void updateManager(List<E_ServiceRequestTypeItem> items)
    {
        if(items==null ||items.size()==0)
            return;

        //all events categories
	    E_ServicesManager.getInstance().setEservicesRequestsTypesList(items, context);
    }


}
