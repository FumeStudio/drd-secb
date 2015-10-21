package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.model.GalleryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GalleryManager {
    private static GalleryManager instance;

    private GalleryManager() {
    }
    public static GalleryManager getInstance() {
        if (instance == null) {
            instance = new GalleryManager();
        }
        return instance;
    }

    //image gallery list
    List<GalleryItem> imageGalleryList= null;

    //video gallery list
    List<GalleryItem> videoGalleryList= null;

    //image album list. Map<AlbumId , List of Images>
    HashMap<String,List<GalleryItem> > imageAlbumListMap =new HashMap<>();

    //video album list. Map<AlbumId , List of Videos>
    HashMap<String,List<GalleryItem> > videoAlbumList=new HashMap<>();

    public List<GalleryItem> getImageGalleryList(Context appContext)
    {
	    List<GalleryItem> temp = CachingManager.getInstance().loadImageGallery(appContext);
        return temp/*imageGalleryList*/;
    }

    public void setImageGalleryList(List<GalleryItem> imageGalleryList ,Context appContext) {
        this.imageGalleryList = imageGalleryList;
	    CachingManager.getInstance().saveImageGallery((ArrayList<GalleryItem>) imageGalleryList, appContext);



/*
	    //cache list inside sd using caching manager
	    private void cacheAlbum(int galleryType, List<GalleryItem> galleryItems)
	    {
		    if(galleryItems==null ||galleryItems.size()==0)
			    return;
		    if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_ALBUM)
		    {
			    CachingManager.getInstance().saveImageGallery(albumId,galleryItems);
		    }
		    else if(galleryType== GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
		    {
			    GalleryManager.getInstance().addVideoAlbumList(albumId,galleryItems);
		    }
	    }
*/

    }

    public List<GalleryItem> getVideoGalleryList() {
        return videoGalleryList;
    }

    public void setVideoGalleryList(List<GalleryItem> videoGalleryList) {
        this.videoGalleryList = videoGalleryList;
    }

    //add list of images in an album with id = albumId
    public void addImageAlbumList(String albumId , List<GalleryItem> imageAlbumList,Context appContext){
        imageAlbumListMap.put(albumId, imageAlbumList);
	    CachingManager.getInstance().saveImageAlbum(albumId,
			    (ArrayList<GalleryItem>) imageAlbumList,appContext);
    }

    //get list of images from album whose id = albumId
    public List<GalleryItem> getImageAlbumList(Context appContext,String albumId )
    {
	    List<GalleryItem> temp = CachingManager.getInstance().loadImageAlbum(appContext, albumId);
        /*if(imageAlbumListMap.size()==0 || !imageAlbumListMap.containsKey(albumId))
            return null;
        return imageAlbumListMap.get(albumId);*/
	    return temp;
    }


    //add list of videos in an album with id = albumId
    public void addVideoAlbumList(String albumId , List<GalleryItem> imageGalleryList ){
        videoAlbumList.put(albumId, imageGalleryList);
    }

    //get list of videos from album whose id = albumId
    public List<GalleryItem> getVideosAlbumList(String albumId ){
        if(videoAlbumList.size()==0 || !videoAlbumList.containsKey(albumId))
            return null;
        return videoAlbumList.get(albumId);
    }




}
