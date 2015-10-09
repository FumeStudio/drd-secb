package com.secb.android.view.components.filters_layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.secb.android.R;
import com.secb.android.model.EventsFilterData;

public class EventsFilterLayout extends LinearLayout {
    private final View view;

    private EventsFilterData eventsFilterData;
    private EditText txtv_city,txtv_timeFrom,txtv_timeTo;
    private RadioGroup radgro_newsTypes;

    public View getLayoutView() {
        return view;
    }

    public EventsFilterLayout(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.events_filter_screen, null);
        getFilterData();
    }

    public EventsFilterData getFilterData()
    {
        eventsFilterData = new EventsFilterData();
        txtv_city= (EditText) view.findViewById(R.id.txtv_city_filter_city_value);
        txtv_timeFrom= (EditText) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_timeTo= (EditText) view.findViewById(R.id.txtv_news_filter_time_to_value);
        radgro_newsTypes= (RadioGroup) view.findViewById(R.id.radgro_newsTypes);

        eventsFilterData.city=txtv_city.getText().toString();
        eventsFilterData.timeFrom=txtv_timeFrom.getText().toString();
        eventsFilterData.timeTo=txtv_timeTo.getText().toString();

        switch (radgro_newsTypes.getCheckedRadioButtonId()){
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

}
