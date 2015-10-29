package com.secb.android.controller.backend;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.EGuideLocationManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.EGuideLocationTypeItem;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;

import org.apache.http.client.methods.HttpGet;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class E_GuideLocationTypesOperation extends BaseOperation {
    private static final String TAG = "NewsCategoryOperation";
    Context context;

    public E_GuideLocationTypesOperation(int requestID, boolean isShowLoadingDialog, Context context)
    {
        super(requestID, isShowLoadingDialog, context);
	       this.context = context;
    }


    @Override
    public Object doMain() throws Exception
    {
        StringBuilder stringBuilder;
	    stringBuilder = new StringBuilder(ServerKeys.EGUIDE_LOCATION_TYPES);
        String requestUrl = stringBuilder.toString();

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

        CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG, response.response);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<EGuideLocationTypeItem>>() {}.getType();
        List<EGuideLocationTypeItem> items= gson.fromJson(response.response.toString(), listType);

	    updateLocationTypesManager(items);
        return items;
    }



    private void updateLocationTypesManager(List<EGuideLocationTypeItem> items)
    {
        if(items==null ||items.size()==0)
            return;

        //all events categories
	    EGuideLocationManager.getInstance().setLocationTypesList(items, context);
    }


}
