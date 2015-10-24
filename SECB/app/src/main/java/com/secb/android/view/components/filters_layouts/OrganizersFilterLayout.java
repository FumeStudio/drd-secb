package com.secb.android.view.components.filters_layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.secb.android.R;
import com.secb.android.model.OrganizersFilterData;
import com.secb.android.view.UiEngine;

public class OrganizersFilterLayout extends LinearLayout {
    private final View view;
    private final Context context;

    private OrganizersFilterData organizersFilterData;
    private EditText txtv_city,txtv_name;


    public View getLayoutView() {
        return view;
    }

    public OrganizersFilterLayout(Context context)
    {
        super(context);
        this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.organizers_filter_screen, null);
        initViews(view);
        applyFonts(view);
        getFilterData();
    }

    private void initViews(View view) {
        organizersFilterData = new OrganizersFilterData();
        txtv_name= (EditText) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_city= (EditText) view.findViewById(R.id.txtv_city_filter_city_value);

    }

    private void applyFonts(View view) {
        UiEngine.applyFontsForAll(context, view, UiEngine.Fonts.HVAR);
    }

    public OrganizersFilterData getFilterData()
    {

	    UiEngine.applyFontsForAll(context,view, UiEngine.Fonts.HVAR);
        organizersFilterData.name=txtv_name.getText().toString();
        organizersFilterData.city=txtv_city.getText().toString();

        
        return organizersFilterData;
    }

}
