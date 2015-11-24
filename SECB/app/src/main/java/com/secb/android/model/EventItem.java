package com.secb.android.model;

import android.graphics.Color;

import com.p_v.flexiblecalendar.entity.Event;

import net.comptoirs.android.common.helper.Utilities;

import java.io.Serializable;

public class EventItem extends Paging implements Event{
/*    public transient  Bitmap eventItemImage;*/

	static final long serialVersionUID =3246079031338613679L;

    public double eventItemLatitude ;
    public double eventItemLongitude ;

	public String ID;
	public String EventDate;
	public String EndDate;
	public String EventSiteCity;
	public String EventSiteName;
	public String SiteonMap;
	public String Description;
	public String EventCategory;
	public String EventSiteDescription;
	public String Title;
	public String EventColor;
	public String IsAllDayEvent;
	public String IsRecurrence;
	public String ImageUrl;

	@Override
	public int getColor()
	{
		int color=0;
		if(!Utilities.isNullString(EventColor)){
			try {
				if(!EventColor.startsWith("#"))
					EventColor="#"+EventColor;
				color=Color.parseColor(EventColor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return color;
	}



    /*private void writeObject(ObjectOutputStream oos) throws IOException{
        // This will serialize all fields that you did not mark with 'transient'
        // (Java's default behaviour)
        oos.defaultWriteObject();
        // Now, manually serialize all transient fields that you want to be serialized
        if(eventItemImage!=null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = eventItemImage.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if(success){
                oos.writeObject(byteStream.toByteArray());
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        // Now, all again, deserializing - in the SAME ORDER!
        // All non-transient fields
        ois.defaultReadObject();
        // All other fields that you serialized
        byte[] image = (byte[]) ois.readObject();
        if(image != null && image.length > 0){
            eventItemImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }*/
}
