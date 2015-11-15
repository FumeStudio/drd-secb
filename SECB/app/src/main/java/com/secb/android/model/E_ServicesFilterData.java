package com.secb.android.model;

import com.secb.android.controller.manager.UserManager;

public class E_ServicesFilterData
{
	public String UserName= UserManager.getInstance().getUser().userName;
	public String FromDate;
	public String ToDate;
	public String Status="All";
    public String RequestType="All";
	public String RequestNumber="All";

}
