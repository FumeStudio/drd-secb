package com.secb.android.view.components.filters_layouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.model.Consts;
import com.secb.android.model.EventsFilterData;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.DateTimePickerDialogView;

import java.util.Date;

public class EventsFilterLayout extends LinearLayout implements View.OnClickListener{
    private final View view;

    private EventsFilterData eventsFilterData;
    private EditText txtv_city;
    private TextView txtv_timeFrom, txtv_timeTo;
    private TextView txtv_news_filter_category_title,
            txtv_event_filter_city_title ,
            txtv_event_filter_time_from_title ,
            txtv_event_filter_time_to_title ;
    private RadioButton radbtn_allTypes;
    private RadioButton radbtn_economicType;
    private RadioButton radbtn_politicalType;
    private RadioButton radbtn_publicType;
    private Button btn_applyFilter;

    private RadioGroup radgro_newsTypes;


    private Context context;


    public View getLayoutView() {
        return view;
    }

    public EventsFilterLayout(Context context) {
        super(context);
        this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.events_filter_screen, null);
        initViews(view);
        applyFonts();
        getFilterData();
    }


    public void initViews(View view) {
        txtv_city = (EditText) view.findViewById(R.id.txtv_city_filter_city_value);
        txtv_timeFrom = (TextView) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_timeTo = (TextView) view.findViewById(R.id.txtv_news_filter_time_to_value);

        txtv_news_filter_category_title = (TextView) view.findViewById(R.id.txtv_news_filter_category_title);
        txtv_event_filter_city_title = (TextView) view.findViewById(R.id.txtv_event_filter_city_title);
        txtv_event_filter_time_from_title = (TextView) view.findViewById(R.id.txtv_event_filter_time_from_title);
        txtv_event_filter_time_to_title = (TextView) view.findViewById(R.id.txtv_event_filter_time_to_title);




        radgro_newsTypes = (RadioGroup) view.findViewById(R.id.radgro_newsTypes);


        radbtn_economicType = (RadioButton) view.findViewById(R.id.radbtn_economicType);
        radbtn_politicalType = (RadioButton) view.findViewById(R.id.radbtn_politicalType);
        radbtn_publicType = (RadioButton) view.findViewById(R.id.radbtn_publicType);
        btn_applyFilter = (Button) view.findViewById(R.id.btn_applyFilter);

        txtv_timeFrom.setOnClickListener(this);
        txtv_timeTo.setOnClickListener(this);

    }

    private void applyFonts()
    {


        if(txtv_city!=null)
        {
            UiEngine.applyCustomFont(txtv_city, UiEngine.Fonts.HVAR);
        }
        if(txtv_timeFrom!=null)
        {
            UiEngine.applyCustomFont(txtv_timeFrom, UiEngine.Fonts.HVAR);
        }
        if(txtv_timeTo!=null)
        {
            UiEngine.applyCustomFont(txtv_timeTo, UiEngine.Fonts.HVAR);
        }
        if(txtv_news_filter_category_title!=null)
        {
            UiEngine.applyCustomFont(txtv_news_filter_category_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_filter_city_title!=null)
        {
            UiEngine.applyCustomFont(txtv_event_filter_city_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_event_filter_time_from_title!=null)
        {
            UiEngine.applyCustomFont(txtv_event_filter_time_from_title, UiEngine.Fonts.HVAR);
        }
        if(radbtn_allTypes!=null)
        {
            UiEngine.applyCustomFont(radbtn_allTypes, UiEngine.Fonts.HVAR);
        }
        if(radbtn_economicType!=null)
        {
            UiEngine.applyCustomFont(radbtn_economicType, UiEngine.Fonts.HVAR);
        }
        if(radbtn_politicalType!=null)
        {
            UiEngine.applyCustomFont(radbtn_politicalType, UiEngine.Fonts.HVAR);
        }
        if(radbtn_publicType!=null)
        {
            UiEngine.applyCustomFont(radbtn_publicType, UiEngine.Fonts.HVAR);
        }
        if(btn_applyFilter!=null)
        {
            UiEngine.applyCustomFont(btn_applyFilter, UiEngine.Fonts.HVAR);
        }
    }

    public EventsFilterData getFilterData() {
        eventsFilterData = new EventsFilterData();

        eventsFilterData.city = txtv_city.getText().toString();
        eventsFilterData.timeFrom = txtv_timeFrom.getText().toString();
        eventsFilterData.timeTo = txtv_timeTo.getText().toString();

        switch (radgro_newsTypes.getCheckedRadioButtonId()) {
            case R.id.radbtn_allTypes:
                eventsFilterData.type = EventsFilterData.TYPE_ALL;
                break;
            case R.id.radbtn_economicType:
                eventsFilterData.type = EventsFilterData.TYPE_ECONOMIC;
                break;
            case R.id.radbtn_politicalType:
                eventsFilterData.type = EventsFilterData.TYPE_POLITICAL;
                break;
            case R.id.radbtn_publicType:
                eventsFilterData.type = EventsFilterData.TYPE_PUBLIC;
                break;
        }
        return eventsFilterData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtv_news_filter_time_from_value:
                showDateTimePicker(txtv_timeFrom);
                break;
            case R.id.txtv_news_filter_time_to_value:
                showDateTimePicker(txtv_timeTo);
            break;
        }
    }

    public void showDateTimePicker(final TextView textView)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DateTimePickerDialogView dialogView = new DateTimePickerDialogView(context);
        builder.setView(dialogView);

        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence time = Consts.APP_DEFAULT_DATE_TIME_FORMAT.format(new Date(dialogView.getSelectedDateTime().getTimeInMillis()));
                textView.setText(time);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
