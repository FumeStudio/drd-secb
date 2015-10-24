package com.secb.android.controller.manager;

import android.graphics.BitmapFactory;

import com.secb.android.R;
import com.secb.android.model.E_ServiceItem;
import com.secb.android.model.EventItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.SECBApplication;

import net.comptoirs.android.common.view.CTApplication;

import java.util.ArrayList;

/*
* this class is used to get development data for testing*/
public class DevData
{
    public static ArrayList<NewsItem> getNewsList(){

        ArrayList<NewsItem> newsList =new ArrayList<>();
        NewsItem newsItem = new NewsItem();

//        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news1));
        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "Saudi Arabia has greatly enhanced";
        newsItem.CreationDate = "2 days ago";

        newsList.add(newsItem);

//        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news2));
        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.CreationDate = "2 days ago";

        newsList.add(newsItem);

//        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news3));
        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "Saudi Arabia has greatly enhanced";
        newsItem.CreationDate = "2 days ago";
        newsList.add(newsItem);

//        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news4));
        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.CreationDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "Saudi Arabia has greatly enhanced";
        newsItem.CreationDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.CreationDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "Saudi Arabia has greatly enhanced";
        newsItem.CreationDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.CreationDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "Saudi Arabia has greatly enhanced";
        newsItem.CreationDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.Title = "Saudi Exhibition and Convention";
        newsItem.NewsBody = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.CreationDate = "2 days ago";

        newsList.add(newsItem);
        return  newsList;
    }

    public static ArrayList<EventItem> getEventsList() {
        ArrayList<EventItem> eventsList =new ArrayList<>();
        EventItem eventItem= new EventItem();

//        eventItem.eventItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.event_card_img));
        eventItem.Title = CTApplication.getContext().getString(R.string.dev_name);
        eventItem.Description = CTApplication.getContext().getString(R.string.dev_desc);
        eventItem.EventDate = "2 days ago";
        eventItem.EventCategory = "org";
        eventItem.IsAllDayEvent = "true";
        eventItem.IsRecurrence = "false";
        eventItem.EventSiteCity = "jadda";
        eventItem.eventItemLatitude=30.0882739;
        eventItem.eventItemLongitude=31.3146007;


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
        organizerItem.OraganizerItemTitle = CTApplication.getContext().getString(R.string.dev_name);
        organizerItem.OraganizerItemAddress= CTApplication.getContext().getString(R.string.dev_address);
        organizerItem.OraganizerItemDescription= CTApplication.getContext().getString(R.string.dev_desc);
        organizerItem.OraganizerItemEmail="email@gmail.com";
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

        locationItem.LoccationItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news3));
        locationItem.LoccationItemTitle = CTApplication.getContext().getString(R.string.dev_name);
        locationItem.LoccationItemAddress= CTApplication.getContext().getString(R.string.dev_address);
        locationItem.LoccationItemDescription=CTApplication.getContext().getString(R.string.dev_desc);
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




    public static ArrayList<E_ServiceItem> getE_ServicesList() {
        ArrayList<E_ServiceItem> e_serviceItems = new ArrayList<>();
        E_ServiceItem item = new E_ServiceItem();

        item.title = "Nine Firms Warned for Committing Violations.";
        item.date = "20 Oct 2015";
        item.status = "In progress";
        item.number = "21-360406-85769";
        item.type = "Organization Service Request";

        e_serviceItems.add(item);
        e_serviceItems.add(item);
        e_serviceItems.add(item);
        e_serviceItems.add(item);
        e_serviceItems.add(item);
        e_serviceItems.add(item);
        e_serviceItems.add(item);

        return e_serviceItems;
    }
}
