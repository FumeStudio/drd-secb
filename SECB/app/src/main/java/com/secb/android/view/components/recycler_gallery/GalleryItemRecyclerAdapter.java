package com.secb.android.view.components.recycler_gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secb.android.R;
import com.secb.android.model.GalleryItem;
import com.squareup.picasso.Picasso;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class GalleryItemRecyclerAdapter extends RecyclerView.Adapter<GalleryItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<GalleryItem>itemsList = Collections.emptyList();
    Context context;

    public GalleryItemRecyclerAdapter(Context context, List<GalleryItem> itemsList) {
	    this.context=context;
	    if(context == null)
		    this.context= CTApplication.getContext();


	    this.itemsList = itemsList;
	    this.inflater = LayoutInflater.from(this.context);
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
        int currentItemType = currentItem.galleryItemType;

    //handle views (play icon , album title) visibility
        handleViewsVisibility(currentItemType, holder);


    //bind data
        bindViews(currentItem, holder);

    }

    private void handleViewsVisibility(int currentItemType, GalleryItemRecyclerViewHolder holder)
    {
        //handle title visibility
        if (currentItemType  ==  GalleryItem.GALLERY_TYPE_IMAGE_ALBUM ||
                currentItemType  == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            holder.txtv_galleryTitle.setVisibility(View.GONE);
        }
        else
        {
            holder.txtv_galleryTitle.setVisibility(View.VISIBLE);
        }

        //handle play icon visibility
        if (currentItemType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            holder.imgv_galleryPlayVideoIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgv_galleryPlayVideoIcon.setVisibility(View.GONE);
        }

    }

    private void bindViews(GalleryItem currentItem, GalleryItemRecyclerViewHolder holder) {
        int currentItemType = currentItem.galleryItemType;
    /*1-Image Gallery*/
        if (currentItemType == GalleryItem.GALLERY_TYPE_IMAGE_GALLERY)
        {
            if(!Utilities.isNullString(currentItem.PhotoGalleryAlbumThumbnail))
            {
                Picasso.with(context)
                        .load(currentItem.PhotoGalleryAlbumThumbnail)
                        .placeholder(R.drawable.image_place_holder)
                        .error(R.drawable.image_place_holder_failed)
                        /*.resize(100,50)*/
                        .into(holder.imgv_galleryImg);
            }
            else
                holder.imgv_galleryImg.setImageResource(R.drawable.image_place_holder);
        }

    /*2-Video Gallery*/
        else if (currentItemType == GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
        {
            if(!Utilities.isNullString(currentItem.VideosGalleryAlbumThumbnail))
            {
                Picasso.with(context)
                        .load(currentItem.VideosGalleryAlbumThumbnail)
                        .placeholder(R.drawable.video_place_holder)
                        .error(R.drawable.video_place_holder_failed)
                        .into(holder.imgv_galleryImg);
            }
            else
                holder.imgv_galleryImg.setImageResource(R.drawable.video_place_holder);
        }

    /*3-Image Album*/
        else if (currentItemType == GalleryItem.GALLERY_TYPE_IMAGE_ALBUM)
        {
            if(!Utilities.isNullString(currentItem.PhotoGalleryImageUrl))
            {
                Picasso.with(context)
                        .load(currentItem.PhotoGalleryImageUrl)
                        .placeholder(R.drawable.image_place_holder)
                        .error(R.drawable.image_place_holder_failed)
                        .into(holder.imgv_galleryImg);
            }
            else
                holder.imgv_galleryImg.setImageResource(R.drawable.video_place_holder);
        }


    /*4-Video Album*/
        else if (currentItemType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            if(!Utilities.isNullString(currentItem.VideosGalleryAlbumThumbnail))
            {
                Picasso.with(context)
                        .load(currentItem.VideosGalleryAlbumThumbnail)
                        .placeholder(R.drawable.video_place_holder)
                        .error(R.drawable.video_place_holder_failed)
                        .into(holder.imgv_galleryImg);
            }
            else
                holder.imgv_galleryImg.setImageResource(R.drawable.video_place_holder);
        }


        if(!Utilities.isNullString(currentItem.Title))
            holder.txtv_galleryTitle.setText(currentItem.Title);
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
