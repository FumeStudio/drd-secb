package com.secb.android.controller.manager;

import com.secb.android.R;
import com.secb.android.model.NewsCategoryItem;
import com.secb.android.model.NewsItem;

import net.comptoirs.android.common.view.CTApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsManager {
    private static NewsManager instance;
	private List<NewsItem> newsUnFilteredList   ;
	//news Categories List
	List<NewsCategoryItem> newsCategoryList= null;
	List<String> newsEnglishCategoryList= null;
	List<String> newsArabichCategoryList= null;

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
	public List<NewsCategoryItem> getNewsCategoryList() {
		return newsCategoryList;
	}

	public void setNewsCategoryList(List<NewsCategoryItem> newsCategoryList)
	{
		//add category called All to get all categories
		this.newsCategoryList =new ArrayList<>();
		NewsCategoryItem newsCategoryItem  =new NewsCategoryItem ();
		newsCategoryItem.ID = "All";
		newsCategoryItem.CategoryArabic = CTApplication.getContext().getResources().getString(R.string.news_filter_all_types);
		newsCategoryItem.CategoryEnglish = CTApplication.getContext().getResources().getString(R.string.news_filter_all_types);
		this.newsCategoryList.add(newsCategoryItem);

		this.newsCategoryList.addAll(newsCategoryList);

		/*//for testing add more items
		for (int i = 0 ; i < 10 ; i++){
			newsCategoryItem  =new NewsCategoryItem ();
			newsCategoryItem.ID = ""+(2*i)+5;
			newsCategoryItem.CategoryArabic = "فئة "+(i);
			newsCategoryItem.CategoryEnglish = "Category "+(i);
 			this.newsCategoryList.add(newsCategoryItem);
		}*/
	}

	public List<String> getNewsEnglishCategoryList() {
		return newsEnglishCategoryList;
	}

	public void setNewsEnglishCategoryList(List<String> newsEnglishCategoryList) {
		this.newsEnglishCategoryList = newsEnglishCategoryList;
	}

	public List<String> getNewsArabichCategoryList() {
		return newsArabichCategoryList;
	}

	public void setNewsArabichCategoryList(List<String> newsArabichCategoryList) {
		this.newsArabichCategoryList = newsArabichCategoryList;
	}

	public void addNewsListToCategory(String newsCategory, List<NewsItem> newsItems) {
		newsInCategory.put(newsCategory,newsItems);
	}

	public List<NewsItem> getNewsListFromCategory(String newsCategory)
	{
		if(newsInCategory.size()==0 || !newsInCategory.containsKey(newsCategory))
			return null;
		return newsInCategory.get(newsCategory);
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

	public List<NewsItem> getNewsUnFilteredList() {
		return newsUnFilteredList;
	}

	public void setNewsUnFilteredList(List<NewsItem> newsItems) {
		this.newsUnFilteredList = newsItems;
	}

}
