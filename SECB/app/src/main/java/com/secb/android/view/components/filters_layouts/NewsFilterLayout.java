package com.secb.android.view.components.filters_layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.secb.android.R;
import com.secb.android.model.NewsFilterData;

public class NewsFilterLayout extends LinearLayout {
    private final View view;

    private NewsFilterData newsFilterData;
    private EditText txtv_timeFrom,txtv_timeTo;
    private RadioGroup radgro_newsTypes;

    public View getLayoutView() {
        return view;
    }

    public NewsFilterLayout(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.news_filter_screen, null);
        getFilterData();
    }

    public NewsFilterData getFilterData()
    {
        newsFilterData = new NewsFilterData();
        txtv_timeFrom= (EditText) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_timeTo= (EditText) view.findViewById(R.id.txtv_news_filter_time_to_value);
        radgro_newsTypes= (RadioGroup) view.findViewById(R.id.radgro_newsTypes);

        newsFilterData.timeFrom=txtv_timeFrom.getText().toString();
        newsFilterData.timeTo=txtv_timeTo.getText().toString();

        switch (radgro_newsTypes.getCheckedRadioButtonId()){
            case R.id.radbtn_allTypes:
                newsFilterData.type = NewsFilterData.TYPE_ALL;
                break;
            case R.id.radbtn_economicType:
                newsFilterData.type = NewsFilterData.TYPE_ECONOMIC;
                break;
            case R.id.radbtn_politicalType:
                newsFilterData.type = NewsFilterData.TYPE_POLITICAL;
                break;
            case R.id.radbtn_publicType:
                newsFilterData.type = NewsFilterData.TYPE_PUBLIC;
                break;
        }
        return newsFilterData;
    }

}
