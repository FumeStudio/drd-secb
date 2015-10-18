package com.secb.android.controller.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.secb.android.model.AppConfiguration;
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
	public static CachingManager getInstance(){
		if(self == null){
			self = new CachingManager();
		}
		return self;
	}
	
	
	protected CachingManager(){
		//contentCachingDirectory = MazikaController.getLayoutView().getAppContext().getDir(FOLDER_NAME_CACHED_DATA, Context.MODE_protected);
		//albumCachingDirectory = MazikaController.getLayoutView().getAppContext().getDir(FOLDER_NAME_CACHED_ALBUMS, Context.MODE_protected);
		//categoryCachingDirectory = MazikaController.getLayoutView().getAppContext().getDir(FOLDER_NAME_CACHED_CATEGORY_ALBUMS, Context.MODE_protected);
	}
	
	protected boolean isObjectCachedAndNotExpired(long expireInHours, File objectFile) {
		boolean exist, expired = false;
		exist = objectFile.exists();

		if(exist) {
			if((objectFile.lastModified() + expireInHours * 60 * 60 * 1000) < new Date().getTime()) {
				expired = true;
			}
		}
		return exist && !expired;
	}
	
	protected boolean isObjectCachedAndExpired(long expireInHours, File objectFile) {
		boolean exist, expired = false;
		exist = objectFile.exists();

		if(expireInHours <= 0)
			expired = true;
		else if(exist) {
			if((objectFile.lastModified() + expireInHours * 60 * 60 * 1000) < new Date().getTime()) {
				expired = true;
			}
		}
		return exist && expired;
	}

	
	public static void saveObject(Serializable object, File objectFile) throws IOException{
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(objectFile));
		outputStream.writeObject(object);
		objectFile.setLastModified(new Date().getTime());
		outputStream.close();
		
		objectFile.setLastModified(new Date().getTime());  
	}
	
	public static Serializable loadOject(File objectFile) throws Exception{
		Object cachedObject = null;
		if(objectFile.exists()){
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
			if(!imageFile.exists()) return null;       
			FileInputStream fIn	= new FileInputStream(imageFile);
			Bitmap bitmap	= BitmapFactory.decodeStream(fIn);
			fIn.close();
			return bitmap;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveAppConfigration(AppConfiguration appConfig){
		File saveToFile = new File(Engine.DataFolder.APP_DATA, Engine.FileName.APP_CONFIGURATION);
		
		try{
			saveObject(appConfig, saveToFile);	
			
		}catch (IOException ex){
			ex.printStackTrace();
			// else ignore exception 
		}
	}
	
	public AppConfiguration loadAppConfiguration(){
		AppConfiguration appConfig = null;
		
		try{
			appConfig = (AppConfiguration) loadOject(new File(Engine.DataFolder.APP_DATA, Engine.FileName.APP_CONFIGURATION));
		}catch (Throwable t){
			Logger.instance().v("cachingmanager", "loadAppConfiguration - failed to load cached app configuration" + t.getClass().getSimpleName(), false);
		}
		
		return appConfig;
	}

	/*
         * *****************************************************************
         * **************************  User part  **************************
         * *****************************************************************
         */
	public void saveUser(User user){
		File saveToFile = new File(Engine.DataFolder.USER_DATA, Engine.FileName.APP_USER);

		try{
			saveObject(user, saveToFile);

		}catch (IOException ex){
			ex.printStackTrace();
			// else ignore exception
		}
	}
	public User loadUser(){
		User user = null;

		try{
			user = (User) loadOject(new File(Engine.DataFolder.USER_DATA, Engine.FileName.APP_USER));
		}catch (Throwable t){
			Logger.instance().v("cachingmanager", "loadAppUser - failed to load cached app user" + t.getClass().getSimpleName(), false);
		}

		return user;
	}
	public void deleteUser(){
		try{
			Engine.deleteFileRecursive(new File(Engine.DataFolder.USER_DATA, Engine.FileName.APP_USER));
		}catch (Throwable t){
			Logger.instance().v("cachingmanager", "loadAppUser - failed to load cached app user" + t.getClass().getSimpleName(), false);
		}
	}

	/*
	 * *****************************************************************
	 * **************************  Test Part  ***************
	 * *****************************************************************
	 */
	public boolean isTestListExpired(String id, Context appContext)
	{
		File file = Engine.getTestFile(id, appContext);
		return isObjectCachedAndExpired(Engine.ExpiryInfo.EXPIRING_TEST_LIST, file);
	}
	
	public void saveTestList(ArrayList<Object> testList, String id, Context appContext) throws IOException
	{
		File file = Engine.getTestFile(id, appContext);
		if(testList != null && testList.size() > 0)
			saveObject(testList, file);
	}
	
	public ArrayList<Object> getTestList(String id, Context appContext) throws Exception
	{
		File file = Engine.getTestFile(id, appContext);
		ArrayList<Object> testList = null;
		try {
			testList = (ArrayList<Object>)loadOject(file);
		}
	  catch (Exception e)
	  { 	e.printStackTrace();    }
		return testList;
	}
	////////// End Test List Part ///////
}
