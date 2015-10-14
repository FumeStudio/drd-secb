package com.secb.android.view.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.GalleryItem;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.recycler_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_click_handlers.RecyclerCustomItemTouchListener;
import com.secb.android.view.menu.items_recycler.MenuItemObject;
import com.secb.android.view.menu.items_recycler.MenuItemRecyclerAdapter;

import java.util.ArrayList;

public class MenuFragment extends Fragment implements View.OnClickListener, RecyclerCustomClickListener {
    View view;
    int []icons;

    String[] strings ;
    ArrayList<MenuItemObject> menuItemsList;

    RecyclerView menuItemRecyclerView;
    MenuItemRecyclerAdapter menuItemRecyclerAdapter;


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
            applyFonts();

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

    private void applyFonts() {
        // TODO:: apply fonts for all texts
//		UiEngine.applyCustomFont(layoutCategoriesMenu, UiEngine.Fonts.EUCLID_BP_BOLD);
//		UiEngine.applyCustomFont(layoutRandomBlogMenu, UiEngine.Fonts.EUCLID_BP_BOLD);
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
        switch (position)
        {
            case 0:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                ((MainActivity)getActivity()).openHomeFragment(true);
                break;
            case 2:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                ((MainActivity)getActivity()).openNewsListFragment();
                break;
            case 3:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                ((MainActivity)getActivity()).openEguideHomeFragment();
                break;
            case 4:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                ((MainActivity)getActivity()).openEvenCalendarFragment();
                break;
            case 5:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                ((MainActivity)getActivity()).openGalleryFragment(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY, -1);
                break;
            case 6:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                ((MainActivity)getActivity()).openGalleryFragment(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY,-1);
                break;
            case 9:
                ((SECBBaseActivity)getActivity()).closeMenuPanel();
                TextView txv = ((TextView) v.findViewById(R.id.text_itemText) );
                boolean changeToEnglish = true;
                if(txv!=null)
                {
                    if(txv.getText().toString().equalsIgnoreCase("English") )
                        changeToEnglish=true;
                    else
                        changeToEnglish=false;
                }
                ((SECBBaseActivity) getActivity()).changeAppLanguage(changeToEnglish);
                break;
        }
    }

    @Override
    public void onItemLongClicked(View v, int position) {

    }
}
