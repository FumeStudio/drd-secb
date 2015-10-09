package com.secb.android.controller.manager;

import android.graphics.BitmapFactory;

import com.secb.android.R;
import com.secb.android.model.EventItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.SECBApplication;

import java.util.ArrayList;

/*
* this class is used to get development data for testing*/
public class DevData
{
    public static ArrayList<NewsItem> getNewsList(){

        ArrayList<NewsItem> newsList =new ArrayList<>();
        NewsItem newsItem = new NewsItem();

        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news_img_sample));
        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);
        return  newsList;
    }


    public static ArrayList<EventItem> getEventsList() {
        ArrayList<EventItem> eventsList =new ArrayList<>();
        EventItem eventItem= new EventItem();

        eventItem.eventItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news_img_sample));
        eventItem.eventItemTitle = "event Saudi Exhibition and Convention";
        eventItem.eventItemDescription = "event لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        eventItem.eventItemTime = "event 2 days ago";
        eventItem.eventItemCategory = "categoryyyyyyy";
        eventItem.eventItemDuration = "halfday";
        eventItem.eventItemRepeating = "rep";
        eventItem.eventItemLocation = "catroooo";

        eventsList.add(eventItem);
        eventsList.add(eventItem);
        eventsList.add(eventItem);
        eventsList.add(eventItem);
        eventsList.add(eventItem);

        return eventsList;
    }

    public static ArrayList<OrganizerItem> getOrganizersList()
    {
        ArrayList<OrganizerItem> organizerItems= new ArrayList<>();
        OrganizerItem organizerItem = new OrganizerItem();

        organizerItem.OraganizerItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news_img_sample));
        organizerItem.OraganizerItemTitle ="Name Name Name";
        organizerItem.OraganizerItemAddress="Address Address Address Address ";
        organizerItem.OraganizerItemDescription="Description Description Description Description Description ";
        organizerItem.OraganizerItemEmail="Email Email Email Email Email Email ";
        organizerItem.OraganizerItemPhone="012222233555-012258554555";
        organizerItem.OraganizerItemWebsite="www.linkedin/namename.com";

        organizerItems.add(organizerItem);
        organizerItems.add(organizerItem);
        organizerItems.add(organizerItem);
        organizerItems.add(organizerItem);
        organizerItems.add(organizerItem);
        return organizerItems;
    }

    public static ArrayList<LocationItem> getLocationsList() {
        ArrayList<LocationItem> locationItems= new ArrayList<>();
        LocationItem locationItem = new LocationItem();

        locationItem.LoccationItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news_img_sample));
        locationItem.LoccationItemTitle ="Name Name Name";
        locationItem.LoccationItemAddress="Address Address Address Address ";
        locationItem.LoccationItemDescription="Description Description Description Description Description ";
        locationItem.LoccationItemEmail="Email Email Email Email Email Email ";
        locationItem.LoccationItemPhone="012222233555-012258554555";


        locationItem.LoccationItemType="Type 1 ";
        locationItem.LoccationItemCapacity= 120;
        locationItem.LoccationItemSpace=2250;
        locationItem.LoccationItemRoomType="room 1 ";
        locationItem.LoccationItemRoomCapacity = 50;
        locationItem.LoccationItemRoomSpace = 500;
        locationItem.LoccationItemNumberOrRooms= 250;

        locationItems.add(locationItem);
        locationItems.add(locationItem);
        locationItems.add(locationItem);
        locationItems.add(locationItem);
        locationItems.add(locationItem);
        locationItems.add(locationItem);
        return locationItems;

    }
}
