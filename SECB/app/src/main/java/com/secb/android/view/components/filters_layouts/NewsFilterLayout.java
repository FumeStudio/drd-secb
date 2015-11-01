package com.secb.android.view.components.filters_layouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.NewsCategoryOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.NewsManager;
import com.secb.android.model.NewsCategoryItem;
import com.secb.android.model.NewsFilterData;
import com.secb.android.view.MainActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.dialogs.DateTimePickerDialogView;
import com.secb.android.view.components.recycler_news.NewsCategoryFilterRecyclerAdapter;

import net.comptoirs.android.common.controller.backend.RequestObserver;

import java.util.Date;
import java.util.List;

public class NewsFilterLayout extends LinearLayout implements View.OnClickListener, RequestObserver {
	private final View view;


	private NewsFilterData newsFilterData;
    private TextView txtv_timeFrom, txtv_timeTo;
    private TextView txtv_news_filter_time_title, txtv_news_filter_type_title;
    private TextView txtv_noData;
	private RecyclerView newsCategoriesRecyclerView;
	private NewsCategoryFilterRecyclerAdapter newsCategoryFilterRecyclerAdapter;

    private RadioGroup radgro_newsTypes;
    private RadioButton radbtn_allTypes;
    private RadioButton radbtn_economicType;
    private RadioButton radbtn_politicalType;
    private RadioButton radbtn_publicType;
    private Button btn_applyFilter;

    private Context context;
	private List<NewsCategoryItem> categoriesList;
	private boolean isCategoryOperationDone;


	public View getLayoutView() {
        return view;
    }

    public NewsFilterLayout(Context context) {
        super(context);
        this.context=context;
        view = LayoutInflater.from(context).inflate(R.layout.news_filter_screen, null);
        initViews(view);
	    ((MainActivity)context).setNewsRequstObserver(this);
        applyFonts();

	    categoriesList = NewsManager.getInstance().getNewsCategoryList(context);
	    if(categoriesList==null || categoriesList.size()==0)
	    {
		    startNewsCategoriesOperation();
	    }
	    else
			getFilterData();
    }

	private void startNewsCategoriesOperation() {
		NewsCategoryOperation operation = new NewsCategoryOperation(RequestIds.NEWS_CATEGORY_REQUEST_ID, false, context);
		operation.addRequsetObserver(this);
		operation.execute();
	}


	private void initViews(View view) {

        newsFilterData = new NewsFilterData();
        txtv_timeFrom = (TextView) view.findViewById(R.id.txtv_news_filter_time_from_value);
        txtv_timeTo = (TextView) view.findViewById(R.id.txtv_news_filter_time_to_value);

        txtv_news_filter_time_title = (TextView) view.findViewById(R.id.txtv_news_filter_time_title);
        txtv_news_filter_type_title = (TextView) view.findViewById(R.id.txtv_news_filter_type_title);

        radgro_newsTypes = (RadioGroup) view.findViewById(R.id.radgro_newsTypes);
        radbtn_allTypes = (RadioButton) view.findViewById(R.id.radbtn_allTypes);

        radbtn_economicType = (RadioButton) view.findViewById(R.id.radbtn_economicType);
        radbtn_politicalType = (RadioButton) view.findViewById(R.id.radbtn_politicalType);
        radbtn_publicType = (RadioButton) view.findViewById(R.id.radbtn_publicType);
        btn_applyFilter = (Button) view.findViewById(R.id.btn_applyFilter);

	    txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
	    newsCategoriesRecyclerView = (RecyclerView) view.findViewById(R.id.newsCategoriesRecyclerView);
	    newsCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
	    categoriesList= NewsManager.getInstance().getNewsCategoryList(context);
	    bindCategoriesRecycler();

	    txtv_timeFrom.setOnClickListener(this);
        txtv_timeTo.setOnClickListener(this);
    }


	private void bindCategoriesRecycler() {
		if (categoriesList != null && categoriesList.size() > 0)
		{
			newsCategoriesRecyclerView.setVisibility(View.VISIBLE);
			txtv_noData.setVisibility(View.GONE);
			newsCategoryFilterRecyclerAdapter = new NewsCategoryFilterRecyclerAdapter(context,categoriesList);
			newsCategoriesRecyclerView.setAdapter(newsCategoryFilterRecyclerAdapter);
		}
		else
		{
			newsCategoriesRecyclerView.setVisibility(View.GONE);
			txtv_noData.setVisibility(View.VISIBLE);
			if(isCategoryOperationDone)
				txtv_noData.setText(context.getString(R.string.news_no_types));


		}
	}
    private void applyFonts() {

	    UiEngine.applyFontsForAll(context,view, UiEngine.Fonts.HVAR);
        /*if(txtv_timeFrom!=null)
        {
            UiEngine.applyCustomFont(txtv_timeFrom, UiEngine.Fonts.HVAR);
        }
        if(txtv_timeTo!=null)
        {
            UiEngine.applyCustomFont(txtv_timeTo, UiEngine.Fonts.HVAR);
        }
        if(txtv_news_filter_time_title!=null)
        {
            UiEngine.applyCustomFont(txtv_news_filter_time_title, UiEngine.Fonts.HVAR);
        }
        if(txtv_news_filter_type_title!=null)
        {
            UiEngine.applyCustomFont(txtv_news_filter_type_title, UiEngine.Fonts.HVAR);
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
        if(txtv_noData!=null)
        {
            UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
        }*/

    }
    public NewsFilterData getFilterData() {
	    newsFilterData.newsID="All";
        newsFilterData.timeFrom = txtv_timeFrom.getText().toString();
        newsFilterData.timeTo = txtv_timeTo.getText().toString();

	    NewsCategoryItem selectedCategory = NewsManager.getInstance().getSelectedCategory();

	    if( selectedCategory!=null)
	    {
		    newsFilterData.newsCategory = (UiEngine.isAppLanguageArabic(context)?
	        selectedCategory.CategoryArabic:selectedCategory.CategoryEnglish);
		    newsFilterData.selectedCategoryId = selectedCategory.ID;

	    }

        /*switch (radgro_newsTypes.getCheckedRadioButtonId()) {
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
        }*/
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
//                CharSequence time = Consts.APP_DEFAULT_DATE_TIME_FORMAT.format(new Date(dialogView.getSelectedDateTime().getTimeInMillis()));
                String time = MainActivity.sdf_Date.format(new Date(dialogView.getSelectedDateTime().getTimeInMillis()));
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

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		newsCategoriesRecyclerView.setAdapter(null);
	}

	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
		if (error == null)
		{
			if((int)requestId == RequestIds.NEWS_CATEGORY_REQUEST_ID && resultObject!=null)
			{
				isCategoryOperationDone=true;
				categoriesList= NewsManager.getInstance().getNewsCategoryList(context);
				bindCategoriesRecycler();
			}
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}
}
