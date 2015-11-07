package com.secb.android.controller.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.secb.android.model.AppConfiguration;
import com.secb.android.model.EGuideLocationTypeItem;
import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.model.E_ServiceRequestTypeItem;
import com.secb.android.model.E_ServiceRequestWorkSpaceModeItem;
import com.secb.android.model.E_ServiceStatisticsItem;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsCategoryItem;
import com.secb.android.model.EventsCityItem;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsCategoryItem;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.model.User;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


//delete contents of cache folder
	public void clearCachingFolder(Context context)
	{
		try {
			Engine.deleteFileRecursive(Engine.getCacheRootDir(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			String language = UiEngine.getCurrentAppLanguage(appContext);
			String img_gallery = Engine.FileName.APP_IMG_GALLERY;
			if(language!=null)
				img_gallery=img_gallery+"_"+language.toUpperCase();
			File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES,
					img_gallery, appContext);

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
//		File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES, Engine.FileName.APP_IMG_GALLERY, appContext);
		String language = UiEngine.getCurrentAppLanguage(appContext);
		String img_gallery = Engine.FileName.APP_IMG_GALLERY;
		if(language!=null)
			img_gallery=img_gallery+"_"+language.toUpperCase();
		File file = Engine.getCacheFile(Engine.DataFolder.USER_IMAGES,
				img_gallery, appContext);

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
//			File file = Engine.getCacheFile(Engine.DataFolder.USER_VIDEOS,
//					Engine.FileName.APP_VIDEO_GALLERY, appContext);

			String language = UiEngine.getCurrentAppLanguage(appContext);
			String vid_gallery = Engine.FileName.APP_VIDEO_GALLERY;
			if(language!=null)
				vid_gallery=vid_gallery+"_"+language.toUpperCase();
			File file = Engine.getCacheFile(Engine.DataFolder.USER_VIDEOS,
					vid_gallery, appContext);


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
//		File file = Engine.getCacheFile(Engine.DataFolder.USER_VIDEOS, Engine.FileName.APP_VIDEO_GALLERY, appContext);

		String language = UiEngine.getCurrentAppLanguage(appContext);
		String vid_gallery = Engine.FileName.APP_VIDEO_GALLERY;
		if(language!=null)
			vid_gallery=vid_gallery+"_"+language.toUpperCase();
		File file = Engine.getCacheFile(Engine.DataFolder.USER_VIDEOS,
				vid_gallery, appContext);
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
			Object object = loadOject(file);
			if(object != null)
				itemsList = (ArrayList<NewsItem>) object;
		} catch (EOFException e) {
            e.printStackTrace();
        } catch (Throwable e) {
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
//			File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_LIST, appContext);


			String language = UiEngine.getCurrentAppLanguage(appContext);
			String events_list_file = Engine.FileName.APP_EVENTS_LIST;
			if(language!=null)
				events_list_file =events_list_file +"_"+language.toUpperCase();
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,
					events_list_file , appContext);


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
//		File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,Engine.FileName.APP_EVENTS_LIST, appContext);


		String language = UiEngine.getCurrentAppLanguage(appContext);
		String events_list_file = Engine.FileName.APP_EVENTS_LIST;
		if(language!=null)
			events_list_file =events_list_file +"_"+language.toUpperCase();
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EVENTS,
				events_list_file , appContext);


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



	/***************************************************************************************************/
	/* ************************* EGuide Location Part *********************/
	/***************************************************************************************************/

/**Types*/
	//save Location types
	public void saveEguideLocationTypes(ArrayList<EGuideLocationTypeItem> itemsList, Context appContext) {
		if(itemsList != null && itemsList.size() > 0)
		{
			//create file called "app_eguide_location_types.dat"
			// inside  folder called "locations" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION,Engine.FileName.APP_EGUIDE_LOCATION_TYPES, appContext);

			//save Events categories in the file "app_events_categories.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load Location types
	public ArrayList<EGuideLocationTypeItem> loadEguideLocationTypes(Context appContext)
	{
		//get file called "app_eguide_location_types.dat" from "locations" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION,Engine.FileName.APP_EGUIDE_LOCATION_TYPES, appContext);
		ArrayList<EGuideLocationTypeItem> itemsList = null;
		try
		{
			//load object from "app_eguide_location_types.dat" file
			itemsList = (ArrayList<EGuideLocationTypeItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

/**Lists*/
	//save Location list
	public void saveLocationsList(ArrayList<LocationItem> itemsList, Context appContext) {
		if (itemsList != null && itemsList.size() > 0) {
			//create file called "app_eguide_location_list.dat"
			// inside  folder called "locations" which existing in "Cache" folder
//			File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION, Engine.FileName.APP_EGUIDE_LOCATION_LIST, appContext);

			String language = UiEngine.getCurrentAppLanguage(appContext);
			String location_list_file = Engine.FileName.APP_EGUIDE_LOCATION_LIST;
			if(language!=null)
				location_list_file=location_list_file+"_"+language.toUpperCase();
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION,
					location_list_file, appContext);



			//save Events categories in the file "app_eguide_location_types.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//load Location list
	public ArrayList<LocationItem> loadLocationsList(Context appContext)
	{
		//get file called "app_eguide_location_list.dat" from "locations" folder
//		File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION, Engine.FileName.APP_EGUIDE_LOCATION_LIST, appContext);

		String language = UiEngine.getCurrentAppLanguage(appContext);
		String location_list_file = Engine.FileName.APP_EGUIDE_LOCATION_LIST;
		if(language!=null)
			location_list_file=location_list_file+"_"+language.toUpperCase();
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION,
				location_list_file, appContext);

		ArrayList<LocationItem> itemsList = null;
		try
		{
			//load object from "app_eguide_location_types.dat" file
			itemsList = (ArrayList<LocationItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}



/**Details*/
	//save Location details
	public void saveLocationDetails(LocationItem item, Context appContext) {
		if(item != null )
		{
			//create file called "app_events_details_xx.dat"
			// inside  folder called "Events" which existing in "Cache" folder
			String language = UiEngine.getCurrentAppLanguage(appContext);
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION,"app_eguide_location_details_"+item.ID+"_"+language.toUpperCase()+".dat", appContext);

			//save Events list in the file "app_events_details_xx.dat
			try {
				saveObject(item, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load Location details
	public LocationItem loadLocationDetails(String LoocationID, Context appContext)
	{
		//get file called "app_events_details_xx.dat" from "Events" folder
		String language = UiEngine.getCurrentAppLanguage(appContext);
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_LOCATION,"app_eguide_location_details_"+LoocationID+"_"+language.toUpperCase()+".dat", appContext);
		LocationItem itemsDetails = null;
		try
		{
			//load object from "app_events_details_xx.dat" file
			itemsDetails = (LocationItem) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsDetails;
	}



	/***************************************************************************************************/
	/* ************************* EGuide Organizers Part *********************/
	/***************************************************************************************************/

/**Lists*/
	//save organizers list
	public void saveOrganizersList(ArrayList<OrganizerItem> itemsList, Context appContext)
	{
		if (itemsList != null && itemsList.size() > 0) {
			//create file called "app_eguide_organizers_list.dat"
			// inside  folder called "organizers" which existing in "Cache" folder
//			File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_ORGANIZERS, Engine.FileName.APP_EGUIDE_ORGANIZERS_LIST, appContext);
			//save Events categories in the file "app_eguide_organizers_list.dat

			String language = UiEngine.getCurrentAppLanguage(appContext);
			String organizers_list_file = Engine.FileName.APP_EGUIDE_ORGANIZERS_LIST;
			if(language!=null)
				organizers_list_file=organizers_list_file+"_"+language.toUpperCase();
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_ORGANIZERS,
					organizers_list_file, appContext);
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//load organizers list
	public ArrayList<OrganizerItem> loadOrganizersList(Context appContext)
	{
		//get file called "app_eguide_location_list.dat" from "organizers" folder
//		File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_ORGANIZERS, Engine.FileName.APP_EGUIDE_ORGANIZERS_LIST, appContext);
		ArrayList<OrganizerItem> itemsList = null;

		String language = UiEngine.getCurrentAppLanguage(appContext);
		String organizers_list_file = Engine.FileName.APP_EGUIDE_ORGANIZERS_LIST;
		if(language!=null)
			organizers_list_file=organizers_list_file+"_"+language.toUpperCase();
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_ORGANIZERS,
				organizers_list_file, appContext);

		try
		{
			//load object from "app_eguide_organizers_list.dat" file
			itemsList = (ArrayList<OrganizerItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}



/**Details*/
	//save organizer details
	public void saveOrganizerDetails(OrganizerItem item, Context appContext) {
		if(item != null )
		{
			//create file called "app_eguide_organizer_details_xx.dat"
			// inside  folder called "Organizers" which existing in "Cache" folder

			String language = UiEngine.getCurrentAppLanguage(appContext);
			File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_ORGANIZERS, "app_eguide_organizer_details_" + item.OrganizerEmail + "_" + language.toUpperCase() + ".dat", appContext);


			//save Events list in the file "app_eguide_organizer_details_xx.dat
			try {
				saveObject(item, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//load organizer details
	public OrganizerItem loadOrganizerDetails(String OrganizerEmail, Context appContext)
	{
		//get file called "app_eguide_organizer_details_xx.dat" from "Organizers" folder

		String language = UiEngine.getCurrentAppLanguage(appContext);
		File file = Engine.getCacheFile(Engine.DataFolder.USER_EGUIDE_ORGANIZERS, "app_eguide_organizer_details_" + OrganizerEmail +"_"+language.toUpperCase()+ ".dat", appContext);
		OrganizerItem itemsDetails = null;
		try
		{
			//load object from "app_eguide_organizer_details_xx.dat" file
			itemsDetails = (OrganizerItem) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsDetails;
	}


	/***************************************************************************************************/
	/* ************************* E-Services Organizers Part *********************/
	/***************************************************************************************************/

/** RequestTypesList*/
	//save e-services requests list
	public void saveE_ServicesRequestsTypesList(ArrayList<E_ServiceRequestTypeItem> itemsList, Context appContext)
	{
		if (itemsList != null && itemsList.size() > 0) {
			//create file called "app_eservices_requests_list.dat"
			// inside  folder called "eservices" which existing in "Cache" folder
			String language = UiEngine.getCurrentAppLanguage(appContext);
			String list_file = Engine.FileName.APP_EGUIDE_E_SERVICES_REQUESTS_TYPES_LIST;
			if(language!=null)
				list_file=list_file+"_"+language.toUpperCase();
			File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services,
					list_file, appContext);

//			File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services, Engine.FileName.APP_EGUIDE_E_SERVICES_REQUESTS_LIST, appContext);
			//save Events categories in the file "app_eservices_requests_list.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//load e-services requests list
	public ArrayList<E_ServiceRequestTypeItem> loadE_ServicesRequestsTypesList(Context appContext)
	{
		//get file called "app_eservices_statistics_list.dat" from "eservices" folder
		String language = UiEngine.getCurrentAppLanguage(appContext);
		String list_file = Engine.FileName.APP_EGUIDE_E_SERVICES_REQUESTS_TYPES_LIST;
		if(language!=null)
			list_file=list_file+"_"+language.toUpperCase();
		File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services,
				list_file, appContext);
		ArrayList<E_ServiceRequestTypeItem> itemsList=null;
		try
		{
			//load object from "app_eservices_statistics_list.dat" file
			itemsList = (ArrayList<E_ServiceRequestTypeItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

/** RequestWorkSpaceModeList*/
	//save e-services requests list
	public void saveE_ServicesRequestsWorkSpaceModesList(ArrayList<E_ServiceRequestWorkSpaceModeItem> itemsList, Context appContext)
	{
		if (itemsList != null && itemsList.size() > 0) {
			//create file called "app_eservices_requests_list.dat"
			// inside  folder called "eservices" which existing in "Cache" folder
			String list_file = Engine.FileName.APP_EGUIDE_E_SERVICES_REQUESTS_WORKSPACEMODE_LIST;
			File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services,
					list_file, appContext);
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//load e-services requests list
	public ArrayList<E_ServiceRequestWorkSpaceModeItem> loadE_ServicesRequestsWorkSpaceModesList(Context appContext)
	{
		//get file called "app_eservices_statistics_list.dat" from "eservices" folder
		String list_file = Engine.FileName.APP_EGUIDE_E_SERVICES_REQUESTS_WORKSPACEMODE_LIST;

		File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services,
				list_file, appContext);
		ArrayList<E_ServiceRequestWorkSpaceModeItem> itemsList=null;
		try
		{
			//load object from "app_eservices_statistics_list.dat" file
			itemsList = (ArrayList<E_ServiceRequestWorkSpaceModeItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

/**RequestList*/
	//save e-services requests list
	public void saveE_ServicesRequestsList(ArrayList<E_ServiceRequestItem> itemsList, Context appContext) {
		if (itemsList != null && itemsList.size() > 0) {
			//create file called "app_eservices_requests_list.dat"
			// inside  folder called "eservices" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services, Engine.FileName.APP_EGUIDE_E_SERVICES_REQUESTS_LIST, appContext);
			//save Events categories in the file "app_eservices_requests_list.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//load e-services requests list
	public ArrayList<E_ServiceRequestItem> loadE_ServicesRequestsList(Context appContext)
	{
		//get file called "app_eservices_requests_list.dat" from "eservices" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services, Engine.FileName.APP_EGUIDE_E_SERVICES_REQUESTS_LIST, appContext);
		ArrayList<E_ServiceRequestItem> itemsList = null;
		try
		{
			//load object from "app_eservices_requests_list.dat" file
			itemsList = (ArrayList<E_ServiceRequestItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

/** StatisticsList*/
	//save e-services Statistics list
	public void saveE_ServicesStatisticsList(ArrayList<E_ServiceStatisticsItem> itemsList, Context appContext) {
		if (itemsList != null && itemsList.size() > 0) {
			//create file called "app_eservices_statistics_list.dat"
			// inside  folder called "eservices" which existing in "Cache" folder
			File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services, Engine.FileName.APP_EGUIDE_E_SERVICES_STATISTICS_LIST, appContext);
			//save Services Statistics in the file "app_eservices_statistics_list.dat
			try {
				saveObject(itemsList, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//load e-services Statistics list
	public List<E_ServiceStatisticsItem> loadE_ServicesStatisticsList(Context appContext)
	{
		//get file called "app_eservices_statistics_list.dat" from "eservices" folder
		File file = Engine.getCacheFile(Engine.DataFolder.USER_E_Services, Engine.FileName.APP_EGUIDE_E_SERVICES_STATISTICS_LIST, appContext);
		ArrayList<E_ServiceStatisticsItem> itemsList = null;
		try
		{
			//load object from "app_eservices_statistics_list.dat" file
			itemsList = (ArrayList<E_ServiceStatisticsItem>) loadOject(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemsList;
	}

}
