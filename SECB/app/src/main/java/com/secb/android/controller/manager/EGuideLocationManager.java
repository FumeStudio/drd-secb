package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.R;
import com.secb.android.model.EGuideLocationTypeItem;
import com.secb.android.model.LocationItem;

import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.List;

public class EGuideLocationManager {
    private static EGuideLocationManager instance;
	private List<LocationItem> locationUnFilteredList;
	//news Categories List
	List<EGuideLocationTypeItem> locationTypesList= null;
	private LocationItem locationDetails;

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
		EGuideLocationTypeItem item = new EGuideLocationTypeItem ();
		item.ID = "All";
		item.SiteTypeEnglish = Utilities.getStringFromLanguage(context,"en",R.string.news_filter_all_types);
		item.SiteTypeArabic = Utilities.getStringFromLanguage(context,"ar",R.string.news_filter_all_types);
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
		locationUnFilteredList = CachingManager.getInstance().loadLocationsList(context);
		return locationUnFilteredList;
	}

	public void setLocationsUnFilteredList(List<LocationItem> eventItems, Context context) {
		this.locationUnFilteredList = eventItems;
		CachingManager.getInstance().saveLocationsList((ArrayList<LocationItem>) this.locationUnFilteredList,context);
	}




//currently not used because there is no network operation to get event details
//and the event item is sent in a bundle from EventList fragment to EventDetails Fragment
/** Details*/
	public LocationItem getLocationDetails(String LocationId,Context context) {
		locationDetails = CachingManager.getInstance().loadLocationDetails(LocationId, context);
		return locationDetails;
	}

	public void setLocationDetails(LocationItem eventItem , Context context) {
		this.locationDetails = eventItem;
		CachingManager.getInstance().saveLocationDetails(this.locationDetails, context);

	}
}
