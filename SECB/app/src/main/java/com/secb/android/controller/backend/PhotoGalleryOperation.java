package com.secb.android.controller.backend;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.PhotoGallery;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.controller.backend.BaseOperation;
import net.comptoirs.android.common.controller.backend.CTHttpResponse;
import net.comptoirs.android.common.controller.backend.ServerConnection;
import net.comptoirs.android.common.helper.Logger;

import org.apache.http.client.methods.HttpGet;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class PhotoGalleryOperation extends BaseOperation {
    private static final String TAG ="LoginOperation" ;
    String loginXml;
    boolean rememberMe;
    Context context;
    int pageSize , pageIndex;

    public PhotoGalleryOperation(int requestID, boolean isShowLoadingDialog,
                                 Context context, int pageSize , int pageIndex)
    {
        super(requestID, isShowLoadingDialog, context);
        this.context = context;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;

    }


    @Override
    public Object doMain() throws Exception
    {

        String language = UiEngine.getCurrentAppLanguage(context);

        StringBuilder stringBuilder = new StringBuilder(ServerKeys.PHOTO_URL);
        stringBuilder.append("?lang=" + language + "&pageSize=" + pageSize + "&pageIndex=" + pageIndex);
        String requestUrl=stringBuilder.toString();

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

        CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null,cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG,response.response);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<PhotoGallery>>(){}.getType();
        List<PhotoGallery> photoAlbums = (List<PhotoGallery>) gson.fromJson(response.response.toString(), listType);

        return photoAlbums;
    }



}
