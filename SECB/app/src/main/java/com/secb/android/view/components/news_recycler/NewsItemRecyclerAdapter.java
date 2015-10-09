package com.secb.android.view.components.news_recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.NewsItem;

import java.util.Collections;
import java.util.List;

public class NewsItemRecyclerAdapter extends RecyclerView.Adapter<NewsItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<NewsItem>itemsList = Collections.emptyList();
    Context context;

    public NewsItemRecyclerAdapter(Context context, List<NewsItem> itemsList) {
        this.inflater = LayoutInflater.from(context);
        this.itemsList = itemsList;
        this.context=context;
    }

    @Override
    public NewsItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_item_card, parent, false);

        NewsItemRecyclerViewHolder vh = new NewsItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(NewsItemRecyclerViewHolder holder, int position) {
        NewsItem currentItem = itemsList.get(position);
        holder.imgv_newImg.setImageBitmap(currentItem.newsItemImage);
        holder.txtv_newTitle.setText(currentItem.newsItemTitle);
        holder.txtv_newDescription.setText(currentItem.newsItemDescription);
        holder.txtv_newTime.setText(currentItem.newsItemDate);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


}
