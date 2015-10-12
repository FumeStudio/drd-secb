package com.secb.android.view.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.secb.android.R;
import com.secb.android.controller.manager.DevData;
import com.secb.android.model.EventItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EventsCalendarFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener ,FlexibleCalendarView.OnDateClickListener
{
    ArrayList<EventItem> eventsList;
    View view;
    private TextView monthTextView;
    private TextView txtv_viewAllEvents;
    FlexibleCalendarView calendarView;
    private Locale currentLocale;

    public static EventsCalendarFragment newInstance() {
        EventsCalendarFragment fragment = new EventsCalendarFragment();
        return fragment;
    }

    @Override
    public void onResume()
    {
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
            if (oldParent != container && oldParent != null)
            {
                ((ViewGroup) oldParent).removeView(view);
            }
        }
        else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.event_calendar_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }
        initViews(view);
        initCalendar();
        customizeCalendarView(calendarView);
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
                ((MainActivity)getActivity()).openEventListFragment();
                break;
            default:
                break;
        }
    }

    private void initViews(View view)
    {
        eventsList = DevData.getEventsList();
        calendarView = (FlexibleCalendarView)view.findViewById(R.id.calendar_view);
        monthTextView = (TextView)view.findViewById(R.id.month_text_view);
        txtv_viewAllEvents = (TextView)view.findViewById(R.id.txtv_viewAllEvents);
        txtv_viewAllEvents.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtv_viewAllEvents.setOnClickListener(this);
    }

    public void initCalendar(){
        Calendar cal = Calendar.getInstance();

        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                Calendar.LONG, currentLocale) + " " + calendarView.getSelectedDateItem().getYear());
    }


    public FlexibleCalendarView customizeCalendarView(FlexibleCalendarView calendarView){
        if(calendarView!=null)
        {
            calendarView.setStartDayOfTheWeek(Calendar.SUNDAY);
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
                        cellView.setTextSize(13);
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
        ((SECBBaseActivity)getActivity()).displayToast(day+"-"+month+"-"+year);
    }
}
