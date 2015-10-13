package com.secb.android.view.components.filters_layouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.secb.android.R;
import com.secb.android.model.Consts;
import com.secb.android.model.NewsFilterData;
import com.secb.android.view.components.DateTimePickerDialogView;

import java.util.Date;

public class NewsFilterLayout extends LinearLayout implements View.OnClickListener {
    private final View view;

    private NewsFilterData newsFilterData;
    private TextView txtv_timeFrom, txtv_timeTo;
    private RadioGroup radgro_newsTypes;
    private Context context;


    public View getLayoutView() {
        return view;
    }

    public NewsFilterLayout(Context context) {
        super(context);
        this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.news_filter_screen, null);
        initViews(view);
        getFilterData();
    }

    private void initViews(View view) {

        newsFilterData = new NewsFilterData();
        txtv_timeFrom = (TextView) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_timeTo = (TextView) view.findViewById(R.id.txtv_news_filter_time_to_value);
        radgro_newsTypes = (RadioGroup) view.findViewById(R.id.radgro_newsTypes);

        txtv_timeFrom.setOnClickListener(this);
        txtv_timeTo.setOnClickListener(this);
    }

    public NewsFilterData getFilterData() {
        newsFilterData.timeFrom = txtv_timeFrom.getText().toString();
        newsFilterData.timeTo = txtv_timeTo.getText().toString();

        switch (radgro_newsTypes.getCheckedRadioButtonId()) {
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

    private void showDateTimePicker(final TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DateTimePickerDialogView dialogView = new DateTimePickerDialogView(context);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence time = Consts.APP_DEFAULT_DATE_TIME_FORMAT.format(new Date(dialogView.getSelectedDateTime().getTimeInMillis()));
                textView.setText(time);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
