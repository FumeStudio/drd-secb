package net.comptoirs.android.common.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings.Secure;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.secb.android.R;

import net.comptoirs.android.common.view.CTApplication;
import net.comptoirs.android.common.view.ProgressDialogView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {
    private static Utilities _instance = null;

    ProgressDialogView progressDialog;
    SharedPreferenceData shared = null;

    private AlertDialog alertDialog;

    public static Utilities instance() {
        if (_instance == null) _instance = new Utilities();
        return _instance;
    }

    public static String errorToString(Throwable t) {
        StringBuffer errorMessage = new StringBuffer();

        if (errorMessage.length() == 0 && t.getCause() != null && t.getCause().getLocalizedMessage() != null)
            errorMessage.append(t.getCause().getLocalizedMessage());

        if (errorMessage.length() == 0 && t.getCause() != null && t.getCause().getMessage() != null)
            errorMessage.append(t.getCause().getMessage());

        if (errorMessage.length() == 0 && t.getLocalizedMessage() != null)
            errorMessage.append(t.getLocalizedMessage());

        if (errorMessage.length() == 0 && t.getMessage() != null)
            errorMessage.append(t.getMessage());

        if (errorMessage.length() == 0)
            errorMessage.append(t.getClass().getSimpleName());

        return errorMessage.toString();
    }

    public static int getScrollYOfRecycler(RecyclerView recyclerView) {
        View c = recyclerView.getChildAt(0);
        int scrolly = -c.getTop() + ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() * c.getHeight();
        return scrolly;
    }


    public static String md5Hash(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /*
     * Constructor
     */
    public Utilities() {
        shared = new SharedPreferenceData(CTApplication.getContext());
        enlargeHeapSize();
    }

    /*
     * Capitalize First letter
     */
    public static String getFirstLetterCapitalized(String word) {
        if (!isNullString(word))
            return word.substring(0, 1).toUpperCase(Locale.getDefault()) + word.substring(1);
        return word;
    }

    /*
     * Get Screen height
     */
    public static int getScreenHeight(Activity activity) {
        int measuredHeight = 0;
        Point size = new Point();
        WindowManager w = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }
        return measuredHeight;
    }

    /*
     * Get Screen Width
     */
    public static int getScreenWidth(Activity activity) {
        int measuredWidth = 0;
        Point size = new Point();
        WindowManager w = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
        }
        return measuredWidth;
    }

    /**
     * Check if the device CHDPI and has a "Navigation Bar"
     */
    public static boolean isDeviceXHDPIWithNavBar(Activity activity) {
        int screenHeight = getScreenHeight(activity);
        return (screenHeight > 1100 && screenHeight < 1280);
    }

    /*
     * Caught the Exceptions when appeared
     */
    public static void caughtCatchException(final Activity activity) {
        // UncaughtExceptionHandler handlerThread = new Thread.UncaughtExceptionHandler()
        // {
        // @Override
        // public void uncaughtException(Thread thread, final Throwable ex)
        // {
        // // Log.v("caughtCatchException()-new Thread", "Started");
        // new Thread(new Runnable()
        // {
        // public void run()
        // {
        // Log.v("caughtCatchException()", "Crached " + ex);
        // activity.startActivity(new Intent(activity, CrashReportActivity.class));
        // activity.finish();
        // android.os.Process.killProcess(android.os.Process.myPid());
        // // System.exit(10);
        // }
        // }).start();
        // }
        // };
        //
        // Thread.currentThread().setDefaultUncaughtExceptionHandler(handlerThread);
    }

	/*
     * get Address from geopoint
	 */
//	public static String getLocationFromPoint(GeoPoint geoPoint, Context context)
//	{
//		if (geoPoint != null)
//		{
//			final Geocoder geocoder = new Geocoder(context);
//			// zip = "90210";
//			try
//			{
//				List<Address> addresses = geocoder.getFromLocation((geoPoint.getLatitudeE6() / 1E6),
//				  (geoPoint.getLongitudeE6() / 1E6), 1);
//				if (addresses != null && !addresses.isEmpty())
//				{
//					Address address = addresses.get(0);
//					if (address != null)
//					{
//						String addressString = "";
//						// Use the address as needed
//						for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
//							addressString += address.getAddressLine(i) + " ";
//						Logger.instance().v("Geo Coding", "Success - " + addressString, false);
//						return addressString.trim();
//					}
//				}
//				else
//				{
//					Logger.instance().v("Geo Coding", "Unable to geocode zipcode", false);
//				}
//			}
//			// handle exception
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	/*
	 * get the Lat, and longitude from zip code
	 */
//	public static GeoPoint getGeoPointFromLocation(String addressLocation, Context context)
//	{
//		if (!Utilities.isNullString(addressLocation))
//		{
//			final Geocoder geocoder = new Geocoder(context);
//			// zip = "90210";
//			try
//			{
//				List<Address> addresses = geocoder.getFromLocationName(addressLocation, 1);
//				if (addresses != null && !addresses.isEmpty())
//				{
//					Address address = addresses.get(0);
//					// Use the address as needed
//					String message = String.format("Latitude: %f, Longitude: %f", address.getLatitude(), address.getLongitude());
//					Logger.instance().v("Geo Coding", "Success - " + message, false);
//					return Utilities.getGeoPoint(address.getLatitude(), address.getLongitude());
//				}
//				else
//				{
//					Logger.instance().v("Geo Coding", "Unable to geocode zipcode", false);
//				}
//			}
//			// handle exception
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	/*
	 * get GeoPoint from longitude and latitude
	 */
//	public static GeoPoint getGeoPoint(double latitude, double longitude)
//	{
//		return new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
//	}

    public static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        return language;
    }

    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    public static String getDecodedString(String text) {
        if (!isNullString(text)) {
            try {
                text = URLDecoder.decode(text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    public static void enlargeHeapSize() {
        // long oldHeapSize = VMRuntime.getRuntime().setMinimumHeapSize(64 * 1024 * 1024);
        // Logger.instance().v("oldHeapSize", oldHeapSize, false);
    }

    public static void printMemState() {
        ActivityManager activityManager = (ActivityManager) CTApplication.getContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        Logger.instance().v("Memory", " memoryInfo.availMem " + memoryInfo.availMem + "\n", false);
        Logger.instance().v("Memory", " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n", false);
        Logger.instance().v("Memory", " memoryInfo.threshold " + memoryInfo.threshold + "\n", false);
        int memoryClass = activityManager.getMemoryClass();

        // Get current size of heap in bytes
        long heapSize = Runtime.getRuntime().totalMemory();

        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
        // Any attempt will result in an OutOfMemoryException.
        long heapMaxSize = Runtime.getRuntime().maxMemory();

        // Get amount of free memory within the heap in bytes. This size will increase
        // after garbage collection and decrease as new objects are created.
        long heapFreeSize = Runtime.getRuntime().freeMemory();

        Logger.instance().v("Memory",
                "memoryClass:" + Integer.toString(memoryClass) + "        " + Runtime.getRuntime().maxMemory(), false);
        Logger.instance().v(
                "Memory",
                "Heap: heapSize= " + (heapSize / 1024 / 1024) + " MB        heapMaxSize= " + (heapMaxSize / 1024 / 1024)
                        + " MB   heapFreeSize= " + (heapFreeSize / 1024 / 1024) + " MB", false);

    }

    public static String getDeviceId() {
        String deviceId = "";

        try {
            deviceId = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
        } catch (Exception e) {
        }

        if (CTApplication.getContext() != null && isNullString(deviceId)) {
            TelephonyManager tel = (TelephonyManager) CTApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            Logger.instance().v("Device Id - Tel", tel.getDeviceId(), false);
            return tel.getDeviceId();
        }
        return deviceId;
    }

    /*
     * Add UI Service Observer to the list
     */
    public static void addUiObserver(Object uiObserver, ArrayList<Object> uiObserversList) {
        // remove the observer if it was already added here
        removeUiObserver(uiObserver, uiObserversList);

        // add to observers List
        uiObserversList.add(uiObserver);
    }

    /*
     * Remove UI Service Observer to the list
     */
    public static void removeUiObserver(Object uiObserver, ArrayList<?> uiObserversList) {
        // remove the observer if it was already added here
        try {
            ArrayList<Object> uiObservers = new ArrayList<Object>();
            uiObservers.addAll(uiObserversList);

            for (Object uiObserver_ : uiObservers)
                if (uiObserver_.getClass().equals(uiObserver.getClass())) {
                    Logger.instance().v("Removed", uiObserver.getClass(), false);
                    uiObserversList.remove(uiObserver);
                }
            uiObservers = null;
        } catch (Exception e) {
        }
    }

    public static String getHtmlFromUrl(String urlString) {
        URL url;
        String inputLine = "";
        try {
            url = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = "";
            while ((line = in.readLine()) != null)
                inputLine += line;
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputLine;
    }

    /*
     * Check is Agency in ContactList
     */
    public static boolean isContactAddedBefore(String agenceName) {
        // / number is the phone number
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]
                {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = CTApplication.getContext().getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();

        try {
            do {
                String name = people.getString(indexName);
                String number = people.getString(indexNumber);
                Logger.instance().v("Name", name + " " + agenceName, false);
                // Do work...
                if (name.compareTo(agenceName) == 0) return true;
            } while (people.moveToNext());
        } catch (Exception e) {
        }

        return false;
    }

    public static double getDouble(String str) {
        double value = 0.0;
        try {
            str = str.replaceAll(",", ".");
            value = Double.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static double getDouble(BigDecimal bigDecimal) {
        double value = 0.0;
        try {
            value = bigDecimal.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /*
     * Check if String is Null/empty or not
     */
    public static boolean isNullString(String str) {
        if (str != null && str.compareToIgnoreCase("null") != 0 && str.trim().length() > 0)
            return false;

        return true;
    }

    /*
     * Display toast msg
     */
    public static void showToastMsg(String msg, int duration) {
        Toast toastMsg = Toast.makeText(getContext(), msg, duration);
        toastMsg.setGravity(Gravity.CENTER, 0, 0);
        toastMsg.show();
    }

    public static void showToastMsg(Activity activity, final String msg, final int duration) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToastMsg(msg, duration);
            }
        });
    }

    public static void showErrorMsg(Activity activity, final String msg, final int duration) {
        showToastMsg(activity, msg, duration);
    }

    // ////////////////////
	/*
	 * ProgressDialog part
	 */

    public boolean isProgressDialogShowing() {
        if (progressDialog != null) return progressDialog.isShowing();
        return false;
    }

    public void showProgressDialog(final Activity activity, String msg) {
        showProgressDialog(activity, msg, null);
    }

    public void showProgressDialog(final Activity activity, String msg, OnCancelListener cancelListener) {
        dismissProgressDialog();

        progressDialog = new ProgressDialogView(activity);
        if (msg == null) msg = activity.getString(R.string.wait_moment);
        progressDialog.setOnCancelListener(cancelListener);
        progressDialog.showProgressDialog(msg);
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.hideDialog();
    }

    /*
     *errorDialog
     */
    public void showErrorDialog(final Activity activity, String title, String msg) {
        if (alertDialog != null)
            alertDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null)
            builder.setTitle(title);

        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }

    public void dismissAlertDialog() {
        if (alertDialog != null) alertDialog.dismiss();
    }

    public static void ellipsizeTextView(final TextView snippet, final int maxLines) {
        ViewTreeObserver vto = snippet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = snippet.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (snippet.getLineCount() > maxLines) {
                    int lineEndIndex = snippet.getLayout().getLineEnd(maxLines - 1);
                    String text = snippet.getText().subSequence(0, lineEndIndex - maxLines) + "...";
                    snippet.setText(text);
                }
            }
        });
    }

    public static Bitmap blurfast(Bitmap bitmap, int radius) {
        if (bitmap == null) return null;
        // Create a bitmap of the same size
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);
        // Create a canvas for new bitmap
        Canvas c = new Canvas(bmp);
        // Draw your old bitmap on it.
        c.drawBitmap(bitmap, 0, 0, new Paint());

        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pix = new int[w * h];
        bmp.getPixels(pix, 0, w, 0, 0, w, h);

        for (int r = radius; r >= 1; r /= 2) {
            for (int i = r; i < h - r; i++) {
                for (int j = r; j < w - r; j++) {
                    int tl = pix[(i - r) * w + j - r];
                    int tr = pix[(i - r) * w + j + r];
                    int tc = pix[(i - r) * w + j];
                    int bl = pix[(i + r) * w + j - r];
                    int br = pix[(i + r) * w + j + r];
                    int bc = pix[(i + r) * w + j];
                    int cl = pix[i * w + j - r];
                    int cr = pix[i * w + j + r];

                    pix[(i * w) + j] = 0xFF000000
                            | (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) + (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3)
                            & 0xFF
                            | (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00) + (br & 0xFF00) + (bc & 0xFF00)
                            + (cl & 0xFF00) + (cr & 0xFF00)) >> 3)
                            & 0xFF00
                            | (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) + (bl & 0xFF0000) + (br & 0xFF0000)
                            + (bc & 0xFF0000) + (cl & 0xFF0000) + (cr & 0xFF0000)) >> 3) & 0xFF0000;
                }
            }
        }
        bmp.setPixels(pix, 0, w, 0, 0, w, h);
        return bmp;
    }

    // Copy instream method
    public static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];

        int n = 0;

        while (-1 != (n = input.read(buffer))) {

            output.write(buffer, 0, n);
        }
    }
	/*
	 * Setters & Getters
	 */

    public SharedPreferenceData getShared() {
        return shared;
    }

    public void setShared(SharedPreferenceData shared) {
        this.shared = shared;
    }

    public static Context getContext() {
        return CTApplication.getContext();
    }

    /*
      * Get rounded bitmap
      */
    public static Bitmap getRoundedShape(Bitmap bmp, int radius) {
        if (bmp == null)
            return null;

        int width, height;
        if (bmp.getWidth() > bmp.getHeight()) {
            height = radius;
            width = radius * bmp.getWidth() / bmp.getHeight();
        } else {
            height = radius * bmp.getHeight() / bmp.getWidth();
            width = radius;
        }

        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, width, height, false);
        else
            sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(radius, radius, Config.ARGB_8888);
		
		/*Bitmap output =  bmp.isMutable()? bmp :Bitmap.createBitmap(radius, radius,
				Config.ARGB_8888) ;*/

        Canvas canvas = new Canvas(output);

        // final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        paint.setColor(Color.parseColor("#BAB399"));
        paint.setColor(Color.parseColor("#000000"));

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius / 2 + 0.7f,
                radius / 2 + 0.7f, radius / 2 + 0.1f, paint);

        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    /*
     * show & hide soft keyboard
     * */
    public void showSoftKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.requestFocus();
    }

    public void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Simple network connection check.
     *
     * @param context
     */
    public static boolean isConnected(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        }

        return true;
    }


    public static boolean isDateInPast(Date date) {
        if (date == null)
            return true;
        Date nowDate = Calendar.getInstance().getTime();
        return date.before(nowDate);
    }

    public static int getDaysBetweenDates(Date date1, Date date2) {
        //if date2 is more in the future than date1 then the result will be negative
        //if date1 is more in the future than date2 then the result will be positive.

        float days = ((float) (date2.getTime() - date1.getTime()) / (1000.0f * 60.0f * 60.0f * 24.0f));
        return (int) Math.ceil(days);
    }

    public static void sendSMS(Context context, String tel, String body) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", tel);
        if (!Utilities.isNullString(body)) smsIntent.putExtra("sms_body", body);
        smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(smsIntent, "Select SMS App"));
    }

    /*
     * Print the hash key
     */
    public static void printHashKey() {
        try {
            PackageInfo info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashkey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Logger.instance().v("KeyHash:", hashkey, false);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void restartActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }


    public static void openEmail(Context context, String emailAddress,
                                 String subject , String body)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",emailAddress, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        Intent chooser = Intent.createChooser(emailIntent, "");
        context.startActivity(chooser);
    }
    public static void openUrlInBrowser(Context context, String url) {
        if(!isNullString(url) && !url.toLowerCase().startsWith("http"))
            url = "http://"+url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        Intent chooser = Intent.createChooser(browserIntent, "");
        context.startActivity(chooser);
    }

    public static void callPhoneNumber(Context context, String phoneNumber) {

        Uri number = Uri.parse("tel:" + phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        // Create intent to show chooser
//        Intent chooser = Intent.createChooser(callIntent, "");
//        context.startActivity(chooser);
	    if(callIntent.resolveActivity(context.getPackageManager()) != null){
		    context.startActivity(callIntent);
	    }

    }

	public static LatLng getLatLngFromString(String latlngStr)
	{
		LatLng latLngObj=null ;
		if(!isNullString(latlngStr))
		{
			String[] latlngArr =  latlngStr.split(",");
			try
			{
				double latitude = Double.parseDouble(latlngArr[0]);
				double longitude = Double.parseDouble(latlngArr[1]);
				latLngObj = new LatLng(latitude,longitude);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return latLngObj;
	}
/*Compare 2 days without comparing their time*/
	public static  boolean isSameDay(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
		boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
		boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
		return (sameDay && sameMonth && sameYear);
	}

/* * email address validation */
	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		}
		else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public static boolean isGooglePlayServicesInstalled(Context context){
		PackageInfo pi =null;
		try
		{
			pi = context.getPackageManager().getPackageInfo("com.google.android.gms", 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi!=null;
	}


//	force to get the string from values-languageTag even if it's not the current language.
	public static String getStringFromLanguage(Context context, String languageTag, int stringResId)
	{
//        return context.getString(stringResId, languageTag);
		String value="";
		if(!isNullString(languageTag) && context!=null && stringResId>0 )
		{
			try {
				Resources standardResources = context.getResources();
				AssetManager assets = standardResources.getAssets();
				DisplayMetrics metrics = standardResources.getDisplayMetrics();
				Configuration config = new Configuration(standardResources.getConfiguration());
                Locale savedLocale = config.locale;
                config.locale = new Locale(languageTag);
				Resources mResources = new Resources(assets, metrics, config);
				value= mResources.getString(stringResId);

                // restore original locale
                config.locale = savedLocale;
                mResources.updateConfiguration(config, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}


	public static void clearCookies(Context context)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
			Logger.instance().v("ClearCookies", "Using ClearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
			CookieManager.getInstance().removeAllCookies(null);
			CookieManager.getInstance().flush();
		} else
		{
			Logger.instance().v("ClearCookies" , "Using ClearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
			CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
			cookieSyncMngr.startSync();
			CookieManager cookieManager=CookieManager.getInstance();
			cookieManager.removeAllCookie();
			cookieManager.removeSessionCookie();
			cookieSyncMngr.stopSync();
			cookieSyncMngr.sync();
		}
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
}
