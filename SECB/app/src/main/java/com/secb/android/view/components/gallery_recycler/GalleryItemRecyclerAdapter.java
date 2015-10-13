package com.secb.android.view.components.gallery_recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.GalleryItem;

import java.util.Collections;
import java.util.List;

public class GalleryItemRecyclerAdapter extends RecyclerView.Adapter<GalleryItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<GalleryItem>itemsList = Collections.emptyList();
    Context context;

    public GalleryItemRecyclerAdapter(Context context, List<GalleryItem> itemsList) {
        this.inflater = LayoutInflater.from(context);
        this.itemsList = itemsList;
        this.context=context;
    }

    @Override
    public GalleryItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gallary_item, parent, false);

        GalleryItemRecyclerViewHolder vh = new GalleryItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(GalleryItemRecyclerViewHolder holder, int position) {
        GalleryItem currentItem = itemsList.get(position);
        if (currentItem.galleryItemType ==  GalleryItem.GALLERY_TYPE_IMAGE_ALBUM ||
                currentItem.galleryItemType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            holder.txtv_galleryTitle.setVisibility(View.GONE);
        }
        else
        {
            holder.txtv_galleryTitle.setVisibility(View.VISIBLE);
        }

        if (currentItem.galleryItemType== GalleryItem.GALLERY_TYPE_VIDEO_ALBUM){
            holder.imgv_galleryPlayVideoIcon.setVisibility(View.VISIBLE);
        }
        else{
            holder.imgv_galleryPlayVideoIcon.setVisibility(View.GONE);
        }

        holder.imgv_galleryImg.setImageResource(currentItem.imgResource);
        holder.txtv_galleryTitle.setText(currentItem.galleryItemTitle);
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
