package com.p_v.flexiblecalendar;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IWeekCellViewDrawer;
import com.p_v.fliexiblecalendar.R;

import java.text.DateFormatSymbols;

/**
 * @author p-v
 */
public class WeekdayNameDisplayAdapter extends ArrayAdapter<WeekdayNameDisplayAdapter.WeekDay>{

    private final Context context  ;
    private IWeekCellViewDrawer cellViewDrawer;
    private WeekDay[] weekDayArray;
    private String[] customDayNames;

    public WeekdayNameDisplayAdapter(Context context, int textViewResourceId, int startDayOfTheWeek) {
        super(context, textViewResourceId);
        initializeWeekDays(startDayOfTheWeek, customDayNames);
        this.context = context;
    }

    @Override
    public WeekDay getItem(int position) {
        return weekDayArray[position];
    }

    @Override
    public int getCount() {
        return weekDayArray.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseCellView cellView = cellViewDrawer.getCellView(position, convertView, parent);
        if(cellView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            cellView = (BaseCellView)inflater.inflate(R.layout.square_cell_layout,null);
        }
        WeekDay weekDay = getItem(position);
        String weekdayName = cellViewDrawer.getWeekDayName(weekDay.index, weekDay.displayValue); //adding 1 as week days starts from 1 in Calendar
        if(TextUtils.isEmpty(weekdayName)){
            weekdayName = weekDay.displayValue;
        }
        cellView.setText(weekdayName);
//        cellView.setText(weekdayName.charAt(0)+"");
        return cellView;
    }

    private void initializeWeekDays(int startDayOfTheWeek, String[] customDayNames){
        DateFormatSymbols symbols = new DateFormatSymbols(FlexibleCalendarHelper.getLocale(getContext()));
        String[] weekDayList ;
        if(customDayNames ==null || customDayNames.length<8 )
            weekDayList = symbols.getShortWeekdays(); // weekday list has 8 elements
        else
            weekDayList= customDayNames;


        weekDayArray = new WeekDay[7];
        //reordering array based on the start day of the week
        for(int i = 1; i<weekDayList.length; i++){
            WeekDay weekDay = new WeekDay();
            weekDay.index = i;
            weekDay.displayValue = weekDayList[i];
            int tempVal = i - startDayOfTheWeek;
            weekDayArray[tempVal <0 ? 7 + tempVal : tempVal] = weekDay;
        }
    }


    public class WeekDay{
        int index;
        String displayValue;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public void setCellView(IWeekCellViewDrawer cellView) {
        this.cellViewDrawer = cellView;
    }

    public IWeekCellViewDrawer getCellViewDrawer(){
        return cellViewDrawer;
    }

    public void setStartDayOfTheWeek(int startDayOfTheWeek ,String[] customDayNames)
    {
        this.customDayNames = customDayNames;
        initializeWeekDays(startDayOfTheWeek , customDayNames);
        this.notifyDataSetChanged();
    }

}
