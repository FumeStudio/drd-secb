package com.secb.android.view.components.recycler_news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.NewsCategoryItem;
import com.secb.android.view.UiEngine;

import java.util.Collections;
import java.util.List;

public class NewsCategoryFilterRecyclerAdapter extends RecyclerView.Adapter<NewsCategoryFilterRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<NewsCategoryItem>itemsList = Collections.emptyList();
    Context context;

    public NewsCategoryFilterRecyclerAdapter(Context context, List<NewsCategoryItem> itemsList) {
        this.inflater = LayoutInflater.from(context);
        this.itemsList = itemsList;
        this.context=context;

    }



	@Override
    public NewsCategoryFilterRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_category_filter_item_card, parent, false);

		NewsCategoryFilterRecyclerViewHolder vh = new NewsCategoryFilterRecyclerViewHolder(view);
        return vh;
    }


	@Override
    public void onBindViewHolder(final NewsCategoryFilterRecyclerViewHolder holder, final int position)
    {
	    NewsCategoryItem currentItem = itemsList.get(position);
	    String radbtn_text = UiEngine.isAppLanguageArabic(context)?currentItem.CategoryArabic:currentItem.CategoryEnglish;
	    holder.radbtn_categoryItem.setText(radbtn_text);
	    holder.radbtn_categoryItem.setChecked(currentItem.isSelected);

	    holder.radbtn_categoryItem.setOnClickListener(new OnClickListener(){

		    @Override
		    public void onClick(View v) {
			    setItemChecked(position,holder.radbtn_categoryItem.isChecked());
		    }
	    });
    }

	private void setItemChecked(int position, boolean checked) {
		if(checked)
		{
			for (int i = 0 ; i<this.itemsList.size();i++)
			{
				if(i==position)
					this.itemsList.get(i).isSelected=true;
				else
					this.itemsList.get(i).isSelected=false;
			}
			notifyDataSetChanged();

		}
	}

	@Override
    public int getItemCount()
	{
		if(itemsList!=null)
            return itemsList.size();
		return 0;
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


}
