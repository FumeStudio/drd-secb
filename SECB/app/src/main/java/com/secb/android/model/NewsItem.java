package com.secb.android.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class NewsItem implements Serializable
{
    public Bitmap newsItemImage;
    public String newsItemTitle;
    public String newsItemDescription;
    public String newsItemDate;
}
