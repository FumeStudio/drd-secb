package com.secb.android.model;

import java.io.Serializable;
import java.util.List;

public class LocationItem extends Paging
{
    public String LoccationItemRoomType;
    public int LoccationItemRoomCapacity;
    public double LoccationItemRoomSpace;
    public int LoccationItemNumberOrRooms;


	public String ID;
	public String SiteName;
	public String SiteImage;
	public String SiteLocation;
	public String SiteCapacity;
	public String SiteArea;
	public String SitePhone;
	public String SiteEmail;
	public String SiteCity;
	public String SiteType;
	public String SiteStreet;
	public String SiteAddressDescription;
	public String SiteDescription;
	public String SiteFeaturesAndResources;
	public String SiteDistrict;
	public String WebSite;
	public List<RoomItem> LocationRooms;

}
