package com.secb.android.controller.manager;

import android.graphics.BitmapFactory;

import com.secb.android.R;
import com.secb.android.model.E_ServiceItem;
import com.secb.android.model.EventItem;
import com.secb.android.model.GalleryItem;
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

        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news1));
        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news2));
        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها. لقد قامت السعودية بتطوير المؤتمرات المنعقدة ليديها.";
        newsItem.newsItemDate = "2 days ago";

        newsList.add(newsItem);

        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news3));
        newsItem.newsItemTitle = "Saudi Exhibition and Convention";
        newsItem.newsItemDescription = "Saudi Arabia has greatly enhanced";
        newsItem.newsItemDate = "2 days ago";
        newsList.add(newsItem);

        newsItem.newsItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.news4));
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

        eventItem.eventItemImage=(BitmapFactory.decodeResource(SECBApplication.getContext().getResources(), R.drawable.event_card_img));
        eventItem.eventItemTitle = CTApplication.getContext().getString(R.string.dev_name);
        eventItem.eventItemDescription = CTApplication.getContext().getString(R.string.dev_desc);
        eventItem.eventItemTime = "2 days ago";
        eventItem.eventItemCategory = "org";
        eventItem.eventItemDuration = "halfday";
        eventItem.eventItemRepeating = "rep";
        eventItem.eventItemLocation = "jadda";
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



    public static ArrayList<GalleryItem> getAlbumList(int galleryType, int id)
    {
        ArrayList<GalleryItem> listViewItems = new ArrayList<>();
        GalleryItem galleryItem ;
        if (id > 0) {
            galleryItem = new GalleryItem(galleryType);
            galleryItem.imgResource = id;
            for (int i = 0; i < 12; i++) {
                listViewItems.add(galleryItem);
            }
        }
        return listViewItems;
    }


    public static ArrayList<GalleryItem> getGalleryList(int galaryType)
    {
        ArrayList<GalleryItem> listViewItems = new ArrayList<>();


        GalleryItem galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.one;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.two;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.three;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.four;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.five;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.six;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.seven;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        galleryItem = new GalleryItem(galaryType);
        galleryItem.imgResource =  R.drawable.eight;
        galleryItem.galleryItemTitle = "Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title Album Title ";
        listViewItems.add(galleryItem);

        return listViewItems;
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
