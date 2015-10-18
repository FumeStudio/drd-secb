package net.comptoirs.android.common.helper;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

public class MapsHelper

{
    private static final double EARTH_RADIUS = 6378100.0;

    public static Polygon addRectangle(GoogleMap map,LatLng p1,LatLng p2,int fillColor,int strokeColor)
    {
        double maxLat,minLat,maxLng,minLng;
        if(p1.latitude > p2.latitude)
        {
            maxLat = p1.latitude;
            minLat = p2.latitude;
        }
        else{
            maxLat = p2.latitude;
            minLat = p1.latitude;
        }

        if(p1.longitude > p2.longitude)
        {
            maxLng = p1.longitude;
            minLng = p2.longitude;
        }
        else{
            maxLng = p2.longitude;
            minLng = p1.longitude;
        }

        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(minLat, minLng),
                        new LatLng(minLat, maxLng),
                        new LatLng(maxLat, maxLng),
                        new LatLng(maxLat, minLng))
                .fillColor(fillColor)
                .strokeColor(strokeColor);

        return map.addPolygon(rectOptions);
    }

    public static boolean isPointInBounds(LatLng latLng, LatLngBounds bounds) {
        double maxLat,minLat,maxLng,minLng;
        LatLng p1 = bounds.northeast;
        LatLng p2 = bounds.southwest;
        if(p1.latitude > p2.latitude)
        {
            maxLat = p1.latitude;
            minLat = p2.latitude;
        }
        else{
            maxLat = p2.latitude;
            minLat = p1.latitude;
        }

        if(p1.longitude > p2.longitude)
        {
            maxLng = p1.longitude;
            minLng = p2.longitude;
        }
        else{
            maxLng = p2.longitude;
            minLng = p1.longitude;
        }
        return (latLng.latitude > minLat && latLng.latitude < maxLat && latLng.longitude > minLng && latLng.longitude < maxLng);
    }

    public static boolean isPointInRectangle(LatLng tap, List<LatLng> vertices) {
        double maxLat,minLat,maxLng,minLng;
        LatLng p1 = vertices.get(0);
        LatLng p2 = vertices.get(2);
        if(p1.latitude > p2.latitude)
        {
            maxLat = p1.latitude;
            minLat = p2.latitude;
        }
        else{
            maxLat = p2.latitude;
            minLat = p1.latitude;
        }

        if(p1.longitude > p2.longitude)
        {
            maxLng = p1.longitude;
            minLng = p2.longitude;
        }
        else{
            maxLng = p2.longitude;
            minLng = p1.longitude;
        }
        return (tap.latitude > minLat && tap.latitude < maxLat && tap.longitude > minLng && tap.longitude < maxLng);
    }

    private static final double ASSUMED_INIT_LATLNG_DIFF = 1.0;
    private static final float ACCURACY = 0.01f;
    public static LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
        latDistanceInMeters /= 2;
        lngDistanceInMeters /= 2;
        LatLngBounds.Builder builder = LatLngBounds.builder();
        float[] distance = new float[1];
        {
            boolean foundMax = false;
            double foundMinLngDiff = 0;
            double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude, center.longitude + assumedLngDiff, distance);
                float distanceDiff = distance[0] - lngDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLngDiff = assumedLngDiff;
                        assumedLngDiff *= 2;
                    } else {
                        double tmp = assumedLngDiff;
                        assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
                        foundMinLngDiff = tmp;
                    }
                } else {
                    assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - lngDistanceInMeters) > lngDistanceInMeters * ACCURACY);
            LatLng east = new LatLng(center.latitude, center.longitude + assumedLngDiff);
            builder.include(east);
            LatLng west = new LatLng(center.latitude, center.longitude - assumedLngDiff);
            builder.include(west);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude + assumedLatDiffNorth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffNorth;
                        assumedLatDiffNorth *= 2;
                    } else {
                        double tmp = assumedLatDiffNorth;
                        assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng north = new LatLng(center.latitude + assumedLatDiffNorth, center.longitude);
            builder.include(north);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude - assumedLatDiffSouth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffSouth;
                        assumedLatDiffSouth *= 2;
                    } else {
                        double tmp = assumedLatDiffSouth;
                        assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng south = new LatLng(center.latitude - assumedLatDiffSouth, center.longitude);
            builder.include(south);
        }
        return builder.build();
    }

    public static Marker addMarker(GoogleMap map,LatLng latLng,int resIcon)
    {
        MarkerOptions marker = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(resIcon));
        if(latLng == null || resIcon < 1) return null;
        return map.addMarker(marker);
    }


    static Float anchor_x=0f,anchor_y=0f;
    public static Marker addMarker(GoogleMap map,LatLng latLng,Bitmap bitmap)
    {
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
//        markerOptions.position(latLng);
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//        markerOptions.anchor(10,10);





        MarkerOptions marker = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(anchor_x,anchor_y);
//        anchor_x+=0.3f;
//        anchor_y+=0.3f;
        if(latLng == null || bitmap == null)
            return null;

        Location markerLocation ;

        return map.addMarker(marker);
//        return map.addMarker(markerOptions);
    }

    public static void setMapCenter(boolean isAnimate, final GoogleMap  map, LatLng latLng) {

        int zoom = (int) map.getCameraPosition().zoom;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, zoom);

        if(isAnimate)
        {
//            map.animateCamera(cu, 2000, null);


            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), 200, null);
            // keeping numbers small you get a nice scrolling effect
//            map.animateCamera(CameraUpdateFactory.scrollBy(250 - (float) Math.random() * 500 - 250, 250 - (float) Math.random() * 500), 2000, null);

        }
        else
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    public static void zoomToAllMarkers(List<LatLng>markersList,final GoogleMap  map, int padding,int zoom)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng:markersList)
        {
            builder.include(latLng);
        }

        LatLngBounds bounds = builder.build();
        CameraUpdate camUpdate=CameraUpdateFactory.newLatLngBounds(bounds, padding);
//        CameraUpdate camUpdate2 = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), zoom);
        try
        {
            map.animateCamera(camUpdate);
        } catch (Exception e) {
            // Map size can't be 0
            e.printStackTrace();
        }
    }
}
