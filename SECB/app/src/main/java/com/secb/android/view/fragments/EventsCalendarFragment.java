package com.secb.android.view.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.secb.android.R;
import com.secb.android.controller.manager.DevData;
import com.secb.android.controller.manager.EventsManager;
import com.secb.android.model.EventItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;

import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventsCalendarFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener, FlexibleCalendarView.OnDateClickListener, RequestObserver {
    private static final int EVENTS_LIST_REQUEST_ID = 6;
    private static final String TAG = "EventsCalendarFragment";
    ArrayList<EventItem> eventsList;
    View view;
    private TextView monthTextView;
    private TextView txtv_viewAllEvents;
    FlexibleCalendarView calendarView;
    private Locale currentLocale;


    View event_card_container;
    EventItem cardEventItem;
    private ImageView imgv_eventImg;
    private TextView txtv_eventTitle;
    private TextView txtv_eventDescription;
    private TextView txtv_event_timeValue;
    private TextView txtv_event_placeValue;
    private TextView txtv_event_categoryValue;

    private TextView txtv_eventImgDate_day;
    private TextView txtv_eventImgDate_month;

    public static Date lastSelectedDate;

    public static EventsCalendarFragment newInstance() {
        EventsCalendarFragment fragment = new EventsCalendarFragment();
        lastSelectedDate = new Date();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.events));
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        ((SECBBaseActivity) getActivity()).setApplyFilterClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentLocale = getResources().getConfiguration().locale;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null) {
                ((ViewGroup) oldParent).removeView(view);
            }
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.event_calendar_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        ((MainActivity) getActivity()).setEventsRequstObserver(this);
        initViews(view);
        initCalendar();
        customizeCalendarView(calendarView);
        applyFonts();
        return view;
    }


    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
        // TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);

        if (txtv_eventTitle != null)
            UiEngine.applyCustomFont(txtv_eventTitle, UiEngine.Fonts.HVAR);
        if (txtv_eventDescription != null)
            UiEngine.applyCustomFont(txtv_eventDescription, UiEngine.Fonts.HVAR);
        if (txtv_event_timeValue != null)
            UiEngine.applyCustomFont(txtv_event_timeValue, UiEngine.Fonts.HVAR);
        if (txtv_event_placeValue != null)
            UiEngine.applyCustomFont(txtv_event_placeValue, UiEngine.Fonts.HVAR);
        if (txtv_event_categoryValue != null)
            UiEngine.applyCustomFont(txtv_event_categoryValue, UiEngine.Fonts.HVAR);

        if (monthTextView != null) {
            UiEngine.applyCustomFont(monthTextView, UiEngine.Fonts.HVAR_BOLD);
        }
        if (txtv_viewAllEvents != null)
            UiEngine.applyCustomFont(txtv_viewAllEvents, UiEngine.Fonts.HVAR);

        if (txtv_eventImgDate_day != null)
            UiEngine.applyCustomFont(txtv_eventImgDate_day, UiEngine.Fonts.HVAR_BOLD);
        if (txtv_eventImgDate_month != null)
            UiEngine.applyCustomFont(txtv_eventImgDate_month, UiEngine.Fonts.HVAR);
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
            case R.id.txtv_viewAllEvents:
                ((MainActivity) getActivity()).openEventListFragment();
                break;
            case R.id.event_card_container:
                ((MainActivity) getActivity()).openEventDetailsFragment(cardEventItem);
                break;
            default:
                break;
        }
    }

    private void initViews(View view) {
        eventsList = DevData.getEventsList();
        calendarView = (FlexibleCalendarView) view.findViewById(R.id.calendar_view);
        monthTextView = (TextView) view.findViewById(R.id.month_text_view);
        txtv_viewAllEvents = (TextView) view.findViewById(R.id.txtv_viewAllEvents);
        txtv_viewAllEvents.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        txtv_eventTitle = (TextView) view.findViewById(R.id.txtv_eventTitle);
        txtv_eventDescription = (TextView) view.findViewById(R.id.txtv_eventDescription);
        txtv_event_timeValue = (TextView) view.findViewById(R.id.txtv_event_timeValue);
        txtv_event_placeValue = (TextView) view.findViewById(R.id.txtv_event_placeValue);
        txtv_event_categoryValue = (TextView) view.findViewById(R.id.txtv_event_categoryValue);


        txtv_eventImgDate_day = (TextView) view.findViewById(R.id.txtv_eventImgDate_day);
        txtv_eventImgDate_month = (TextView) view.findViewById(R.id.txtv_eventImgDate_month);

        imgv_eventImg = (ImageView) view.findViewById(R.id.imgv_eventImg);

        event_card_container = view.findViewById(R.id.event_card_container);
        event_card_container.setOnClickListener(this);

        txtv_viewAllEvents.setOnClickListener(this);


        bindEventCard(lastSelectedDate);

    }

    public void initCalendar() {
        Calendar cal = Calendar.getInstance();

        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                Calendar.LONG, currentLocale) + " " + calendarView.getSelectedDateItem().getYear());
    }


    public FlexibleCalendarView customizeCalendarView(final FlexibleCalendarView calendarView) {
        if (calendarView != null) {

            String[] customDayNames = new String[8];
            customDayNames[0] = "";
            customDayNames[1] = getString(R.string.sunday);
            customDayNames[2] = getString(R.string.monday);
            customDayNames[3] = getString(R.string.tuesday);
            customDayNames[4] = getString(R.string.wednesday);
            customDayNames[5] = getString(R.string.thursday);
            customDayNames[6] = getString(R.string.friday);
            customDayNames[7] = getString(R.string.saturday);

            calendarView.setStartDayOfTheWeek(Calendar.SUNDAY, customDayNames);
            calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
                @Override
                public void onMonthChange(int year, int month, @FlexibleCalendarView.Direction int direction) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, 1);
                    monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                            Calendar.LONG, currentLocale) + " " + year);

                }
            });

            calendarView.setShowDatesOutsideMonth(true);

            calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
                @Override
                public BaseCellView getCellView(int position, View convertView, ViewGroup parent, int cellType) {
                    BaseCellView cellView = (BaseCellView) convertView;
                    if (cellView == null) {
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        cellView = (BaseCellView) inflater.inflate(R.layout.event_calendar_date_cell_view, null);
                    }
                    return cellView;
                }

                @Override
                public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                    BaseCellView cellView = (BaseCellView) convertView;
                    if (cellView == null) {
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        cellView = (BaseCellView) inflater.inflate(R.layout.event_calendar_week_cell_view, null);
                        cellView.setBackgroundColor(getResources().getColor(android.R.color.white)); //week day bg color
                        cellView.setTextColor(getResources().getColor(R.color.sceb_dark_blue));//week day text color
                        cellView.setTextSize(16);
	                    UiEngine.applyFontsForAll(getActivity(),cellView, UiEngine.Fonts.HVAR_BOLD);
                    }
                    return cellView;
                }

                @Override
                public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                    return null;
                }
            });
        }
        calendarView.setOnDateClickListener(this);
        return calendarView;
    }

    @Override
    public void onDateClick(int year, int month, int day) {
        //parse selected date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        lastSelectedDate = calendar.getTime();
        bindEventCard(lastSelectedDate);
    }

    private void bindEventCard(Date selectedDate) {
        //get event on this day and set it to cardEventItem
        getDayEvent(selectedDate);

        if (cardEventItem == null) {
            event_card_container.setVisibility(View.GONE);
            return;
        } else {
            event_card_container.setVisibility(View.VISIBLE);
            if (!Utilities.isNullString(cardEventItem.ImageUrl)) {
                Glide.with(getActivity())
                        .load(cardEventItem.ImageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.events_image_place_holder)
                        .into(imgv_eventImg);
            } else
                imgv_eventImg.setImageResource(R.drawable.events_image_place_holder);

            String evdateStr = MainActivity.reFormatDate(cardEventItem.EventDate, MainActivity.sdf_day_mon);
            if (!Utilities.isNullString(evdateStr)) {
                String[] dayMonthArr = evdateStr.split("-");
                txtv_eventImgDate_day.setText(dayMonthArr[0]);
                txtv_eventImgDate_month.setText(dayMonthArr[1].toUpperCase());
            }


            txtv_eventTitle.setText(cardEventItem.Title);
            txtv_eventDescription.setText(cardEventItem.Description);
            evdateStr = MainActivity.reFormatDate(cardEventItem.EventDate, MainActivity.sdf_Date);
            txtv_event_timeValue.setText(evdateStr);
            txtv_event_placeValue.setText(cardEventItem.EventSiteCity);
            txtv_event_categoryValue.setText(cardEventItem.EventCategory);
        }

    }

    private void getDayEvent(Date selectedDate) {
        if (selectedDate == null) {
            return;
        }
        cardEventItem = EventsManager.getInstance().getEventOnDate(selectedDate);

    }

    @Override
    public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
        if (error == null) {
            Logger.instance().v(TAG, "Success \n\t\t" + resultObject);
            if ((int) requestId == EVENTS_LIST_REQUEST_ID && resultObject != null) {
                eventsList = (ArrayList<EventItem>) resultObject;
                bindEventCard(lastSelectedDate);
            }

        } else
            bindEventCard(null);
    }

    @Override
    public void requestCanceled(Integer requestId, Throwable error) {

    }

    @Override
    public void updateStatus(Integer requestId, String statusMsg) {

    }
}
