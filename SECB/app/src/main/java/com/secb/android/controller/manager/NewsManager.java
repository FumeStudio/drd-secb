package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.R;
import com.secb.android.model.NewsCategoryItem;
import com.secb.android.model.NewsItem;

import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsManager {
    private static NewsManager instance;
	private List<NewsItem> newsUnFilteredList   ;
	private NewsItem newDetails ;
	//news Categories List
	List<NewsCategoryItem> newsCategoryList= null;

	//map contains <Category , List on news in this category>
	HashMap<String,List<NewsItem>> newsInCategory = new HashMap<>();


	private NewsManager() {
    }
    public static NewsManager getInstance() {
        if (instance == null) {
            instance = new NewsManager();
        }
        return instance;
    }




/** Categories*/
	public List<NewsCategoryItem> getNewsCategoryList(Context context) {
		newsCategoryList=CachingManager.getInstance().loadNewsCategories(context);
		return newsCategoryList;
	}

	public void setNewsCategoryList(List<NewsCategoryItem> newsCategoryList ,Context context)
	{
		//add category called All to get all categories
		this.newsCategoryList =new ArrayList<>();
		NewsCategoryItem newsCategoryItem  =new NewsCategoryItem ();
		newsCategoryItem.ID = "All";

		newsCategoryItem.CategoryEnglish = Utilities.getStringFromLanguage(context,"en",R.string.news_filter_all_types);
		newsCategoryItem.CategoryArabic = Utilities.getStringFromLanguage(context,"ar",R.string.news_filter_all_types);

		newsCategoryItem.isSelected =true;
		this.newsCategoryList.add(newsCategoryItem);

		this.newsCategoryList.addAll(newsCategoryList);
		CachingManager.getInstance().saveNewsCategories((ArrayList<NewsCategoryItem>) this.newsCategoryList,context);

		/*//for testing add more items
		for (int i = 0 ; i < 10 ; i++){
			newsCategoryItem  =new NewsCategoryItem ();
			newsCategoryItem.ID = ""+(2*i)+5;
			newsCategoryItem.CategoryArabic = "فئة "+(i);
			newsCategoryItem.CategoryEnglish = "Category "+(i);
 			this.newsCategoryList.add(newsCategoryItem);
		}*/
	}

	public NewsCategoryItem getSelectedCategory() {
		if (newsCategoryList != null && newsCategoryList.size()>0){
			for (NewsCategoryItem iterator:newsCategoryList){
				if(iterator.isSelected){
					return iterator;
				}
			}
		}
		return null;
	}

/** List*/
	public List<NewsItem> getNewsUnFilteredList( Context context) {
		newsUnFilteredList = CachingManager.getInstance().loadNewsList(context);
		return newsUnFilteredList;
	}

	public void setNewsUnFilteredList(List<NewsItem> newsItems , Context context) {
		this.newsUnFilteredList = newsItems;
		CachingManager.getInstance().saveNewsList((ArrayList<NewsItem>) newsUnFilteredList, context);

	}


/** Details*/
	public  NewsItem getNewDetails(String newId,Context context) {
		newDetails = CachingManager.getInstance().loadNewsDetais(newId,context);
		return newDetails;
	}

	public void setNewDetails(NewsItem newsItem , Context context) {
		this.newDetails = newsItem;
		CachingManager.getInstance().saveNewDetails(this.newDetails, context);

	}



	/** Not Used */
	public List<NewsItem> getNewsListFromCategory(String newsCategory)
	{
		if(newsInCategory.size()==0 || !newsInCategory.containsKey(newsCategory))
			return null;
		return newsInCategory.get(newsCategory);
	}
	public void addNewsListToCategory(String newsCategory, List<NewsItem> newsItems) {
		newsInCategory.put(newsCategory,newsItems);
	}

}
