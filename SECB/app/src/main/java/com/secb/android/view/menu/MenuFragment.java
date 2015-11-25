package com.secb.android.view.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.model.GalleryItem;
import com.secb.android.view.AboutUsActivity;
import com.secb.android.view.EguideActivity;
import com.secb.android.view.EservicesActivity;
import com.secb.android.view.EventsActivity;
import com.secb.android.view.GalleryActivity;
import com.secb.android.view.MainActivity;
import com.secb.android.view.NewsActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.dialogs.DialogConfirmListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;
import com.secb.android.view.fragments.AboutUsFragment;
import com.secb.android.view.fragments.ContactUsFragment;
import com.secb.android.view.fragments.E_ServicesListFragment;
import com.secb.android.view.fragments.EguideHomeFragment;
import com.secb.android.view.fragments.EventsCalendarFragment;
import com.secb.android.view.fragments.HomeFragment;
import com.secb.android.view.fragments.NewsListFragment;
import com.secb.android.view.fragments.SECBBaseFragment;
import com.secb.android.view.menu.items_recycler.MenuItemObject;
import com.secb.android.view.menu.items_recycler.MenuItemRecyclerAdapter;

import java.util.ArrayList;

public class MenuFragment extends Fragment implements View.OnClickListener, RecyclerCustomClickListener ,DialogConfirmListener {
    View view;
    int []icons;

    String[] strings ;
    ArrayList<MenuItemObject> menuItemsList;

    RecyclerView menuItemRecyclerView;
    MenuItemRecyclerAdapter menuItemRecyclerAdapter;

/*    public int MENU_ITEM_HOME=0;
    public int MENU_ITEM_ESERVICES=1;
    public int MENU_ITEM_NEWS=2;
    public int MENU_ITEM_EGUIDE=3;
    public int MENU_ITEM_EVENTS=4;
    public int MENU_ITEM_IMAGES=5;
    public int MENU_ITEM_VIDEOS=6;
    public int MENU_ITEM_ABOUTUS=7;
    public int MENU_ITEM_CONTACTUS=8;
    public int MENU_ITEM_LAGUAGE=9;
    public int MENU_ITEM_LOGOUT=10;*/

    private int CONFIRM_LANGUAGE=1;
    private int CONFIRM_LOGOUT=2;
    private int confirmAbout;
    private SECBBaseFragment currentDisplayedFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
        {
            ViewParent oldParent = view.getParent();
            if (oldParent != container) {
                ((ViewGroup) oldParent).removeView(view);
            }
            return view;
        } else
        {
//            view = LayoutInflater.from(getActivity()).inflate(R.layout.menu_layout, container, false);
            view = inflater.inflate(R.layout.menu_layout, container, false);
            initializeMenu(view);
            handleButtonsEvents();
            return view;
        }
    }

    private void initializeMenu(View view) {
        strings =new String[] {getResources().getString(R.string.home),getResources().getString(R.string.eservices),
                getResources().getString(R.string.news),getResources().getString(R.string.eguide),
                getResources().getString(R.string.events),getResources().getString(R.string.images),
                getResources().getString(R.string.videos),getResources().getString(R.string.aboutus),
                getResources().getString(R.string.contactus),
                getResources().getString(R.string.language),
                getResources().getString(R.string.logout)
        };
        icons= new int[]{R.drawable.home_icon,R.drawable.eservices_icon,R.drawable.news_icon,
                R.drawable.eguide_icon,R.drawable.events_icon,R.drawable.images_icon,
                R.drawable.videos_icon,R.drawable.aboutus_icon,R.drawable.contactus_icon,
                R.drawable.lanuage_icon,R.drawable.logout_icon};

        menuItemRecyclerView = (RecyclerView)view.findViewById(R.id.menuItemRecyclerView);
        menuItemRecyclerAdapter = new MenuItemRecyclerAdapter(getActivity(), getMenuItems());
        /*adapter.setRecyclerClickListener(this);*/
        menuItemRecyclerView.setAdapter(menuItemRecyclerAdapter);
        menuItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        menuItemRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), menuItemRecyclerView, this));

    }

    private void handleButtonsEvents() {
        // TODO:: add clicklistener on the buttons
//		imageViewHome.setOnClickListener(this); 				// Home
    }


    @Override
    public void onClick(View v) {
        ((SECBBaseActivity) getActivity()).onMenuItemPressed(v.getId());
    }

    //make list of items used in the sliding menu_layout
    private ArrayList<MenuItemObject> getMenuItems()
    {
        if(menuItemsList!=null && menuItemsList.size()==icons.length)
            return menuItemsList;


        menuItemsList= new ArrayList<>();
        for (int i = 0 ; i < icons.length ; i++){
            menuItemsList.add(new MenuItemObject(icons[i],strings[i]));
        }
        return  menuItemsList;

    }

    @Override
    public void onItemClicked(View v, int position)
    {
        boolean isNavigationChanged = false;
        currentDisplayedFragment = ((SECBBaseActivity)getActivity()).getCurrentDisplayedFragment();
        switch (position)
        {
            case 0:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                if (!(getActivity() instanceof MainActivity)) // !(currentDisplayedFragment instanceof HomeFragment)
                {
                    ((SECBBaseActivity)getActivity()).openHomeFragment(true);
	                isNavigationChanged = true;
                }
                break;
            case 1:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                if(!(getActivity() instanceof EservicesActivity)) //  !(currentDisplayedFragment instanceof E_ServicesListFragment)
                {
                    ((SECBBaseActivity)getActivity()).openE_ServicesFragment();
	                isNavigationChanged = true;
                }
                break;
            case 2:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                if (!(getActivity() instanceof NewsActivity)) //  !(currentDisplayedFragment instanceof NewsListFragment)
                {
                    ((SECBBaseActivity)getActivity()).openNewsListFragment();
	                isNavigationChanged = true;
                }
                break;
            case 3:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                if (!(getActivity() instanceof EguideActivity)) //  !(currentDisplayedFragment instanceof EguideHomeFragment)
                {
                    ((SECBBaseActivity)getActivity()).openEguideHomeFragment();
	                isNavigationChanged = true;
                }
                break;
            case 4:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                if (!(getActivity() instanceof EventsActivity)) //  !(currentDisplayedFragment instanceof EventsCalendarFragment)
                {
                    ((SECBBaseActivity)getActivity()).openEventsCalendarFragment();
	                isNavigationChanged = true;
                }
                break;
            case 5:
                ((SECBBaseActivity) getActivity()).closeMenuPanel();
                if (!(getActivity() instanceof GalleryActivity)
                        || (getActivity() instanceof GalleryActivity) && ((GalleryActivity) getActivity()).galleryType != GalleryItem.GALLERY_TYPE_IMAGE_GALLERY) {
                    ((SECBBaseActivity) getActivity()).openGalleryFragment(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY);
                    isNavigationChanged = true;
                }
               /* //check if the displayed fragment is gallery or not
                boolean isGalleryFragment = (currentDisplayedFragment instanceof GalleryFragment);
                boolean isVideoGallery=false ;

                //in case of gallery check it's video gallery or not
                if(isGalleryFragment)
                    isVideoGallery=((GalleryFragment)currentDisplayedFragment).galleryType == GalleryItem.GALLERY_TYPE_VIDEO_GALLERY;

                //if the current is video gallery , then open image gallery
                if(! isGalleryFragment || isVideoGallery)
                {
                    ((SECBBaseActivity)getActivity()).openGalleryFragment(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY);
                }*/
                break;
            case 6:
                ((SECBBaseActivity) getActivity()).closeMenuPanel();
                if (!(getActivity() instanceof GalleryActivity)
                        || (getActivity() instanceof GalleryActivity) && ((GalleryActivity) getActivity()).galleryType != GalleryItem.GALLERY_TYPE_VIDEO_GALLERY) {
                    ((SECBBaseActivity) getActivity()).openGalleryFragment(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY);
                    isNavigationChanged = true;
                }
                //check if the displayed fragment is gallery or not
          /*      isGalleryFragment = (currentDisplayedFragment instanceof GalleryFragment);
                boolean isImageGallery=false ;

                //in case of gallery check it's image gallery or not
                if(isGalleryFragment)
                    isImageGallery=((GalleryFragment)currentDisplayedFragment).galleryType == GalleryItem.GALLERY_TYPE_IMAGE_GALLERY;

                //if the current is video gallery , then open image gallery
                if(! isGalleryFragment || isImageGallery)
                {
                    ((SECBBaseActivity)getActivity()).openGalleryFragment(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY);
                }*/
                break;
            case 7:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                if ( !(getActivity() instanceof AboutUsActivity)) // !(currentDisplayedFragment instanceof AboutUsFragment)
                {
                    ((SECBBaseActivity)getActivity()).openAboutUsFragment();
	                isNavigationChanged = true;
                }
                break;
            case 8:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
//                ((SECBBaseActivity)getActivity()).openContactUsFragment();
	            if ( !(getActivity() instanceof AboutUsActivity)) //  !(currentDisplayedFragment instanceof ContactUsFragment)
	            {
		            ((SECBBaseActivity) getActivity()).openContactUsFragment();
		            isNavigationChanged = true;
	            }
                break;
            case 9:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                confirmAbout=CONFIRM_LANGUAGE;
                Spanned messageDialog = Html.fromHtml(getResources().getString(R.string.switch_language_confirm));
                ((SECBBaseActivity)getActivity()).showConfirmDialog((SECBBaseActivity) getActivity(),
                        messageDialog.toString(), this);
                break;
            case 10:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                confirmAbout=CONFIRM_LOGOUT;
                messageDialog = Html.fromHtml(getResources().getString(R.string.logout_confirm));
                ((SECBBaseActivity)getActivity()).showConfirmDialog((SECBBaseActivity) getActivity(),
                        messageDialog.toString(), this);
                break;
        }

        if(isNavigationChanged)
            ((SECBBaseActivity)getActivity()).hideFilterLayout();
    }


    @Override
    public void onItemLongClicked(View v, int position) {

    }

    @Override
    public void onDilaogConfirmed() {
        if(confirmAbout==CONFIRM_LANGUAGE)
        {
            boolean changeToEnglish ;
            changeToEnglish = UiEngine.isDeviceLanguageArabic(getActivity());
            net.comptoirs.android.common.helper.Logger.instance().v("LANGUAGE", "onDilaogConfirmed ");
            ((SECBBaseActivity) getActivity()).changeAppLanguage(changeToEnglish);
        }
        else if(confirmAbout==CONFIRM_LOGOUT)
        {
            ((SECBBaseActivity) getActivity()).logout();
        }

    }

    @Override
    public void onDilaogCancelled() {

    }
}
