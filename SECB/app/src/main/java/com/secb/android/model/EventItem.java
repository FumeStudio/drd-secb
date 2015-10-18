package com.secb.android.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EventItem implements Serializable {
    public transient  Bitmap eventItemImage;
    public String eventItemTitle;
    public String eventItemDescription;
    public String eventItemTime;
    public String eventItemLocation;
    public String eventItemCategory;
    public String eventItemDuration;
    public String eventItemRepeating;
    public double eventItemLatitude ;
    public double eventItemLongitude ;


    private void writeObject(ObjectOutputStream oos) throws IOException{
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
    }
}
