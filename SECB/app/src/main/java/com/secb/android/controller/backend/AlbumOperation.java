package com.secb.android.controller.backend;

import android.content.Context;
import android.net.Uri;

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

import org.apache.http.client.methods.HttpGet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlbumOperation extends BaseOperation {
    private static final String TAG = "LoginOperation";
    Context context;
    int pageSize, pageIndex;
    int galleryType;
    String folderPath;
    String albumId;

    public AlbumOperation(int galleryType, int requestID, boolean isShowLoadingDialog,
                          Context context, int pageSize, int pageIndex, String folderPath ,String albumId)
    {
        super(requestID, isShowLoadingDialog, context);
        this.galleryType = galleryType;
        this.context = context;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.folderPath = folderPath;
        this.albumId = albumId;
    }


    @Override
    public Object doMain() throws Exception {

        if (galleryType != GalleryItem.GALLERY_TYPE_IMAGE_ALBUM && galleryType != GalleryItem.GALLERY_TYPE_VIDEO_ALBUM) {
            return null;
        }


        String language = UiEngine.getCurrentAppLanguage(context);

        StringBuilder stringBuilder;

        if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_GALLERY)
            stringBuilder = new StringBuilder(ServerKeys.PHOTO_URL);
        else /*if (galleryType== GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)*/
            stringBuilder = new StringBuilder(ServerKeys.VIDEO_URL);

        folderPath = Uri.encode(folderPath, ServerKeys.ALLOWED_URI_CHARS);
        stringBuilder.append("?lang=" + language + "&pageSize=" + pageSize + "&pageIndex=" + pageIndex + "&folderpath=" + folderPath);
        String requestUrl = stringBuilder.toString();

        HashMap<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", UserManager.getInstance().getUser().loginCookie);

        CTHttpResponse response = doRequest(requestUrl, HttpGet.METHOD_NAME, null, null, cookies, null, ServerConnection.ResponseType.RESP_TYPE_STRING);
        Logger.instance().v(TAG, response.response);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<GalleryItem>>() {}.getType();
        List<GalleryItem> galleryItems = gson.fromJson(response.response.toString(), listType);

        //for testing use this fake data
        if(galleryItems==null ||galleryItems.size()==0)
            galleryItems = generateFakeData();


        updateGalleryManager(galleryType, galleryItems);
        return galleryItems;
    }

    private List<GalleryItem> generateFakeData()
    {
        List<GalleryItem> galleryItems = new ArrayList<>();
        GalleryItem item=null;
        for (int i = 0 ; i< 8 ;i++)
        {
            item = new GalleryItem();
            if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_ALBUM) {
                item.PhotoGalleryImageUrl =(i%2==0)? "http://lorempixel.com/output/business-q-c-640-480-7.jpg":"http://lorempixel.com/output/nature-q-c-640-480-2.jpg";
            }
            else if (galleryType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM) {
                item.VideosGalleryAlbumThumbnail = (i%2==0)?"http://lorempixel.com/output/city-q-c-640-480-10.jpg":"http://lorempixel.com/output/nature-q-c-640-480-5.jpg";
                item.VideoGalleryUrl = "https://www.youtube.com/watch?v=iPNCHpHCGAU";
            }
            galleryItems.add(item);
        }
        return galleryItems;
    }

    private void updateGalleryManager(int galleryType, List<GalleryItem> galleryItems)
    {
        if(galleryItems==null ||galleryItems.size()==0)
            return;
        if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_ALBUM)
        {
            GalleryManager.getInstance().addImageAlbumList(albumId,galleryItems);
        }
        else if(galleryType== GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            GalleryManager.getInstance().addVideoAlbumList(albumId,galleryItems);
        }
    }


}
