package com.secb.android.view.components.recycler_organizers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.components.FooterLoaderAdapter;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrganizerItemRecyclerAdapter extends FooterLoaderAdapter<OrganizerItemRecyclerViewHolder> {
    LayoutInflater inflater;
    List<OrganizerItem> itemsList = Collections.emptyList();
    Context context;
	private ArrayList<Integer> selectedIndices;
	private ArrayList<View>selectedViews;

    public OrganizerItemRecyclerAdapter(Context context, List<OrganizerItem> itemsList) {
        super(context);
        this.context = context;
	    selectedIndices = new ArrayList<>();
	    selectedViews = new ArrayList<>();

	    if (context == null)
            this.context = CTApplication.getContext();

        setItemsList(itemsList);
        this.inflater = LayoutInflater.from(context);
    }

	public void setItemsList(List<OrganizerItem> itemsList) {
        setItems(itemsList);
        this.itemsList = itemsList;
    }

    @Override
    public long getYourItemId(int position) {
        return (itemsList.get(position).OrganizerName + "_").hashCode();
    }

    @Override
    public OrganizerItemRecyclerViewHolder getYourItemViewHolder(ViewGroup parent) {

        if (inflater == null && context != null)
            this.inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.organizer_item_card, parent, false);

        OrganizerItemRecyclerViewHolder vh = new OrganizerItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder _holder, int position) {
        OrganizerItemRecyclerViewHolder holder = (OrganizerItemRecyclerViewHolder) _holder;
        OrganizerItem currentItem = itemsList.get(position);
        if (!Utilities.isNullString(currentItem.OrganizerImage)) {
            Glide.with(context)
                    .load(currentItem.OrganizerImage)
                    .centerCrop()
                    .placeholder(R.drawable.organizer_place_holder)
                    .into(holder.imgv_organizerImg);
        } else
            holder.imgv_organizerImg.setImageResource(R.drawable.organizer_place_holder);

        holder.txtv_organizerTitle.setText(currentItem.OrganizerName);
        holder.txtv_organizerDescription.setText(currentItem.OrganizerDescription);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void deleteItem(int position) {
        itemsList.remove(position);
        notifyItemRemoved(position);
    }

	public void setItemSelected(View v,int index) {
		setItemSelected(selectedIndices,selectedViews,context,v,index);
	}

}
