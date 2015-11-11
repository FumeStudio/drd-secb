package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.R;
import com.secb.android.model.EventItem;
import com.secb.android.model.EventsCategoryItem;
import com.secb.android.model.EventsCityItem;
import com.secb.android.view.MainActivity;

import net.comptoirs.android.common.helper.Utilities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EventsManager {
    private static EventsManager instance;
	private List<EventItem> eventsUnFilteredList   ;
	//news Categories List
	List<EventsCategoryItem> eventsCategoryList= null;

	//map contains <Category , List on events in this category>
	HashMap<String,List<EventItem>> eventsInCategory = new HashMap<>();
	private List<EventsCityItem> eventsCityItems;
	private EventItem eventDetails;


	private ArrayList<EventItem> monthEventsList;

	private EventsManager() {
    }
    public static EventsManager getInstance() {
        if (instance == null) {
            instance = new EventsManager();
        }
        return instance;
    }

/** Cities*/
	public void setEventsCityList(List<EventsCityItem> eventsCityItems, Context context)
	{
		this.eventsCityItems=new ArrayList<>();
		//add the item "All"
		this.eventsCityItems.add(new EventsCityItem());
		this.eventsCityItems .addAll(eventsCityItems);

		CachingManager.getInstance().saveEventsCities((ArrayList<EventsCityItem>) this.eventsCityItems, context);
	}

	public List<EventsCityItem>  getEventsCityList(Context context)
	{
		this.eventsCityItems = CachingManager.getInstance().loadEventsCities(context);
		return this.eventsCityItems;
	}

/** Categories*/
	public List<EventsCategoryItem> getEventsCategoryList(Context context) {
		this.eventsCategoryList = CachingManager.getInstance().loadEventsCategories(context);
		return eventsCategoryList;
	}

	public void setEventsCategoryList(List<EventsCategoryItem> eventsCategoryList, Context context)
	{
		//add category called All to get all categories
		this.eventsCategoryList =new ArrayList<>();
		EventsCategoryItem eventsCategoryItem  =new EventsCategoryItem ();
		eventsCategoryItem.ID = "All";

		eventsCategoryItem.TitleEnglish = Utilities.getStringFromLanguage(context, "en", R.string.news_filter_all_types);
		eventsCategoryItem.TitleArabic = Utilities.getStringFromLanguage(context, "ar", R.string.news_filter_all_types);


		eventsCategoryItem.isSelected =true;
		this.eventsCategoryList.add(eventsCategoryItem);

		this.eventsCategoryList.addAll(eventsCategoryList);

		/*//for testing add more items
		for (int i = 0 ; i < 10 ; i++){
			newsCategoryItem  =new NewsCategoryItem ();
			newsCategoryItem.ID = ""+(2*i)+5;
			newsCategoryItem.CategoryArabic = "فئة "+(i);
			newsCategoryItem.CategoryEnglish = "Category "+(i);
 			this.eventsCategoryList.add(newsCategoryItem);
		}*/

		CachingManager.getInstance().saveEventsCategories((ArrayList<EventsCategoryItem>) this.eventsCategoryList,context);
	}

	public EventsCategoryItem getSelectedCategory() {
		if (eventsCategoryList != null && eventsCategoryList.size()>0){
			for (EventsCategoryItem iterator:eventsCategoryList){
				if(iterator.isSelected){
					return iterator;
				}
			}
		}
		return null;
	}


/** List*/
	public List<EventItem> getEventsUnFilteredList(Context context) {
		eventsUnFilteredList=CachingManager.getInstance().loadEventsList(context);
		return eventsUnFilteredList;
	}

	public void setEventsUnFilteredList(List<EventItem> eventItems, Context context) {
		this.eventsUnFilteredList = eventItems;
		CachingManager.getInstance().saveEventsList((ArrayList<EventItem>) this.eventsUnFilteredList,context);
	}

	public EventItem getEventOnDate(Date selectedDate) {
		if(selectedDate==null || monthEventsList /*eventsUnFilteredList*/==null)
			return null;
/*		// to return list of events on this day
		ArrayList<EventItem> dayEvents = new ArrayList<>();*/

		//parse date of each event
		EventItem eventItem=null;

		for(EventItem iterator:monthEventsList /*eventsUnFilteredList*/){
			if(iterator==null)
				continue;
			Date eventDate = null;
			try
			{
				eventDate = MainActivity.sdf_Date.parse(iterator.EventDate);
				/*selectedDate = MainActivity.sdf_Date.parse(selectedDate.toString());*/
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			// get event
			if(Utilities.isSameDay(eventDate, selectedDate)){
//				dayEvents.add(iterator); // to return list of events on this day
				return iterator; // to return first matching event on this day
			}
		}
		return eventItem;
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

	public void setMonthEventsList(ArrayList<EventItem> eventsList) {
		this.monthEventsList=eventsList;
	}


	public ArrayList<EventItem> getMonthEventsList() {
		return monthEventsList;
	}
}
