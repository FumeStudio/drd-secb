package com.secb.android.model;

import java.io.Serializable;

public class GalleryItem implements Serializable
{
    public static int GALLERY_TYPE_IMAGE_GALLERY = 1;
    public static int GALLERY_TYPE_IMAGE_ALBUM = 2;
    public static int GALLERY_TYPE_VIDEO_GALLERY = 3;
    public static int GALLERY_TYPE_VIDEO_ALBUM = 4;



//for gallery
    public int galleryItemType;
    public String Title;
    public String Created;
    public String Id;
    public String IsFolder;
    public String FolderPath;

//for photo gallery
    public String PhotoGalleryImageUrl;
    public String PhotoGalleryAlbumThumbnail;

//for video gallery
    public String VideoGalleryUrl;
    public String VideosGalleryAlbumThumbnail;
}
