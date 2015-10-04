package com.secb.android.view.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.menu.items_recycler.MenuItemObject;
import com.secb.android.view.menu.items_recycler.MenuItemRecyclerAdapter;

import java.util.ArrayList;

public class MenuFragment extends Fragment implements View.OnClickListener {
    View view;
    int []icons= {R.drawable.home_icon,R.drawable.eservices_icon,R.drawable.news_icon,
            R.drawable.eguide_icon,R.drawable.events_icon,R.drawable.images_icon,
            R.drawable.videos_icon,R.drawable.aboutus_icon,R.drawable.contactus_icon};

    String[] strings = {getResources().getString(R.string.home),getResources().getString(R.string.eservices),
            getResources().getString(R.string.news),getResources().getString(R.string.eguide),
            getResources().getString(R.string.events),getResources().getString(R.string.images),
            getResources().getString(R.string.videos),getResources().getString(R.string.aboutus),
            getResources().getString(R.string.contactus)
    };
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.menu, container, false);
            initializeMenu(view);
            handleButtonsEvents();
            applyFonts();

            return view;
        }
    }

    private void initializeMenu(View view) {
        menuItemRecyclerView = (RecyclerView)view.findViewById(R.id.menuItemRecyclerView);
        menuItemRecyclerAdapter = new MenuItemRecyclerAdapter(getActivity(), getMenuItems());
        /*adapter.setRecyclerClickListener(this);*/
        menuItemRecyclerView.setAdapter(menuItemRecyclerAdapter);
        menuItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*menuItemRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(getActivity(),menuItemRecyclerView,new RecyclerClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Toast.makeText(getActivity(), "onClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClicked(View v, int position)
            {
                Toast.makeText(getActivity(),"onLongClicked",Toast.LENGTH_SHORT).show();
            }
        }));*/
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

    //make list of items used in the sliding menu
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
}
