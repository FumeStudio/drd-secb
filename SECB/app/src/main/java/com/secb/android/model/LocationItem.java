package com.secb.android.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LocationItem implements Serializable
{
    public transient Bitmap LoccationItemImage;
    public String LoccationItemTitle;
    public String LoccationItemDescription;

    public String LoccationItemType;
    public int LoccationItemCapacity;
    public double LoccationItemSpace;
    public String LoccationItemRoomType;
    public int LoccationItemRoomCapacity;
    public double LoccationItemRoomSpace;
    public int LoccationItemNumberOrRooms;

    public String LoccationItemAddress;
    public String LoccationItemPhone;
    public String LoccationItemEmail;

    private void writeObject(ObjectOutputStream oos) throws IOException {
        // This will serialize all fields that you did not mark with 'transient'
        // (Java's default behaviour)
        oos.defaultWriteObject();
        // Now, manually serialize all transient fields that you want to be serialized
        if(LoccationItemImage!=null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = LoccationItemImage.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
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
            LoccationItemImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }
}
