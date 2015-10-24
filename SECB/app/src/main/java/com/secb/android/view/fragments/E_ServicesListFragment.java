package com.secb.android.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.manager.DevData;
import com.secb.android.model.E_ServiceItem;
import com.secb.android.model.EventsFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.dialogs.ProgressWheel;
import com.secb.android.view.components.filters_layouts.EventsFilterLayout;
import com.secb.android.view.components.recycler_e_service.E_ServiceItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import java.util.ArrayList;

public class E_ServicesListFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener, RecyclerCustomClickListener

{
    RecyclerView eServicesRecyclerView;
    E_ServiceItemRecyclerAdapter e_serviceItemRecyclerAdapter;
    ArrayList<E_ServiceItem> eServicesList;
    EventsFilterData eventsFilterData;
    LinearLayout layout_graphs_container;
    ProgressWheel progressWheelClosed, progressWheelInbox, progressWheelInProgress;
    private static final int PROGRESS_WHEEL_TIME = 2 * 1000;
    int[] graphsValues;
    TextView txtv_graph_title_closed, txtv_graph_value_closed,
            txtv_graph_title_inbox, txtv_graph_value_inbox,
            txtv_graph_title_inProgress, txtv_graph_value_inProgress;

    View view;
    private EventsFilterLayout eventsFilterLayout = null;


    public static E_ServicesListFragment newInstance() {
        E_ServicesListFragment fragment = new E_ServicesListFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.eservices));
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
            if (oldParent != container && oldParent != null) {
                ((ViewGroup) oldParent).removeView(view);
            }
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.e_services_list_fragment, container, false);
            handleButtonsEvents();

        }
        initViews(view);
        applyFonts();
        initFilterLayout();
        return view;
    }

    public void initFilterLayout() {
        eventsFilterLayout = new EventsFilterLayout(getActivity());
        ((SECBBaseActivity) getActivity()).setFilterLayout(eventsFilterLayout, false);
        ((SECBBaseActivity) getActivity()).setFilterLayoutView(eventsFilterLayout.getLayoutView());
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
        // TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);

        UiEngine.applyFontsForAll(getActivity(),layout_graphs_container, UiEngine.Fonts.HVAR);
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
        eventsFilterData = this.eventsFilterLayout.getFilterData();
        if (eventsFilterData != null) {
            ((SECBBaseActivity) getActivity()).displayToast("Filter Data \n " +
                    " city: " + eventsFilterData.city + "\n" +
                    " Time From: " + eventsFilterData.timeFrom + "\n" +
                    " Time To: " + eventsFilterData.timeTo + " \n" +
                    " Type: " + eventsFilterData.selectedCategoryId);
        }
    }


    private void initViews(View view) {
        progressWheelClosed = (ProgressWheel) view.findViewById(R.id.progressWheelClosed);
        progressWheelInbox = (ProgressWheel) view.findViewById(R.id.progressWheelInbox);
        progressWheelInProgress = (ProgressWheel) view.findViewById(R.id.progressWheelProgress);


        txtv_graph_title_closed = (TextView) view.findViewById(R.id.txtv_graph_title_closed);
        txtv_graph_value_closed = (TextView) view.findViewById(R.id.txtv_graph_value_closed);

        txtv_graph_title_inbox = (TextView) view.findViewById(R.id.txtv_graph_title_inbox);
        txtv_graph_value_inbox = (TextView) view.findViewById(R.id.txtv_graph_value_inbox);

        txtv_graph_title_inProgress = (TextView) view.findViewById(R.id.txtv_graph_title_inProgress);
        txtv_graph_value_inProgress = (TextView) view.findViewById(R.id.txtv_graph_value_inProgress);


        txtv_graph_title_closed.setText(Html.fromHtml(getString(R.string.graph_title_closed)));
        txtv_graph_title_inbox.setText(Html.fromHtml(getString(R.string.graph_title_inbox)));
        txtv_graph_title_inProgress.setText(Html.fromHtml(getString(R.string.graph_title_inProgress)));
        layout_graphs_container = (LinearLayout) view.findViewById(R.id.layout_graphs_container);

        eServicesList = DevData.getE_ServicesList();
        eServicesRecyclerView = (RecyclerView) view.findViewById(R.id.eServicesRecyclerView);
        e_serviceItemRecyclerAdapter = new E_ServiceItemRecyclerAdapter(getActivity(), eServicesList);
        eServicesRecyclerView.setAdapter(e_serviceItemRecyclerAdapter);
        eServicesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        eServicesRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), eServicesRecyclerView, this));

        graphsValues = new int[]{15, 36, 82};
        fillWheelPercentage(graphsValues[0], graphsValues[1], graphsValues[2]);

    }

    private void fillWheelPercentage(int closedScore, int inboxScore, int inProgressScore) {

        int largeRadius = (int) getResources().getDimension(R.dimen.home_graphs_wheel_size);

        txtv_graph_value_closed.setText("" + closedScore);
        txtv_graph_value_inbox.setText("" + inboxScore);
        txtv_graph_value_inProgress.setText("" + inProgressScore);

        progressWheelClosed.setColors(getResources().getColor(R.color.graph_color_closed), getResources().getColor(R.color.sceb_dark_blue));
        progressWheelInbox.setColors(getResources().getColor(R.color.graph_color_inbox), getResources().getColor(R.color.sceb_dark_blue));
        progressWheelInProgress.setColors(getResources().getColor(R.color.graph_color_inProgress), getResources().getColor(R.color.sceb_dark_blue));

        progressWheelClosed.startLoading(closedScore, PROGRESS_WHEEL_TIME, "" + closedScore, largeRadius);
        progressWheelInbox.startLoading(inboxScore, PROGRESS_WHEEL_TIME, "" + inboxScore, largeRadius);
        progressWheelInProgress.startLoading(inProgressScore, PROGRESS_WHEEL_TIME, "" + inProgressScore, largeRadius);
    }

    @Override
    public void onItemClicked(View v, int position) {
        ((MainActivity) getActivity()).openE_ServiceDetailsFragment(eServicesList.get(position));
    }

    @Override
    public void onItemLongClicked(View v, int position) {

    }
}
