package com.secb.android.view;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.secb.android.R;
import com.secb.android.controller.manager.CachingManager;
import com.secb.android.controller.manager.E_ServicesManager;
import com.secb.android.controller.manager.Engine;
import com.secb.android.controller.manager.UserManager;
import com.secb.android.model.Consts;
import com.secb.android.model.E_ServiceRequestItem;
import com.secb.android.model.E_ServiceStatisticsItem;
import com.secb.android.model.EventItem;
import com.secb.android.model.LocationItem;
import com.secb.android.model.NewsItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.model.ShareItemInterface;
import com.secb.android.view.components.HeaderLayout;
import com.secb.android.view.components.LayoutAnimator;
import com.secb.android.view.components.dialogs.DialogConfirmListener;
import com.secb.android.view.fragments.EventsListFragment;
import com.secb.android.view.fragments.LocationsDetailsFragment;
import com.secb.android.view.fragments.LocationsListFragment;
import com.secb.android.view.fragments.OrganizersDetailsFragment;
import com.secb.android.view.fragments.OrganizersListFragment;
import com.secb.android.view.fragments.SECBBaseFragment;
import com.secb.android.view.menu.MenuFragment;
import com.secb.android.view.menu.MenuItem;

import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.SharedPreferenceData;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.Locale;

public abstract class SECBBaseActivity extends FragmentActivity /*AppCompatActivity*/ implements View.OnClickListener {
    private static final String TAG = "SECBBaseActivity";

    SECBBaseFragment currentDisplayedFragment;
    ArrayList<FragmentBackObserver> backObservers = null;    // observers list to hold the back of inner fragments
    private boolean mAllowSideMenu = true;                                    // to allow sliding menu_layout or not
    private int mActivityLayout;                                                        // inner layout to be inflatted

    MenuFragment fragmentLeftMenu;                                                    // menu_layout fragment
    private boolean isMenuOpened;
    private DrawerLayout mDrawerLayout;                                            // menu_layout holder
    private HeaderLayout headerLayoutHome;                                    // header layout

    public static SECBBaseActivity activity;
    public static MenuItem menuItemSelected = null;                    // Current menu_layout item selected

    public int MENU_GRAVITY = Gravity.LEFT;

    float filterStartX = 0;
    float filterStartY = 0;
    public boolean isFilterLayoutOpened;
    //icon when clicked , display filter view
    ImageView imgv_filter;

    //filter of fragments
    LinearLayout filterLayout;

    //holder in base activity for holding fragment's filters
    LinearLayout filterLayoutHolder;

    //view of the filter to be loaded inside base activity holder
    private View filterLayoutView;

    //on click listener for the apply filters button
    View.OnClickListener applyFilterClickListener;
    //on click listener for the clear filters button
    View.OnClickListener clearFilterClickListener;
    private LayoutAnimator layoutAnimator;
    private boolean isVerticalAnimation;
    Bundle savedInstanceState;

    IntentFilter languageIntentFiler = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
	private View filter_holder_dark_layer;

//	private /*static*/ ImageFetcher mImageFetcher;

    // abstract method to be called in activities that extend this one instead of oncreate().


    protected abstract void doOnCreate(Bundle arg0);

    /*
     * Constructor
     */
    public SECBBaseActivity(int activityLayout, boolean allowMenu) {
        this.mActivityLayout = activityLayout;
        this.mAllowSideMenu = allowMenu;
    }

    /*
     * *****************************************************************************
     * *********************** Activity LifeCycle **********************************
     * *****************************************************************************
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Engine.setApplicationLanguage(this, Engine.getAppConfiguration().getLanguage());
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //if tablet force screen to be landscape else portrait
	    if(Utilities.isTablet(this))
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    else
		    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // startIncomingView();
        Logger.instance().v("SecbBaseActivity", "onCreate", false);

        activity = SECBBaseActivity.this;

        if (mAllowSideMenu) {
            setContentView(R.layout.main);

            ViewGroup contentLayout = (ViewGroup) findViewById(R.id.simple_fragment);

	        imgv_filter = (ImageView) findViewById(R.id.imgv_filter);
            imgv_filter.setVisibility(View.GONE);
            imgv_filter.setOnClickListener(this);
            filterLayoutHolder = (LinearLayout) findViewById(R.id.filter_holder);
//            filterLayoutHolder.setVisibility(View.VISIBLE);
            /*layoutAnimator = new LayoutAnimator(filterLayoutHolder,isVerticalAnimation);*/
//            layoutAnimator.hideFirst();

            if (mActivityLayout != -1)
                LayoutInflater.from(this).inflate(mActivityLayout, contentLayout, true);

            fragmentLeftMenu = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentLeftMenu);

            // fragmentLeftMenu.setLeftMenuObserver(leftMenuObserver);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMainDialer);
            // set a custom shadow_h that overlays the main content when the drawer opens
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            mDrawerLayout.setDrawerListener(new DrawerListener() {
                @Override
                public void onDrawerStateChanged(int arg0) {
                }

                @Override
                public void onDrawerSlide(View arg0, float offset) {
                }

                @Override
                public void onDrawerOpened(View arg0) {
                    isMenuOpened = true;
                }

                @Override
                public void onDrawerClosed(View arg0) {
                    isMenuOpened = false;
                }
            });

            headerLayoutHome = (HeaderLayout) findViewById(R.id.headerLayoutHome);


            headerLayoutHome.setMenuOnClickListener(this);
            handleButtonsEvents();
            setHeaderTitleFont(UiEngine.Fonts.HVAR);
            backObservers = new ArrayList<FragmentBackObserver>();


            //if arabic open menu_layout from right to left
            if (Engine.getAppConfiguration().getLanguage().startsWith("ar"))
                MENU_GRAVITY = Gravity.RIGHT;
                //else open from left to right
            else
                MENU_GRAVITY = Gravity.LEFT;


        } else {
            setContentView(mActivityLayout);
        }

        doOnCreate(savedInstanceState);

//		if(mImageFetcher == null){
//			mImageFetcher = new ImageFetcher(this, Engine.DataFolder.IMAGE_FETCHER_HTTP_CACHE);
//			mImageFetcher.addImageCache(this.getSupportFragmentManager(),
//					Engine.getCacheParams());
//		}
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Consts.IS_FLURRY_ENABLED) FlurryAgent.onStartSession(this, Consts.FLURRY_API_KEY);

//		if(mImageFetcher != null){
//			mImageFetcher.initDiskCache(Engine.DataFolder.IMAGE_FETCHER_HTTP_CACHE);
//		}
    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerLanguageBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unRegisterLanguageBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Consts.IS_FLURRY_ENABLED) FlurryAgent.onEndSession(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    private void handleButtonsEvents() {
    }

    /*
     * *****************************************************************************
     * *********************** Filter Layout Part **********************************
     * *****************************************************************************
     */
/*filter button*/
    public void showFilterButton(boolean isVisible)
    {
	    if(Utilities.isTablet(this))
	    {
		    enableHeaderFilterButton(this);
		 /*   if(isVisible)
			    enableHeaderFilterButton(this);
		    else
			    disableHeaderFilterButton();*/
	    }
        else
	    {
		    imgv_filter.setVisibility(isVisible ? View.VISIBLE : View.GONE);
	    }
    }

    public void setApplyFilterClickListener(View.OnClickListener listener) {
        applyFilterClickListener = (listener);
    }
    public void setClearFilterClickListener(View.OnClickListener listener) {
        clearFilterClickListener = (listener);
    }

    public void setFilterLayout(LinearLayout filterLayout, boolean isVerticalAnimation) {
        filterStartX = headerLayoutHome.getX();
        filterStartY = headerLayoutHome.getMeasuredHeight();
        this.filterLayout = filterLayout;
        this.isVerticalAnimation = isVerticalAnimation;
        layoutAnimator = new LayoutAnimator(filterLayoutHolder, isVerticalAnimation, filterStartX, filterStartY);

    }

    public void setFilterLayoutView(View filterLayoutView) {
        this.filterLayoutView = filterLayoutView;
	    filter_holder_dark_layer = filterLayoutView.findViewById(R.id.layout_dark_layer);
	    layoutAnimator.setDarkLayer(filter_holder_dark_layer);
    }

    public void prepareFilerLayout() {
        if (filterLayoutHolder != null && filterLayoutView != null) {
            filterLayoutHolder.removeAllViews();
            filterLayoutHolder.addView(filterLayoutView);
	        //apply filter
            if (filterLayoutView.findViewById(R.id.btn_applyFilter) != null && applyFilterClickListener != null) {
                filterLayoutView.findViewById(R.id.btn_applyFilter).setOnClickListener(applyFilterClickListener);
            }
            //clear filter
            if (filterLayoutView.findViewById(R.id.btn_clearFilter) != null && clearFilterClickListener != null) {
                filterLayoutView.findViewById(R.id.btn_clearFilter).setOnClickListener(clearFilterClickListener );
            }
            if (filterLayoutView.findViewById(R.id.layout_dark_layer) != null) {
                filterLayoutView.findViewById(R.id.layout_dark_layer).setOnClickListener(this);
            }
            showFilterLayout();
        }
    }

    public void hideFilterLayout() {
        if (this.filterLayoutHolder != null && layoutAnimator != null && isFilterLayoutOpened)
        {
//            this.filterLayoutHolder.setVisibility(View.GONE);
            isFilterLayoutOpened = false;
            layoutAnimator.hidePreviewPanel();
        }
    }

    public void showFilterLayout() {
        if (this.filterLayoutHolder != null && layoutAnimator != null) {
//            this.filterLayoutHolder.setVisibility(View.VISIBLE);
            isFilterLayoutOpened = true;
            layoutAnimator.showPreviewPanel();
        }
    }


    /*
     * *****************************************************************************
     * *********************** Header Layout Part **********************************
     * *****************************************************************************
     */
    // Set header 'title'
    public void setHeaderTitleText(String title) {
        headerLayoutHome.setTitleText(title);
    }

    // apply font to  header 'title'
    public void setHeaderTitleFont(Typeface font) {
        headerLayoutHome.applyFontToTitleText(font);
    }

    // Enable header 'back' button
    public void enableHeaderBackButton(View.OnClickListener backOnClickListener) {
        headerLayoutHome.enableBackButton(backOnClickListener);
    }

    // Disable header 'back' button
    public void disableHeaderBackButton() {
        headerLayoutHome.disableBackButton();
    }



	// Enable header 'filter' button
	public void enableHeaderFilterButton(View.OnClickListener onClickListener) {
		headerLayoutHome.enableFilterButton(onClickListener);
	}

	// Disable header 'filter' button
	public void disableHeaderFilterButton() {
		headerLayoutHome.disableFilterButton();
	}




	// Enable header 'menu_layout' button
    public void enableHeaderMenuButton() {
        headerLayoutHome.enableMenuButton(this);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    // Disable header 'menu_layout' button
    public void disableHeaderMenuButton() {
        headerLayoutHome.disableMenuButton();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    // Enable header 'share' button
    public void enableHeaderShareButton(ShareItemInterface shareItemInterface)
    {
	    if(!Utilities.isTablet(this))
	    {
		    headerLayoutHome.enableShareButton(shareItemInterface);
	    }
	    else
	    {
		    //enable share icon from news details
	    }

    }

    // Disable header 'share' button
    public void disableHeaderShareButton() {
        headerLayoutHome.disableShareButton();
    }


    // hide header layout
    public void hideHeader() {
        findViewById(R.id.headerLayoutHome).setVisibility(View.GONE);
    }

    // hide header layout
    public void showHeader(boolean enableMenu) {
        if (headerLayoutHome != null) {
            headerLayoutHome.setVisibility(View.VISIBLE);
            if (!enableMenu) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                disableHeaderMenuButton();
            }
        }

    }

    // set header bg color
    public void setHeaderBGColor(int color) {
        headerLayoutHome.setHeaderBGColor(color);
    }

    // set header default bg color
    public void setHeaderDefaultBGColor() {
        headerLayoutHome.setHeaderDefaultBGColor();
    }

    ////////////////// Header Layout Part //////////////////////////////

    /*
     * On 'Menu Item' pressed
     */
    public void onMenuItemPressed(int id) {
        boolean isCloseMenu = true;
        switch (id) {
            default:
                isCloseMenu = false;
                break;
        }

        if (isCloseMenu) closeMenuPanel();
    }

    /*
     * Add new fragment and empty the stack of fragments
     */
    public void addFragmentAndEmptyStack(Fragment fragment, boolean isAnimated) {
        FragmentManager manager = getSupportFragmentManager();
        Logger.instance().v("addFragmentAndEmptyStack", "Count " + manager.getBackStackEntryCount(), false);
        if (manager.getBackStackEntryCount() > 0)
            for (int i = 0; i < manager.getBackStackEntryCount(); i++)
                getSupportFragmentManager().popBackStack();

        addFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, isAnimated);
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, int transition) {
        addFragment(fragment, addToBackStack, transition, true);
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, int transition, boolean isAnimated) {

        Log.v("addFragment", addToBackStack + " " + fragment);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (addToBackStack && isAnimated)
            ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out);
        ft.replace(R.id.simple_fragment, fragment);
        if (addToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    public void launchNewFragment(Fragment fragment, boolean isAnimated) {
        // Fragment anotherFragment = Fragment.instantiate(this, ExampleFragment.class.getName());
        addFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, isAnimated);
    }


    //always add fragment to back stack with a tag
    public void addFragment(Fragment fragment, String Tag, int transition, boolean isAnimated) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isAnimated)
            ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out);
        ft.replace(R.id.simple_fragment, fragment);
        ft.addToBackStack(Tag);
        ft.commit();
    }


    public void finishFragmentOrActivity() {
        hideFilterLayout();
        FragmentManager manager = getSupportFragmentManager();
        Logger.instance().v("finishFragmentOrActivity", manager.getBackStackEntryCount(), false);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();

        else {
            moveTaskToBack(true);
        }
    }

	public void finishFragmentOrActivity(String name) {
		finishFragmentOrActivity(name, false);
    }

	public void finishFragmentOrActivity(String name,boolean isBackToHome){
		if( isFilterLayoutOpened){
			hideFilterLayout();
			return;
		}
		FragmentManager manager = getSupportFragmentManager();
		Logger.instance().v("finishFragmentOrActivity", manager.getBackStackEntryCount(), false);
		if (getSupportFragmentManager().getBackStackEntryCount() > 0)
		{
			getSupportFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//	        remove till the home fragment
			if(isBackToHome)
			{
				while (getSupportFragmentManager().getBackStackEntryCount() != 0) {
					getSupportFragmentManager().popBackStackImmediate();
				}
			}
		} else {
			moveTaskToBack(true);
		}
	}

    public SECBBaseFragment getCurrentDisplayedFragment() {
        currentDisplayedFragment = (SECBBaseFragment) getSupportFragmentManager().findFragmentById(R.id.simple_fragment);
        return currentDisplayedFragment;
    }

    /*
     * pop to root of fragments list
     */
    public void popToRoot() {
        runOnUiThread(new Runnable() {
            public void run() {
                int length = getSupportFragmentManager().getBackStackEntryCount() - 1;
                Logger.instance().v("Poptoroot - length", length, false);
                if (length > 0) {
                    for (int i = 0; i < length; i++)
                        getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

	/*
     * *****************************************************************************
	 * *********************** Back observers list Part ****************************
	 * *****************************************************************************
	 */

    /*
     * Add LocationUpdatedListener UI Observer to List
     */
    public synchronized void addBackObserver(FragmentBackObserver fragmentBackObserver) {
        // remove the observer if it was already added here
        removeBackObserver(fragmentBackObserver);

        // add to observers List
        backObservers.add(fragmentBackObserver);
    }

    /*
     * Remove LocationUpdatedListener UI Observer from the list
     */
    public synchronized void removeBackObserver(FragmentBackObserver fragmentBackObserver) {
        backObservers.remove(fragmentBackObserver);
    }

    private synchronized void callBack() {
        if (backObservers != null)
            for (FragmentBackObserver backObserver : backObservers)
                backObserver.onBack();
    }

    // //////////////// Back observers list Part //////////////////////////////

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.instance().v("ONKEYDOWN", (keyCode == KeyEvent.KEYCODE_BACK), false);
        if (keyCode == KeyEvent.KEYCODE_BACK && onBackButtonPressed()) {
            return true;
            // getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onBackButtonPressed() {
        Log.v("FragmentStackAct", "Back " + getSupportFragmentManager().getBackStackEntryCount() + "  ");
        if (isMenuOpened) {
            closeMenuPanel();
            return true;
        }
        if (isFilterLayoutOpened) {
            hideFilterLayout();
            return true;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // getSupportFragmentManager().popBackStack();
            callBack();
            return true;
        } else {
            // if (HomeTabsActivity.tabsActivity != null)
            // {
            // HomeTabsActivity.tabsActivity.moveTaskToBack(true);
            // HomeTabsActivity.tabsActivity.finish();
            // }
            // finish();
            // moveTaskToBack(true);
        }
        return false;
    }


    /*
     * Handle open/close menu_layout
     */
    public void handleMenuAppearance() {
        if (mDrawerLayout.isDrawerOpen(MENU_GRAVITY))
            mDrawerLayout.closeDrawer(MENU_GRAVITY);
        else mDrawerLayout.openDrawer(MENU_GRAVITY);
    }

    /*
     * Open Menu Panel
     */
    public void openMenuPanel() {
        mDrawerLayout.openDrawer(MENU_GRAVITY/*GravityCompat.END*/);
    }

    /*
     * Open Menu Panel
     */
    public void closeMenuPanel() {
        mDrawerLayout.closeDrawer(MENU_GRAVITY/*GravityCompat.END*/);
    }

    public static void setMenuItemSelected(MenuItem menuItemSelected) {
        SECBBaseActivity.menuItemSelected = menuItemSelected;
    }

//	public ImageFetcher getImageFetcher(){
//		return mImageFetcher;
//	}

    public static int indexOfSearchQuery(String search, String displayName) {
        if (!TextUtils.isEmpty(search)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(search.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

    public void openUrl(String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(Intent.createChooser(intent, "Complete Action"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewMenuHeader: // Menu
                handleMenuAppearance();
                break;
            case R.id.layout_dark_layer:
                hideFilterLayout();
                break;
//            case R.id.imageViewShareHeader:
//                displayToast("share !");
//                break;
            case R.id.imageViewFilterHeader:
            case R.id.imgv_filter:
	            if(!isFilterLayoutOpened)
                    prepareFilerLayout();
                break;

            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void displayToast(String msg) {
        Toast.makeText(this, msg + "", Toast.LENGTH_SHORT).show();
    }


    public void showConfirmDialog(final SECBBaseActivity activity, String dialogMessage, final DialogConfirmListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onDilaogConfirmed();
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onDilaogCancelled();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void changeAppLanguage(boolean changeToEnglish) {
        net.comptoirs.android.common.helper.Logger.instance().v("LANGUAGE", ">changeAppLanguage< changeToEnglish? "+changeToEnglish);
        String languageToLoad = "en";
        if (!changeToEnglish)
            languageToLoad = "ar";

	    SharedPreferenceData sharedPreferenceData = new SharedPreferenceData(this);
	    sharedPreferenceData.save("language",languageToLoad);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Engine.switchAppLanguage(this, languageToLoad);
        myOnConfigurationChanged(config);

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
//        CTApplication.getApplication().onConfigurationChanged(config);


    }

    public void myOnConfigurationChanged(Configuration newConfig) {
        Logger.instance().v(TAG + " myOnConfigurationChanged - Language changed", newConfig.locale.getDisplayLanguage() + "");

        Intent i = new Intent(activity, SplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
    }

    public void logout() {
        //clear user from manager
        UserManager.getInstance().logout();
	    //clear caching folder from sd card
	    CachingManager.getInstance().clearCachingFolder(this);
	    try {
		    //clear cookies for webview
		    Utilities.clearCookies(this);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }

	    //go to login page
        Intent i = new Intent(activity, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);


    }


    public void registerLanguageBroadcastReceiver() {
        registerReceiver(languageChangedBroadcastReceiver, languageIntentFiler);
    }

    public void unRegisterLanguageBroadcastReceiver() {
        try {
            unregisterReceiver(languageChangedBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //if the device language is changed save the new language in preferences
    private final BroadcastReceiver languageChangedBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
            String language = "" + UiEngine.getCurrentDeviceLanguage(SECBBaseActivity.this);
            Logger.instance().v(TAG + " - Language changed", "Broadcast - onReceive - saveLanguage " + getResources().getConfiguration().locale.getLanguage());
//            saveLanguageSetting(language);
        }
    };

	//inflate a fragment inside a layout

	public void inflateFragmentInsideLayout(SECBBaseFragment fragment , int containerResId,boolean isAddtoBackStack){
		if(fragment!=null && containerResId!=-1){
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(containerResId, fragment);
			if (isAddtoBackStack)
				transaction.addToBackStack(fragment.getClass().getName());
			transaction.commit();
		}
	}

	public void openHomeFragment(boolean addToBackStack) {
		Intent i = new Intent(activity, MainActivity.class);
		activity.startActivity(i);
	}


	public void openNewsListFragment() {
		startActivity(new Intent(this, NewsActivity.class));
	}


	public void openNewDetailsFragment(NewsItem newsItem) {
//		NewsDetailsFragment newDetailsFragment = NewsDetailsFragment.newInstance(newsItem);
//		addFragment(newDetailsFragment, newDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		Intent intent = new Intent(this,NewsActivity.class);
		intent.putExtra("item",newsItem);
		startActivity(intent);
	}


	public void openEventListFragment() {
		EventsListFragment eventsListFragment = EventsListFragment.newInstance();
		addFragment(eventsListFragment, eventsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
	}
	//used only with calendar
	public void openEventListFragment(String startDate ,String endDate ) {
		EventsListFragment eventsListFragment = EventsListFragment.newInstance(startDate,endDate);
		addFragment(eventsListFragment, eventsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
	}


	public void openEventDetailsFragment(EventItem eventItem) {
//		EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(eventItem);
//		addFragment(eventDetailsFragment, eventDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		Intent intent = new Intent(this,EventsActivity.class);
		intent.putExtra("item",eventItem);
		startActivity(intent);

//	    openTestFragment(eventItem);
	}

	public void openEventsCalendarFragment() {
//		EventsCalendarFragment eventsCalendarFragment = EventsCalendarFragment.newInstance();
//		addFragment(eventsCalendarFragment, eventsCalendarFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		startActivity(new Intent(this, EventsActivity.class));
	}


	public void openEguideHomeFragment() {
//		EguideHomeFragment eguideHomeFragment = EguideHomeFragment.newInstance();
//		addFragment(eguideHomeFragment, eguideHomeFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		startActivity(new Intent(this, EguideActivity.class));
	}

	public void openEguideLocationFragment() {
		LocationsListFragment locationsListFragment = LocationsListFragment.newInstance();
		addFragment(locationsListFragment, locationsListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
	}

	public void openEguideOrganizersFragment() {
		OrganizersListFragment organizersListFragment = OrganizersListFragment.newInstance();
		addFragment(organizersListFragment, organizersListFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

	}

	public void openOrganizerDetailsFragment(OrganizerItem organizerItem) {
		OrganizersDetailsFragment organizersDetailsFragment = OrganizersDetailsFragment.newInstance(organizerItem);
		addFragment(organizersDetailsFragment, organizersDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
	}

	public void openLocationDetailsFragment(LocationItem locationItem) {
		LocationsDetailsFragment locationsDetailsFragment = LocationsDetailsFragment.newInstance(locationItem);
		addFragment(locationsDetailsFragment, locationsDetailsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
	}


	public void openGalleryFragment(int galleryType)
	{
//		GalleryFragment galleryFragment = GalleryFragment.newInstance(galleryType, galleryId);
//		addFragment(galleryFragment, galleryFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		Intent intent = new Intent(this,GalleryActivity.class);
		intent.putExtra("galleryType",galleryType);
		startActivity(intent);
	}

	public void openAlbumFragment(int galleryType, String folderPath, String albumId) {

//		AlbumFragment albumFragment = AlbumFragment.newInstance(galleryType, folderPath, albumId);
//		addFragment(albumFragment, albumFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);


		Intent intent = new Intent(this,GalleryActivity.class);
		intent.putExtra("galleryType",galleryType);
		intent.putExtra("folderPath",folderPath);
		intent.putExtra("albumId",albumId);
		startActivity(intent);
	}

	public void openContactUsFragment() {
//		ContactUsFragment contactUsFragment = ContactUsFragment.newInstance();
//		addFragment(contactUsFragment, contactUsFragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		startActivity(new Intent(this, ContactUsActivity.class));
	}

	public void openE_ServicesFragment() {
//		E_ServicesListFragment fragment = E_ServicesListFragment.newInstance();
//		addFragment(fragment, fragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		startActivity(new Intent (this,EservicesActivity.class));
	}

	public void openE_ServiceDetailsFragment(E_ServiceRequestItem e_serviceRequestItem) {
//		E_ServiceDetailsFragment fragment = E_ServiceDetailsFragment.newInstance(e_serviceRequestItem);
//		addFragment(fragment, fragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);
		Intent intent = new Intent(this,EservicesActivity.class);
		intent.putExtra("item",e_serviceRequestItem);
		startActivity(intent);
//		openTestFragment(null);
	}

	public void openAboutUsFragment() {
//		AboutUsFragment fragment = AboutUsFragment.newInstance();
//		addFragment(fragment, fragment.getClass().getName(), FragmentTransaction.TRANSIT_EXIT_MASK, true);

		startActivity(new Intent(this,AboutUsActivity.class));
	}


	public void openPlayerFragment(String videoUrl) {
//        VideoPlayerFragment videoPlayerFragment = VideoPlayerFragment.newInstance(videoUrl);
//        addFragment(videoPlayerFragment,videoPlayerFragment.getClass().getName() , FragmentTransaction.TRANSIT_EXIT_MASK, true);

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"), "video/*");
//        intent.setDataAndType(Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo"), "video/*");

		if (intent.resolveActivity(getPackageManager()) != null)
			startActivity(intent);
		else
			displayToast("can't play this video file ");
	}

	public void playYouTubeVideo(String videoUrlvideoUrl)
	{
		Intent intent = new Intent(this,YoutubePlayerActivity.class);
		intent.putExtra("youtubeVideoId", videoUrlvideoUrl);
		startActivity(intent);
	}
	public ArrayList<Integer> calculateGraphsValues(){
		ArrayList<Integer>valuesList = new ArrayList<>();
		//1-get Sum of All requests
		//2-get sum of closedRequests values
		//3-get % of closedRequests in All requests

		ArrayList<E_ServiceStatisticsItem> allRequsts = (ArrayList<E_ServiceStatisticsItem>) E_ServicesManager.getInstance().getEservicesStatisticsList(this);
		if(allRequsts==null || allRequsts.size()==0)
			return  null;

		//sum of all
		int sumOfAllRequests=0;
		//sum of Closed Requests
		int closedRequests=0;
		//sum of Inbox Requests
		int inboxRequests=0;
		//sum of InProgress Requests
		int progressRequests=0;

		for (E_ServiceStatisticsItem item: allRequsts)
		{
			int currentValue=0;
			try
			{
				currentValue=Integer.parseInt(item.Value);
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
				continue;
			}

			if(currentValue<=0)
				continue;

			sumOfAllRequests+=currentValue;

			if(item.Key.equalsIgnoreCase("ClosedRequests"))
				closedRequests=currentValue;
			else if (item.Key.equalsIgnoreCase("Inbox"))
				inboxRequests=currentValue;
			else if (item.Key.equalsIgnoreCase("InProgress"))
				progressRequests=currentValue;
		}
		valuesList.add((int)(100*((double)closedRequests/(double)sumOfAllRequests)));
		valuesList.add((int)(100*((double)inboxRequests/(double)sumOfAllRequests)));
		valuesList.add((int)(100*((double)progressRequests/(double)sumOfAllRequests)));
		return valuesList;
	}

    /*
     * hide keyboard
     */
    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
}
