package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.model.E_ServiceStatisticsItem;

import java.util.ArrayList;
import java.util.List;

public class E_ServicesManager
{
    private static E_ServicesManager instance;
	private List<E_ServiceRequestItem> e_serviceRequestsUnFilteredItems;
	private List<E_ServiceStatisticsItem> e_serviceStatisticsItems;

	private E_ServicesManager() {
    }

    public static E_ServicesManager getInstance() {
        if (instance == null) {
            instance = new E_ServicesManager();
        }
        return instance;
    }


/** RequestList*/
	public void setEservicesRequestsUnFilteredList(List<E_ServiceRequestItem> organizerItems, Context context) {
		this.e_serviceRequestsUnFilteredItems = organizerItems;
		CachingManager.getInstance().saveE_ServicesRequestsList((ArrayList<E_ServiceRequestItem>) this.e_serviceRequestsUnFilteredItems, context);
	}
	public List<E_ServiceRequestItem> getEservicesRequestsUnFilteredList(Context context) {
		e_serviceRequestsUnFilteredItems =CachingManager.getInstance().loadE_ServicesRequestsList(context);
		return e_serviceRequestsUnFilteredItems;
	}


/** StatisticsList*/
	public void setEservicesStatisticsList(List<E_ServiceStatisticsItem> e_serviceStatisticsItems, Context context) {
		this.e_serviceStatisticsItems = e_serviceStatisticsItems;
		CachingManager.getInstance().saveE_ServicesStatisticsList((ArrayList<E_ServiceStatisticsItem>) this.e_serviceStatisticsItems, context);
	}
	public List<E_ServiceStatisticsItem> getEservicesStatisticsList(Context context) {
		e_serviceStatisticsItems =CachingManager.getInstance().loadE_ServicesStatisticsList(context);
		return e_serviceStatisticsItems;
	}
}
