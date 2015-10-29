package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.model.OrganizerItem;

import java.util.ArrayList;
import java.util.List;

public class EGuideOrganizersManager {
    private static EGuideOrganizersManager instance;
	private List<OrganizerItem> organizersUnFilteredList;

	private OrganizerItem organizerDetails;

	private EGuideOrganizersManager() {
    }
    public static EGuideOrganizersManager getInstance() {
        if (instance == null) {
            instance = new EGuideOrganizersManager();
        }
        return instance;
    }

/** Cities*/
	//go to EventManager

/** List*/
	public List<OrganizerItem> getOrganizersUnFilteredList(Context context) {
		organizersUnFilteredList =CachingManager.getInstance().loadOrganizersList(context);
		return organizersUnFilteredList;
	}

	public void setOrganizersUnFilteredList(List<OrganizerItem> organizerItems, Context context) {
		this.organizersUnFilteredList = organizerItems;
		CachingManager.getInstance().saveOrganizersList((ArrayList<OrganizerItem>) this.organizersUnFilteredList, context);
	}




//currently not used because there is no network operation to get item details
//and the item is sent in a bundle from EventList fragment to EventDetails Fragment
/** Details*/
	public OrganizerItem getOrganizerDetails(String OrganizerEmail,Context context) {
		organizerDetails = CachingManager.getInstance().loadOrganizerDetails(OrganizerEmail, context);
		return organizerDetails;
	}

	public void setOrganizerDetails(OrganizerItem item , Context context) {
		this.organizerDetails = item;
		CachingManager.getInstance().saveOrganizerDetails(this.organizerDetails, context);

	}

}
