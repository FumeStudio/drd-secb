package com.secb.android.view;

import android.content.Intent;
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
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.secb.android.R;
import com.secb.android.model.Consts;
import com.secb.android.view.components.HeaderLayout;
import com.secb.android.view.menu.MenuFragment;
import com.secb.android.view.menu.MenuItem;

import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.Locale;

public abstract class SECBBaseActivity extends FragmentActivity /*AppCompatActivity*/ implements View.OnClickListener {
    private static final String TAG = "SECBBaseActivity";

    ArrayList<FragmentBackObserver> backObservers = null;    // observers list to hold the back of inner fragments
    private boolean mAllowSideMenu = true;                                    // to allow sliding menu or not
    private int mActivityLayout;                                                        // inner layout to be inflatted

    MenuFragment fragmentLeftMenu;                                                    // menu fragment

    private DrawerLayout mDrawerLayout;                                            // menu holder
    private HeaderLayout headerLayoutHome;                                    // header layout

    public static SECBBaseActivity activity;
    public static MenuItem menuItemSelected = null;                    // Current menu item selected

    public int MENU_GRAVITY=Gravity.START;



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

    @Override
    protected void onStart() {
        super.onStart();
        if (Consts.IS_FLURRY_ENABLED) FlurryAgent.onStartSession(this, Consts.FLURRY_API_KEY);

//		if(mImageFetcher != null){
//			mImageFetcher.initDiskCache(Engine.DataFolder.IMAGE_FETCHER_HTTP_CACHE);
//		}
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // startIncomingView();
        Logger.instance().v("MainDialer", "onCreate", false);

        activity = SECBBaseActivity.this;

        if (mAllowSideMenu)
        {
            setContentView(R.layout.main);

            ViewGroup contentLayout = (ViewGroup) findViewById(R.id.simple_fragment);

            if (mActivityLayout != -1)
                LayoutInflater.from(this).inflate(mActivityLayout, contentLayout, true);

            fragmentLeftMenu = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentLeftMenu);

            // fragmentLeftMenu.setLeftMenuObserver(leftMenuObserver);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMainDialer);
            // set a custom shadow that overlays the main content when the drawer opens
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
                }

                @Override
                public void onDrawerClosed(View arg0) {
                }
            });

            headerLayoutHome = (HeaderLayout) findViewById(R.id.headerLayoutHome);
            headerLayoutHome.setMenuOnClickListener(this);
            handleButtonsEvents();

            backObservers = new ArrayList<FragmentBackObserver>();


            //if arabic open menu from right to left
            if(Utilities.getLanguage().startsWith("ar"))
                MENU_GRAVITY =Gravity.END;
            //else open from left to right
            else
                MENU_GRAVITY =Gravity.START;


        }
        else
        {
            setContentView(mActivityLayout);
        }

        doOnCreate(savedInstanceState);

//		if(mImageFetcher == null){
//			mImageFetcher = new ImageFetcher(this, Engine.DataFolder.IMAGE_FETCHER_HTTP_CACHE);
//			mImageFetcher.addImageCache(this.getSupportFragmentManager(),
//					Engine.getCacheParams());
//		}
    }

    private void handleButtonsEvents() {
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

    // Enable header 'back' button
    public void enableHeaderBackButton(View.OnClickListener backOnClickListener) {
        headerLayoutHome.enableBackButton(backOnClickListener);
    }

    // Disable header 'back' button
    public void disableHeaderBackButton() {
        headerLayoutHome.disableBackButton();
    }


    // Enable header 'menu' button
    public void enableHeaderMenuButton(View.OnClickListener menuOnClickListener) {
        headerLayoutHome.enableMenuButton(menuOnClickListener);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    // Disable header 'menu' button
    public void disableHeaderMenuButton() {
        headerLayoutHome.disableMenuButton();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

    public void finishFragmentOrActivity() {
        FragmentManager manager = getSupportFragmentManager();
        Logger.instance().v("finishFragmentOrActivity", manager.getBackStackEntryCount(), false);
        if (manager.getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else {
            moveTaskToBack(true);
        }
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
    public void addBackObserver(FragmentBackObserver fragmentBackObserver) {
        // remove the observer if it was already added here
        removeBackObserver(fragmentBackObserver);

        // add to observers List
        backObservers.add(fragmentBackObserver);
    }

    /*
     * Remove LocationUpdatedListener UI Observer from the list
     */
    public void removeBackObserver(FragmentBackObserver fragmentBackObserver) {
        backObservers.remove(fragmentBackObserver);
    }

    private void callBack() {
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
     * Handle open/close menu
     */
    public void handleMenuAppearance()
    {
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


}
