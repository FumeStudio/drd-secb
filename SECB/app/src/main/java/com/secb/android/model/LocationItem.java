package com.secb.android.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class LocationItem implements Serializable
{
    public transient Bitmap LoccationItemImage;

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
	public String SiteType;
	public String SiteStreet;
	public String SiteAddressDescription;
	public String SiteDescription;
	public String SiteFeaturesAndResources;
	public String SiteDistrict;


	/*private void writeObject(ObjectOutputStream oos) throws IOException {
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
    }*/
}
