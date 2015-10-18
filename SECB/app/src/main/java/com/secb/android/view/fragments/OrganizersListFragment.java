package com.secb.android.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.controller.manager.DevData;
import com.secb.android.model.OrganizerItem;
import com.secb.android.model.OrganizersFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.filters_layouts.OrganizersFilterLayout;
import com.secb.android.view.components.recycler_organizers.OrganizerItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import java.util.ArrayList;

public class OrganizersListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener ,RecyclerCustomClickListener

{
    RecyclerView organizerRecyclerView;
    OrganizerItemRecyclerAdapter organizerItemRecyclerAdapter;
    ArrayList<OrganizerItem> organizerList;
    OrganizersFilterData organizerFilterData;

    View view;
    private OrganizersFilterLayout organizersFilterLayout =null;


    public static OrganizersListFragment newInstance() {
        OrganizersListFragment fragment = new OrganizersListFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.organizers_eguide));
        ((SECBBaseActivity) getActivity()).showFilterButton(true);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
        ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
        ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();

    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null)
            {
                ((ViewGroup) oldParent).removeView(view);
            }
        }
        else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.organizers_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        initViews(view);
        initFilterLayout();
        return view;
    }

    public void initFilterLayout()
    {
        organizersFilterLayout = new OrganizersFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(organizersFilterLayout,false);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(organizersFilterLayout.getLayoutView());
    }
    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
        // TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
    }

    private void goBack() {
        ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(getClass().getName());
    }

    // ////////////////////////////////////////////////////////////

    @Override
    public void onBack() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewBackHeader:
                onBack();
                break;

            case R.id.btn_applyFilter:
                getFilterDataObject();
                break;
            default:
                break;
        }
    }

    private void getFilterDataObject() {
        organizerFilterData =this.organizersFilterLayout.getFilterData();
        if(organizerFilterData !=null){
            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
                    "Name : "+ organizerFilterData.name+"\n" +
                    "City : "+ organizerFilterData.city+" \n" );
        }
    }


    private void initViews(View view)
    {
        organizerList = DevData.getOrganizersList();
        organizerRecyclerView = (RecyclerView) view.findViewById(R.id.organizerRecyclerView);
        organizerItemRecyclerAdapter = new OrganizerItemRecyclerAdapter(getActivity(), organizerList);
        organizerRecyclerView.setAdapter(organizerItemRecyclerAdapter);
//        organizerRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        organizerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        organizerRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), organizerRecyclerView, this));
    }

    @Override
    public void onItemClicked(View v, int position)
    {
        ((MainActivity) getActivity()).openOrganizerDetailsFragment(organizerList.get(position));
    }

    @Override
    public void onItemLongClicked(View v, int position)
    {

    }
}
