package com.secb.android.view.components.recycler_organizers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.OrganizerItem;

import java.util.Collections;
import java.util.List;

public class OrganizerItemRecyclerAdapter extends RecyclerView.Adapter<OrganizerItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<OrganizerItem>itemsList = Collections.emptyList();
    Context context;

    public OrganizerItemRecyclerAdapter(Context context, List<OrganizerItem> itemsList) {
        this.inflater = LayoutInflater.from(context);
        this.itemsList = itemsList;
        this.context=context;
    }

    @Override
    public OrganizerItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.organizer_item_card, parent, false);

        OrganizerItemRecyclerViewHolder vh = new OrganizerItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(OrganizerItemRecyclerViewHolder holder, int position) {
        OrganizerItem currentItem = itemsList.get(position);
        holder.imgv_organizerImg.setImageBitmap(currentItem.OraganizerItemImage);
        holder.txtv_organizerTitle.setText(currentItem.OraganizerItemTitle);
        holder.txtv_organizerDescription.setText(currentItem.OraganizerItemDescription);
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
