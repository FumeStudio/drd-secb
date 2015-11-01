package com.secb.android.view.components.recycler_news;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.secb.android.R;
import com.secb.android.model.NewsItem;

import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NewsItemRecyclerAdapter extends RecyclerView.Adapter<NewsItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<NewsItem>itemsList = Collections.emptyList();
    Context context;

    public NewsItemRecyclerAdapter(Context context, List<NewsItem> itemsList) {

        this.itemsList = itemsList;
	    this.context=context;
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
    public NewsItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		if(inflater==null && context!=null)
			this.inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.news_item_card, parent, false);

        NewsItemRecyclerViewHolder vh = new NewsItemRecyclerViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(NewsItemRecyclerViewHolder holder, int position)
    {
        final NewsItem currentItem = itemsList.get(position);
		Logger.instance().v("News-Card", "Image: "+currentItem.getImageUrl(), false);
	    if(!Utilities.isNullString(currentItem.getImageUrl()))
	    {
            Glide.with(context).load(currentItem.getImageUrl())
                    .placeholder(R.drawable.news_image_place_holder)
//                    .signature(new StringSignature(user.userPhotoID != null ? user.userPhotoID : ""))
                    .centerCrop()
                    .into(holder.imgv_newImg);
        }
	    else
		    holder.imgv_newImg.setImageResource(R.drawable.news_image_place_holder);

        holder.txtv_newTitle.setText(currentItem.Title);
        holder.txtv_newDescription.setText(currentItem.NewsBrief);
//	    String newdateStr = MainActivity.reFormatDate(currentItem.CreationDate, MainActivity.sdf_Date);
        holder.txtv_newTime.setText(currentItem.CreationDate);
    }

    @Override
    public int getItemCount() {
	    if(itemsList!=null)
	        return itemsList.size();
	    return 0;
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


}
