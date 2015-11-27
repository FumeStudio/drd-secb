package com.secb.android.view.components.recycler_news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.model.NewsItem;
import com.secb.android.view.MainActivity;
import com.secb.android.view.components.FooterLoaderAdapter;

import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsItemRecyclerAdapter extends FooterLoaderAdapter<NewsItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<NewsItem>itemsList = Collections.emptyList();
    Context context;
	private View view;
//	private SparseBooleanArray selectedItems;

	private ArrayList<Integer>selectedIndices;
	private ArrayList<View>selectedViews;

	public NewsItemRecyclerAdapter(Context context, List<NewsItem> itemsList) {
        super(context);
        setItemsList(itemsList);
	    this.context=context;
		selectedIndices = new ArrayList<>();
		selectedViews = new ArrayList<>();
	    if(context == null)
	    {
		    this.context= CTApplication.getContext();
	    }
	    try
	    {
		    this.inflater = LayoutInflater.from(context);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
    }

    @Override
    public long getYourItemId(int position) {
        return (itemsList.get(position).ID).hashCode();
    }

    public void setItemsList(List<NewsItem> itemsList) {
        this.itemsList = itemsList;
        setItems(itemsList);
    }

	@Override
    public NewsItemRecyclerViewHolder getYourItemViewHolder(ViewGroup parent)
	{
		if(inflater==null && context!=null)
			this.inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.news_item_card, parent, false);

        NewsItemRecyclerViewHolder vh = new NewsItemRecyclerViewHolder(view);
        return vh;
    }


    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder _holder, int position)
    {
        NewsItemRecyclerViewHolder holder = (NewsItemRecyclerViewHolder) _holder;
//	    view.setSelected(selectedItems.get(position, false));
        final NewsItem currentItem = itemsList.get(position);
		Logger.instance().v("News-Card", "Image: " + currentItem.getImageUrl(), false);
	    if(!Utilities.isNullString(currentItem.getImageUrl()))
	    {
            Glide.with(context).load(currentItem.getImageUrl())
                    .placeholder(R.drawable.news_placeholder)
//                    .signature(new StringSignature(user.userPhotoID != null ? user.userPhotoID : ""))
                    .centerCrop()
                    .into(holder.imgv_newImg);
        }
	    else
		    holder.imgv_newImg.setImageResource(R.drawable.news_placeholder);

        holder.txtv_newTitle.setText(currentItem.Title);
        holder.txtv_newDescription.setText(currentItem.NewsBrief);
	    String newdateStr = MainActivity.reFormatNewsDate(currentItem.CreationDate, MainActivity.sdf_Date);
        holder.txtv_newTime.setText(newdateStr);

	    // Set the selected state
//	    holder.layout_item_root.setSelected(selectedItems.get(position, false));
	    if(Utilities.isTablet(context))
	        holder.layout_item_root.setSelected(selectedIndices.contains(position));
    }

    public int getItemCount() {
        return super.getItemCount();
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


	public void setItemSelected(View v,int index) {
		if(!Utilities.isTablet(context)) return;
		/*removeSelection();
		selectedIndices.add(index);
		selectedViews.add(v);
		v.setSelected(true);
//		UiEngine.setListItemSelected(v);
		UiEngine.setAllTextsSelected(v, true);*/

		setItemSelected(selectedIndices,selectedViews,context,v,index);

	}

	private void removeSelection() {
		if(!Utilities.isTablet(context)) return;
		/*selectedIndices.clear();
		for(View v:selectedViews){
			v.setSelected(false);
			UiEngine.setAllTextsSelected(v, false);
		}*/
	}
}
