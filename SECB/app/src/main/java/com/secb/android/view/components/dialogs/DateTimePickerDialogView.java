package com.secb.android.view.components.dialogs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.secb.android.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimePickerDialogView extends LinearLayout{
    DatePicker datePicker;
    TimePicker timePicker;
    public static final int TIME_LIMIT=7;
    public DateTimePickerDialogView(Context context) {
        super(context);
        init();
    }

    public DateTimePickerDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateTimePickerDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER_HORIZONTAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_date_time_picker_dialog, null, false);
        datePicker = (DatePicker) view.findViewById(R.id.datePickerDay);
        timePicker = (TimePicker) view.findViewById(R.id.timePickerTime);
        removeAllViews();
        addView(view);
        limitDatePicker();
    }

    private void limitDatePicker() {
        Calendar now =  Calendar.getInstance();
        GregorianCalendar limit = new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH)
                ,now.get(Calendar.DAY_OF_MONTH) );
        limit.add(GregorianCalendar.DAY_OF_MONTH, TIME_LIMIT);
        datePicker.setMinDate(now.getTimeInMillis()- 1000);
        //datePicker.setMaxDate(limit.getTimeInMillis());
    }

    public Calendar getSelectedDateTime()
    {
        Calendar calender = new GregorianCalendar(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());
        return  calender;
    }
}
