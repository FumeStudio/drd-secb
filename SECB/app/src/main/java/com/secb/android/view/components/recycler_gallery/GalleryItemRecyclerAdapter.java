package com.secb.android.view.components.recycler_gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.secb.android.R;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.OrganizerItem;
import com.secb.android.view.components.FooterLoaderAdapter;
import com.secb.android.view.components.recycler_organizers.OrganizerItemRecyclerViewHolder;

import net.comptoirs.android.common.helper.Utilities;
import net.comptoirs.android.common.view.CTApplication;

import java.util.Collections;
import java.util.List;

public class GalleryItemRecyclerAdapter extends FooterLoaderAdapter<GalleryItemRecyclerViewHolder>
{
    LayoutInflater inflater ;
    List<GalleryItem>itemsList = Collections.emptyList();
    Context context;
	private View view;

	public GalleryItemRecyclerAdapter(Context context, List<GalleryItem> itemsList) {
        super(context);
        this.context=context;
	    if(context == null)
		    this.context= CTApplication.getContext();

        setItemsList(itemsList);
	    this.inflater = LayoutInflater.from(this.context);
    }
    public void setItemsList(List<GalleryItem> itemsList) {
        setItems(itemsList);
        this.itemsList = itemsList;
    }
    @Override
    public long getYourItemId(int position) {
        return (itemsList.get(position).Id).hashCode();
    }

    @Override
    public GalleryItemRecyclerViewHolder getYourItemViewHolder(ViewGroup parent) {
        view = inflater.inflate(R.layout.gallary_item, parent, false);

        GalleryItemRecyclerViewHolder vh = new GalleryItemRecyclerViewHolder(view);
        return vh;
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder _holder, int position)
    {
        GalleryItemRecyclerViewHolder holder = (GalleryItemRecyclerViewHolder) _holder;

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
//            Logger.instance().v("Image", "gallery Album: "+currentItem.PhotoGalleryAlbumThumbnail + " -- "+currentItem.PhotoGalleryImageUrl, false);
            if(!Utilities.isNullString(currentItem.getPhotoGalleryImageThumbnail()))
            {
                Glide.with(context)
                        .load(currentItem.getPhotoGalleryImageThumbnail())
                        .placeholder(R.drawable.image_place_holder)
                        .centerCrop()
                        .into(holder.imgv_galleryImg);
            }
            else
                holder.imgv_galleryImg.setImageResource(R.drawable.image_place_holder);
        }

    /*2-Video Gallery*/
        else if (currentItemType == GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
        {
            if(!Utilities.isNullString(currentItem.getVideoGalleryImageThumbnail()))
            {
                Glide.with(context)
                        .load(currentItem.getVideoGalleryImageThumbnail())
                        .placeholder(R.drawable.video_place_holder)
                        .centerCrop()
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
                Glide.with(context)
                        .load(currentItem.PhotoGalleryImageUrl)
                        .placeholder(R.drawable.image_place_holder)
                        .centerCrop()
                        .into(holder.imgv_galleryImg);
            }
            else
                holder.imgv_galleryImg.setImageResource(R.drawable.video_place_holder);
        }


    /*4-Video Album*/
        else if (currentItemType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            if(!Utilities.isNullString(currentItem.VideoGalleryUrl))
            {
                Glide.with(context)
                        .load(currentItem.VideoGalleryUrl)
                        .placeholder(R.drawable.video_place_holder)
                        .centerCrop()
                        .into(holder.imgv_galleryImg);
            }
            else
                holder.imgv_galleryImg.setImageResource(R.drawable.video_place_holder);
        }


        if(!Utilities.isNullString(currentItem.Title))
            holder.txtv_galleryTitle.setText(currentItem.Title);
    }

    public int getItemCount() {
        return super.getItemCount();
    }

    public void deleteItem(int position){
        itemsList.remove(position);
        notifyItemRemoved(position);
    }


}
