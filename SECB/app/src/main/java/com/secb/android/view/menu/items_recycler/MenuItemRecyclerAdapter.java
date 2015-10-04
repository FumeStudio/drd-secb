package com.secb.android.view.menu.items_recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;

import java.util.Collections;
import java.util.List;

public class MenuItemRecyclerAdapter extends RecyclerView.Adapter<MenuItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<MenuItemObject>itemsList = Collections.emptyList();
    Context context;

    public MenuItemRecyclerAdapter(Context context, List<MenuItemObject> itemsList) {
        this.inflater = LayoutInflater.from(context);
        this.itemsList = itemsList;
        this.context=context;
    }

    @Override
    public MenuItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_item_view, parent, false);

        MenuItemRecyclerViewHolder vh = new MenuItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MenuItemRecyclerViewHolder holder, int position) {
       MenuItemObject currentItem = itemsList.get(position);
       holder.itemText.setText(currentItem.name);
       holder.itemIcon.setImageResource(currentItem.imgId);
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
