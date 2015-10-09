package com.secb.android.model;

import java.util.ArrayList;

public class LocationsFilterData
{
    public String name;
    public String city;

    public ArrayList<Integer> types;

    public String totalCapacityFrom;
    public String totalCapacityTo;

    public static int TYPE_ALL=1;
    public static int TYPE_ECONOMIC=2;
    public static int TYPE_POLITICAL=3;
    public static int TYPE_PUBLIC=4;
}
