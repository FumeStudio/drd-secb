package com.secb.android.controller.manager;

import android.content.Context;

import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.model.E_ServiceRequestTypeItem;
import com.secb.android.model.E_ServiceRequestWorkSpaceModeItem;
import com.secb.android.model.E_ServiceStatisticsItem;

import java.util.ArrayList;
import java.util.List;

public class E_ServicesManager
{
    private static E_ServicesManager instance;
	private List<E_ServiceRequestItem> e_serviceRequestsUnFilteredItems;
	private List<E_ServiceStatisticsItem> e_serviceStatisticsItems;
	private List<E_ServiceRequestTypeItem> e_serviceRequestsTypesItems;
	private List<E_ServiceRequestWorkSpaceModeItem> e_serviceRequestsWorkSpaceModesItems;

	private E_ServicesManager() {
    }

    public static E_ServicesManager getInstance() {
        if (instance == null) {
            instance = new E_ServicesManager();
        }
        return instance;
    }


/** RequestTypesList*/
	public void setEservicesRequestsTypesList(List<E_ServiceRequestTypeItem> items, Context context) {
		this.e_serviceRequestsTypesItems = items;
		if(e_serviceRequestsTypesItems!=null&&e_serviceRequestsTypesItems.size()>0&&e_serviceRequestsTypesItems.get(0)!=null)
			e_serviceRequestsTypesItems.get(0).isSelected=true;
		CachingManager.getInstance().saveE_ServicesRequestsTypesList((ArrayList<E_ServiceRequestTypeItem>) this.e_serviceRequestsTypesItems, context);
	}
	public List<E_ServiceRequestTypeItem> getEservicesRequestsTypesList(Context context) {
		e_serviceRequestsTypesItems =CachingManager.getInstance().loadE_ServicesRequestsTypesList(context);
		return e_serviceRequestsTypesItems;
	}

	public E_ServiceRequestTypeItem getSelectedRequestType() {
		if (e_serviceRequestsTypesItems != null && e_serviceRequestsTypesItems.size()>0){
			for (E_ServiceRequestTypeItem iterator:e_serviceRequestsTypesItems){
				if(iterator.isSelected){
					return iterator;
				}
			}
		}
		return null;
	}

/** RequestWorkSpaceModesList*/
	public void setEservicesRequestsWorkSpaceModesList(List<E_ServiceRequestWorkSpaceModeItem> items, Context context) {
		this.e_serviceRequestsWorkSpaceModesItems = items;
		if(e_serviceRequestsWorkSpaceModesItems!=null&&e_serviceRequestsWorkSpaceModesItems.size()>0&&e_serviceRequestsWorkSpaceModesItems.get(0)!=null)
			e_serviceRequestsWorkSpaceModesItems.get(0).isSelected=true;
		CachingManager.getInstance().saveE_ServicesRequestsWorkSpaceModesList((ArrayList<E_ServiceRequestWorkSpaceModeItem>) this.e_serviceRequestsWorkSpaceModesItems, context);
	}
	public List<E_ServiceRequestWorkSpaceModeItem> getEservicesRequestsWorkSpaceModesList(Context context) {
		e_serviceRequestsWorkSpaceModesItems =CachingManager.getInstance().loadE_ServicesRequestsWorkSpaceModesList(context);
		return e_serviceRequestsWorkSpaceModesItems;
	}

	public E_ServiceRequestWorkSpaceModeItem getSelectedRequestWorkSpaceModeType() {
		if (e_serviceRequestsWorkSpaceModesItems != null && e_serviceRequestsWorkSpaceModesItems.size()>0){
			for (E_ServiceRequestWorkSpaceModeItem iterator:e_serviceRequestsWorkSpaceModesItems){
				if(iterator.isSelected){
					return iterator;
				}
			}
		}
		return null;
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
