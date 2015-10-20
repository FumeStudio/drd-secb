package com.secb.android.controller.manager;

import com.secb.android.model.GalleryItem;

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
    HashMap<String,List<GalleryItem> > imageAlbumList=new HashMap<>();

    //video album list. Map<AlbumId , List of Videos>
    HashMap<String,List<GalleryItem> > videoAlbumList=new HashMap<>();

    public List<GalleryItem> getImageGalleryList() {
        return imageGalleryList;
    }

    public void setImageGalleryList(List<GalleryItem> imageGalleryList) {
        this.imageGalleryList = imageGalleryList;
    }

    public List<GalleryItem> getVideoGalleryList() {
        return videoGalleryList;
    }

    public void setVideoGalleryList(List<GalleryItem> videoGalleryList) {
        this.videoGalleryList = videoGalleryList;
    }

    //add list of images in an album with id = albumId
    public void addImageAlbumList(String albumId , List<GalleryItem> imageGalleryList ){
        imageAlbumList.put(albumId,imageGalleryList);
    }

    //get list of images from album whose id = albumId
    public List<GalleryItem> getImageAlbumList(String albumId ){
        if(imageAlbumList.size()==0 || !imageAlbumList.containsKey(albumId))
            return null;
        return imageAlbumList.get(albumId);
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
