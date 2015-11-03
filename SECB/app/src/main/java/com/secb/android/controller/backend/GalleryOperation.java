package com.secb.android.controller.backend;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.secb.android.controller.manager.GalleryManager;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.GalleryItem;
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

public class GalleryOperation extends BaseOperation {
    private static final String TAG ="LoginOperation" ;
    Context context;
    int pageSize , pageIndex;
    int galleryType;
	private String language ;

	public GalleryOperation(int galleryType, int requestID, boolean isShowLoadingDialog,
                            Context context, int pageSize, int pageIndex)
    {
        super(requestID, isShowLoadingDialog, context);
        this.galleryType = galleryType;
        this.context = context;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;

    }


    @Override
    public Object doMain() throws Exception
    {

        if(galleryType!= GalleryItem.GALLERY_TYPE_IMAGE_GALLERY && galleryType!= GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
        {
            return null;
        }

        language = UiEngine.getCurrentAppLanguage(context);

	    if(Utilities.isNullString(language))
		    language=UiEngine.getCurrentDeviceLanguage(context);

        StringBuilder stringBuilder ;
        if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_GALLERY )
            stringBuilder= new StringBuilder(ServerKeys.PHOTO_URL);
        else /*if (galleryType== GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)*/
            stringBuilder= new StringBuilder(ServerKeys.VIDEO_URL);

        stringBuilder.append("?lang=" + language + "&pageSize=" + pageSize + "&pageIndex=" + pageIndex);
        String requestUrl=stringBuilder.toString();

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

        CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null,cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG,response.response);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<GalleryItem>>(){}.getType();
        List<GalleryItem> galleryItems = gson.fromJson(response.response.toString(), listType);

        updateGalleryManager(galleryType , galleryItems);
        return galleryItems;
    }

    private void updateGalleryManager(int galleryType, List<GalleryItem> galleryItems)
    {
        if(galleryItems==null ||galleryItems.size()==0)
            return;

        if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_GALLERY)
        {
            GalleryManager.getInstance().setImageGalleryList(galleryItems,context);
        }
        else if(galleryType== GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
        {
            GalleryManager.getInstance().setVideoGalleryList(galleryItems,context);
        }
    }


}
