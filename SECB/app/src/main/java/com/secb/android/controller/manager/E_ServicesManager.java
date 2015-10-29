package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.model.E_ServiceItem;

import java.util.ArrayList;
import java.util.List;

public class E_ServicesManager
{
    private static E_ServicesManager instance;
	private List<E_ServiceItem> e_serviceRequestsUnFilteredItems;

	private E_ServicesManager() {
    }

    public static E_ServicesManager getInstance() {
        if (instance == null) {
            instance = new E_ServicesManager();
        }
        return instance;
    }


/** List*/
	public List<E_ServiceItem> getEservicesRequestsUnFilteredList(Context context) {
		e_serviceRequestsUnFilteredItems =CachingManager.getInstance().loadE_ServicesRequestsList(context);
		return e_serviceRequestsUnFilteredItems;
	}

	public void setEservicesRequestsUnFilteredList(List<E_ServiceItem> organizerItems, Context context) {
		this.e_serviceRequestsUnFilteredItems = organizerItems;
		CachingManager.getInstance().saveE_ServicesRequestsList((ArrayList<E_ServiceItem>) this.e_serviceRequestsUnFilteredItems, context);
	}



}
