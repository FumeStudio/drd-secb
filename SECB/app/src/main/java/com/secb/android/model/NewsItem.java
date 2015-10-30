package com.secb.android.model;

import net.comptoirs.android.common.helper.Utilities;

import java.io.Serializable;

public class NewsItem implements Serializable
{
    static final long serialVersionUID =-5921136756892110095L;

    public String Title;
	public String CreationDate;
    public String ID;
    public String ImageUrl;
    public String NewsCategory;
    public String NewsBrief;
    public String NewsBody;

    public String getImageUrl() {
        return !Utilities.isNullString(ImageUrl) ? ImageUrl.replaceAll("(?<!(http:|https:))//", "/") : ImageUrl;
    }
}
