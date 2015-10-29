package com.secb.android.model;

import java.io.Serializable;

public class OrganizerItem implements Serializable
{
//    public transient Bitmap OraganizerItemImage;
    public String OrganizerName;
    public String OrganizerImage;
    public String OrganizerCity;
    public String OrganizerLocation;
	public String OrganizerPhone;
	public String OrganizerEmail;
	public String OrganizerFAX;
    public String OrganizerWebAddress;
	public String OrganizerAddressDescription;
	public String OrganizerDescription;
	public String OrganizerDistrict;
	public String OrganizerStreet;

//    private void writeObject(ObjectOutputStream oos) throws IOException {
//        // This will serialize all fields that you did not mark with 'transient'
//        // (Java's default behaviour)
//        oos.defaultWriteObject();
//        // Now, manually serialize all transient fields that you want to be serialized
//        if(OraganizerItemImage!=null){
//            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//            boolean success = OraganizerItemImage.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
//            if(success){
//                oos.writeObject(byteStream.toByteArray());
//            }
//        }
//    }
//
//    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
//        // Now, all again, deserializing - in the SAME ORDER!
//        // All non-transient fields
//        ois.defaultReadObject();
//        // All other fields that you serialized
//        byte[] image = (byte[]) ois.readObject();
//        if(image != null && image.length > 0){
//            OraganizerItemImage = BitmapFactory.decodeByteArray(image, 0, image.length);
//        }
//    }
}
