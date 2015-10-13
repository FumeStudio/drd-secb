package com.secb.android.model;

import java.io.Serializable;

public class GalleryItem implements Serializable
{

    public int galleryItemType;
    public static int GALLERY_TYPE_IMAGE_GALLERY = 1;
    public static int GALLERY_TYPE_IMAGE_ALBUM = 2;
    public static int GALLERY_TYPE_VIDEO_GALLERY = 3;
    public static int GALLERY_TYPE_VIDEO_ALBUM = 4;

    public String galleryItemTitle;
    //used only  with GALLERY_TYPE_IMAGE_GALLERY ,GALLERY_TYPE_VIDEO_GALLERY

    public int imgResource;
    //used for testing later will be replaced by Img Id

    String galleryItemId;
    public GalleryItem(int galleryItemType){
        this.galleryItemType=galleryItemType;
    }
}
