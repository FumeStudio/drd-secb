package com.secb.android.controller.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.secb.android.model.AppConfiguration;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsCategoryItem;
import com.secb.android.model.EventsCityItem;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.NewsCategoryItem;
import com.secb.android.model.NewsItem;
import com.secb.android.model.User;

import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CachingManager {

	private static final String TAG = "GBACachingManager";



	public enum ImageType {
		SMALL_IMAGE, LARGE_IMAGE
	}

	protected static CachingManager self;

	public static CachingManager getInstance() {
		if (self == null) {
			self = new CachingManager();
		}
		return self;
	}


	protected CachingManager() {
		//contentCachingDirectory = MazikaController.getLayoutView().getAppContext().getDir(FOLDER_NAME_CACHED_DATA, Context.MODE_protected);
		//albumCachingDirectory = MazikaController.getLayoutView().getAppContext().getDir(FOLDER_NAME_CACHED_ALBUMS, Context.MODE_protected);
		//categoryCachingDirectory = MazikaController.getLayoutView().getAppContext().getDir(FOLDER_NAME_CACHED_CATEGORY_ALBUMS, Context.MODE_protected);
	}

	protected boolean isObjectCachedAndNotExpired(long expireInHours, File objectFile) {
		boolean exist, expired = false;
		exist = objectFile.exists();

		if (exist) {
			if ((objectFile.lastModified() + expireInHours * 60 * 60 * 1000) < new Date().getTime()) {
				expired = true;
			}
		}
		return exist && !expired;
	}

	protected boolean isObjectCachedAndExpired(long expireInHours, File objectFile) {
		boolean exist, expired = false;
		exist = objectFile.exists();

		if (expireInHours <= 0)
			expired = true;
		else if (exist) {
			if ((objectFile.lastModified() + expireInHours * 60 * 60 * 1000) < new Date().getTime()) {
				expired = true;
			}
		}
		return exist && expired;
	}


	public static void saveObject(Serializable object, File objectFile) throws IOException {
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(objectFile));
		outputStream.writeObject(object);
		objectFile.setLastModified(new Date().getTime());
		outputStream.close();

		objectFile.setLastModified(new Date().getTime());
	}

	public static Serializable loadOject(File objectFile) throws Exception {
		Object cachedObject = null;
		if (objectFile.exists()) {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(objectFile));
			cachedObject = inputStream.readObject();
			inputStream.close();

			objectFile.setLastModified(new Date().getTime());
		}

		return (Serializable) cachedObject;
	}


	public void saveImage(final Bitmap bitmap, final String imageUrl) {

		try {
			File imageFile = new File(Engine.DataFolder.IMAGE_CACHE, Utilities.md5Hash(imageUrl));
			FileOutputStream fOut = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap loadImage(String imageUrl) {

		try {
			File imageFile = new File(Engine.DataFolder.IMAGE_CACHE, Utilities.md5Hash(imageUrl));
			if (!imageFile.exists()) return null;
			FileInputStream fIn = new FileInputStream(imageFile);
			Bitmap bitmap = BitmapFactory.decodeStream(fIn);
			fIn.close();
			return bitmap;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public void saveAppConfigration(AppConfiguration appConfig) {
		File saveToFile = new File(Engine.DataFolder.APP_DATA, Engine.FileName.APP_CONFIGURATION);

		try {
			saveObject(appConfig, saveToFile);

		} catch (IOException ex) {
			ex.printStackTrace();
			// else ignore exception 
		}
	}

	public AppConfiguration loadAppConfiguration() {
		AppConfiguration appConfig = null;

		try {
			appConfig = (AppConfiguration) loadOject(new File(Engine.DataFolder.APP_DATA, Engine.FileName.APP_CONFIGURATION));
		} catch (Throwable t) {
			Logger.instance().v("cachingmanager", "loadAppConfiguration - failed to load cached app configuration" + t.getClass().getSimpleName(), false);
		}

		return appConfig;
	}

	/*
	     * *****************************************************************
         * **************************  User part  **************************
         * *****************************************************************
         */
	public void saveUser(User user) {
		File saveToFile = new File(Engine.DataFolder.USER_DATA, Engine.FileName.APP_USER);

		try {
			saveObject(user, saveToFile);

		} catch (IOException ex) {
			ex.printStackTrace();
			// else ignore exception
		}
	}

	public User loadUser() {
		User user = null;

		try {
			user = (User) loadOject(new File(Engine.DataFolder.USER_DATA, Engine.FileName.APP_USER));
		} catch (Throwable t) {
			Logger.instance().v("cachingmanager", "loadAppUser - failed to load cached app user" + t.getClass().getSimpleName(), false);
		}

		return user;
	}

	public void deleteUser() {
		try {
			Engine.deleteFileRecursive(new File(Engine.DataFolder.USER_DATA, Engine.FileName.APP_USER));
		} catch (Throwable t) {
			Logger.instance().v("cachingmanager", "loadAppUser - failed to load cached app user" + t.getClass().getSimpleName(), false);
		}
	}

	/*
	 * *****************************************************************
	 * **************************  Test Part  ***************
	 * *****************************************************************
	 */
	public boolean isTestListExpired(String id, Context appContext) {
		File file = Engine.getTestFile(id, appContext);
		return isObjectCachedAndExpired(Engine.ExpiryInfo.EXPIRING_TEST_LIST, file);
	}

	public void saveTestList(ArrayList<Object> testList, String id, Context appContext) throws IOException {
		File file = Engine.getTestFile(id, appContext);
		if (testList != null && testList.size() > 0)
			saveObject(testList, file);
	}

	public ArrayList<Object> getTestList(String id, Context appContext) throws Exception {
		File file = Engine.getTestFile(id, appContext);
		ArrayList<Object> testList = null;
		try {
			testList = (ArrayList<Object>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testList;
	}
	////////// End Test List Part ///////


	/***************************************************************************************************/
	/* *************************Gallery Part*********************/
	/***************************************************************************************************/
/**Images*/
	//save image gallery
	public void saveImageGallery(ArrayList<GalleryItem> galleryItems, Context appContext)
	{
		if(galleryItems != null && galleryItems.size() > 0){
			//create file called "app_img_gallery.dat"
			// inside  folder called "images" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES,
					Engine.FileName.APP_IMG_GALLERY, appContext);

			//save Image Gallery in the file "app_img_gallery.dat
			try {
				saveObject(galleryItems, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load image gallery
	public ArrayList<GalleryItem> loadImageGallery(Context appContext)
	{
		//get file called "app_img_gallery.dat" from "images" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES, Engine.FileName.APP_IMG_GALLERY, appContext);
		ArrayList<GalleryItem> itemsList = null;
		try
		{
			itemsList = (ArrayList<GalleryItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

	//save Image Album
	public void saveImageAlbum(String albumId,ArrayList<GalleryItem> albumItems, Context appContext) {
		if(albumItems != null && albumItems.size() > 0)
		{
			//create file called "app_img_album_xx.dat"
			// inside  folder called "images" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES,
					"app_image_album_"+albumId+".dat", appContext);

			//save Image Gallery in the file "app_img_gallery.dat
			try {
				saveObject(albumItems, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load image album
	public ArrayList<GalleryItem> loadImageAlbum(Context appContext,String albumId)
	{
		//get file called "app_img_album_xx.dat" from "images" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES, "app_image_album_"+albumId+".dat", appContext);
		ArrayList<GalleryItem> itemsList = null;
		try
		{
			//load object from "app_img_album_xx.dat" file
			itemsList = (ArrayList<GalleryItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

/**Videos*/
//save video gallery
	public void saveVideoGallery(ArrayList<GalleryItem> galleryItems, Context appContext)
	{
		if(galleryItems != null && galleryItems.size() > 0){
			//create file called "app_video_gallery.dat"
			// inside "videos" folder which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_VIDEOS,
					Engine.FileName.APP_VIDEO_GALLERY, appContext);

			//save Image Gallery in the file "app_img_gallery.dat
			try {
				saveObject(galleryItems, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//load video gallery
	public ArrayList<GalleryItem> loadVideoGallery(Context appContext)
	{
//get file called "app_img_gallery.dat" from "videos" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES, Engine.FileName.APP_VIDEO_GALLERY, appContext);
		ArrayList<GalleryItem> itemsList = null;
		try
		{
			itemsList = (ArrayList<GalleryItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

	//save video album
	public void saveVideoAlbum(String albumId, ArrayList<GalleryItem> videoGalleryList, Context appContext) {
		if(videoGalleryList != null && videoGalleryList.size() > 0)
		{
			//create file called "app_video_album.dat"
			// inside  folder called "videos" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_VIDEOS,
					"app_video_album_"+albumId+".dat", appContext);

			//save Video Gallery in the file "app_video_gallery.dat
			try {
				saveObject(videoGalleryList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


//load video album
	public ArrayList<GalleryItem> loadVideoAlbum(Context appContext,String albumId)
	{
		//get file called "app_video_album_xx.dat" from "videos" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_VIDEOS, "app_video_album_" + albumId + ".dat", appContext);
		ArrayList<GalleryItem> itemsList = null;
		try
		{
			//load object from "app_video_album_xx.dat" file
			itemsList = (ArrayList<GalleryItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}



	/***************************************************************************************************/
	/* ************************* News Part *********************/
	/***************************************************************************************************/

/**categories*/
	//save news categories
	public void saveNewsCategories(ArrayList<NewsCategoryItem> itemsList, Context appContext) {
		if(itemsList != null && itemsList.size() > 0)
		{
			//create file called "app_news_categories.dat"
			// inside  folder called "News" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_NEWS,Engine.FileName.APP_NEWS_CATEGORIES, appContext);

			//save news categories in the file "app_news_categories.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load news categories
	public ArrayList<NewsCategoryItem> loadNewsCategories(Context appContext)
	{
		//get file called "app_news_categories.dat" from "News" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_NEWS,Engine.FileName.APP_NEWS_CATEGORIES, appContext);
		ArrayList<NewsCategoryItem> itemsList = null;
		try
		{
			//load object from "app_news_categories.dat" file
			itemsList = (ArrayList<NewsCategoryItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}


/**List*/
	//save news list
	public void saveNewsList(ArrayList<NewsItem> itemsList, Context appContext) {
		if(itemsList != null && itemsList.size() > 0)
		{
			//create file called "app_news_list.dat"
			// inside  folder called "News" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_NEWS,Engine.FileName.APP_NEWS_LIST, appContext);

			//save news list in the file "app_news_list.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load news list
	public ArrayList<NewsItem> loadNewsList(Context appContext)
	{
		//get file called "app_news_list.dat" from "News" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_NEWS,Engine.FileName.APP_NEWS_LIST, appContext);
		ArrayList<NewsItem> itemsList = null;
		try
		{
			//load object from "app_news_list.dat" file
			itemsList = (ArrayList<NewsItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}


/**Details*/
	//save news details
	public void saveNewDetails(NewsItem item, Context appContext) {
		if(item != null )
		{
			//create file called "app_news_details_xx.dat"
			// inside  folder called "News" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_NEWS,"app_news_details_"+item.ID+".dat", appContext);

			//save news list in the file "app_news_details_xx.dat
			try {
				saveObject(item, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load news details
	public NewsItem loadNewsDetais(String newID, Context appContext)
	{
		//get file called "app_news_details_xx.dat" from "News" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_NEWS,"app_news_details_"+newID+".dat", appContext);
		NewsItem itemsList = null;
		try
		{
			//load object from "app_news_details_xx.dat" file
			itemsList = (NewsItem) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}



	/***************************************************************************************************/
	/* ************************* Events Part *********************/
	/***************************************************************************************************/

/**cities*/
	//save Events cities
	public void saveEventsCities(ArrayList<EventsCityItem> itemsList, Context appContext) {
		if(itemsList != null && itemsList.size() > 0)
		{
			//create file called "app_events_cities.dat"
			// inside  folder called "Events" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_CITIES, appContext);

			//save Events cities in the file "app_events_cities.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load Events cities
	public ArrayList<EventsCityItem> loadEventsCities(Context appContext)
	{
		//get file called "app_events_cities.dat" from "Events" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_CITIES, appContext);
		ArrayList<EventsCityItem> itemsList = null;
		try
		{
			//load object from "app_events_cities.dat" file
			itemsList = (ArrayList<EventsCityItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

/**categories*/
	//save Events categories
	public void saveEventsCategories(ArrayList<EventsCategoryItem> itemsList, Context appContext) {
		if(itemsList != null && itemsList.size() > 0)
		{
			//create file called "app_events_categories.dat"
			// inside  folder called "Events" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_CATEGORIES, appContext);

			//save Events categories in the file "app_events_categories.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load Events categories
	public ArrayList<EventsCategoryItem> loadEventsCategories(Context appContext)
	{
		//get file called "app_events_categories.dat" from "Events" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_CATEGORIES, appContext);
		ArrayList<EventsCategoryItem> itemsList = null;
		try
		{
			//load object from "app_events_categories.dat" file
			itemsList = (ArrayList<EventsCategoryItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}


/**List*/
	//save Events list
	public void saveEventsList(ArrayList<EventItem> itemsList, Context appContext) {
		if(itemsList != null && itemsList.size() > 0)
		{
			//create file called "app_events_list.dat"
			// inside  folder called "Events" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_LIST, appContext);

			//save Events list in the file "app_events_list.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load Events list
	public ArrayList<EventItem> loadEventsList(Context appContext)
	{
		//get file called "app_events_list.dat" from "Events" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_LIST, appContext);
		ArrayList<EventItem> itemsList = null;
		try
		{
			//load object from "app_events_list.dat" file
			itemsList = (ArrayList<EventItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}


/**Details*/
	//save Events details
	public void saveEventDetails(EventItem item, Context appContext) {
		if(item != null )
		{
			//create file called "app_events_details_xx.dat"
			// inside  folder called "Events" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,"app_events_details_"+item.ID+".dat", appContext);

			//save Events list in the file "app_events_details_xx.dat
			try {
				saveObject(item, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load Events details
	public EventItem loadEventsDetails(String newID, Context appContext)
	{
		//get file called "app_events_details_xx.dat" from "Events" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,"app_events_details_"+newID+".dat", appContext);
		EventItem itemsList = null;
		try
		{
			//load object from "app_events_details_xx.dat" file
			itemsList = (EventItem) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}
}
