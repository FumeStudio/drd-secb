package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.R;
import com.secb.android.model.EGuideLocationTypeItem;
import com.secb.android.model.EventItem;
import com.secb.android.model.LocationItem;

import net.comptoirs.android.common.view.CTApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EGuideLocationManager {
    private static EGuideLocationManager instance;
	private List<LocationItem> locationUnFilteredList;
	//news Categories List
	List<EGuideLocationTypeItem> locationTypesList= null;

	//map contains <Category , List on events in this category>
	HashMap<String,List<EventItem>> eventsInCategory = new HashMap<>();

	private EventItem eventDetails;

	private EGuideLocationManager() {
    }
    public static EGuideLocationManager getInstance() {
        if (instance == null) {
            instance = new EGuideLocationManager();
        }
        return instance;
    }

/** Cities*/
	//go to EventManager

/** Types*/
	public List<EGuideLocationTypeItem> getLocationTypesList(Context context) {
		this.locationTypesList = CachingManager.getInstance().loadEguideLocationTypes(context);
		return locationTypesList;
	}

	public void setLocationTypesList(List<EGuideLocationTypeItem> itemsList, Context context)
	{
		//add category called All to get all categories
		this.locationTypesList =new ArrayList<>();
		EGuideLocationTypeItem item  =new EGuideLocationTypeItem ();
		item.ID = "All";
		item.SiteTypeEnglish = CTApplication.getContext().getResources().getString(R.string.news_filter_all_types);
		item.SiteTypeArabic ="الكل";
		item.isSelected =true;
		this.locationTypesList.add(item);

		this.locationTypesList.addAll(itemsList);

		CachingManager.getInstance().saveEguideLocationTypes((ArrayList<EGuideLocationTypeItem>) this.locationTypesList, context);
	}

	public EGuideLocationTypeItem getSelectedLocationType() {
		if (locationTypesList != null && locationTypesList.size()>0){
			for (EGuideLocationTypeItem iterator:locationTypesList){
				if(iterator.isSelected){
					return iterator;
				}
			}
		}
		return null;
	}


/** List*/
	public List<LocationItem> getLocationsUnFilteredList(Context context) {
		locationUnFilteredList =CachingManager.getInstance().loadLocationsList(context);
		return locationUnFilteredList;
	}

	public void setLocationsUnFilteredList(List<LocationItem> eventItems, Context context) {
		this.locationUnFilteredList = eventItems;
		CachingManager.getInstance().saveLocationsList((ArrayList<LocationItem>) this.locationUnFilteredList,context);
	}




//currently not used because there is no network operation to get event details
//and the event item is sent in a bundle from EventList fragment to EventDetails Fragment
/** Details*/
	public EventItem getEventDetails(String eventId,Context context) {
		eventDetails = CachingManager.getInstance().loadEventsDetails(eventId, context);
		return eventDetails;
	}

	public void setEventDetails(EventItem eventItem , Context context) {
		this.eventDetails = eventItem;
		CachingManager.getInstance().saveEventDetails(this.eventDetails, context);

	}


/** Not Used*/
	public void addEventsListToCategory(String newsCategory, List<EventItem> newsItems) {
		eventsInCategory.put(newsCategory, newsItems);
	}

	public List<EventItem> getEventsListFromCategory(String eventsCategory)
	{
		if(eventsInCategory.size()==0 || !eventsInCategory.containsKey(eventsCategory))
			return null;
		return eventsInCategory.get(eventsCategory);
	}

}
