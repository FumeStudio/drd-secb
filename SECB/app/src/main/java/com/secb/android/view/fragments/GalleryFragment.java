package com.secb.android.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.secb.android.R;
import com.secb.android.controller.backend.PhotoGalleryOperation;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.PhotoGallery;
import com.secb.android.model.User;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.recycler_gallery.GalleryItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener , RecyclerCustomClickListener, RequestObserver

{
    private static final int PHOTO_GALLERY_REQUEST_ID = 1;
    private static final String TAG = "GalleryFragment";
    RecyclerView galleryRecyclerView;
    GridLayoutManager layoutManager;
    GalleryItemRecyclerAdapter galleryItemRecyclerAdapter;
    ArrayList<GalleryItem> galleryItemsList;
    public int galleryType;
    int galleryId;
    View view;


    public static GalleryFragment newInstance(int galleryType , int galleryId)
    {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("galleryType", galleryType);
        bundle.putInt("galleryId", galleryId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);

        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null)
        {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null)
            {
                ((ViewGroup) oldParent).removeView(view);
            }
        }
        else
        {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.gallery_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            galleryType = bundle.getInt("galleryType");
            galleryId = bundle.getInt("galleryId");
        }
        setHeaderTitle();
        initViews(view);
        return view;
    }

    private void setHeaderTitle() {
        if(galleryType==GalleryItem.GALLERY_TYPE_IMAGE_ALBUM)
        {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.image_album));
        }
        else if(galleryType==GalleryItem.GALLERY_TYPE_IMAGE_GALLERY)
        {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.image_gallery));
        }
        else if(galleryType==GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.video_album));
        }
        else if(galleryType==GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
        {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.video_gallery));
        }
    }


    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts() {
        // TODO::
//		UiEngine.applyCustomFont(((TextView) view.findViewById(R.id.textViewAbout)), UiEngine.Fonts.HELVETICA_NEUE_LT_STD_CN);
    }

    private void goBack() {
        ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(getClass().getName());
    }

    // ////////////////////////////////////////////////////////////

    @Override
    public void onBack() {
        goBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }




    private void initViews(View view)
    {

        getData();

        galleryRecyclerView = (RecyclerView) view.findViewById(R.id.galleryRecyclerView);
        galleryItemRecyclerAdapter = new GalleryItemRecyclerAdapter(getActivity(), galleryItemsList);
        galleryRecyclerView.setAdapter(galleryItemRecyclerAdapter);
        galleryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(),2);
        galleryRecyclerView.setLayoutManager(layoutManager);
//        galleryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
//        galleryRecyclerView.addItemDecoration(new GridDividerDecoration(getActivity()));
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), galleryRecyclerView, this));
    }

    private void getData()
    {

        if(galleryType==GalleryItem.GALLERY_TYPE_IMAGE_GALLERY ||galleryType==GalleryItem.GALLERY_TYPE_VIDEO_GALLERY )
        {
//            galleryItemsList = DevData.getGalleryList(galleryType);

            PhotoGalleryOperation operation = new PhotoGalleryOperation(PHOTO_GALLERY_REQUEST_ID, true,getActivity(), 100,0);
            operation.addRequsetObserver(this);
            operation.execute();
        }
    }

    @Override
    public void onItemClicked(View v, int position)
    {
        GalleryItem clickedItem = galleryItemsList.get(position);
        if(clickedItem.galleryItemType==GalleryItem.GALLERY_TYPE_IMAGE_GALLERY ||clickedItem.galleryItemType==GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
        {
            ((MainActivity) getActivity()).openAlbumFragment(clickedItem.galleryItemType+1, clickedItem.imgResource);
        }
    }

    @Override
    public void onItemLongClicked(View v, int position)
    {

    }

    @Override
    public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
        if (error == null)
        {
            Logger.instance().v(TAG,"Success \n\t\t"+resultObject);
            if((int)requestId == PHOTO_GALLERY_REQUEST_ID && resultObject!=null &&
                    !Utilities.isNullString(((User) resultObject).loginCookie))
            {
                List<PhotoGallery>photoAlbums = (List<PhotoGallery>) resultObject;
                Logger.instance().v(TAG,"photoAlbums size = "+photoAlbums.size());
                Logger.instance().v(TAG,"photoAlbums.get(0)= "+photoAlbums.get(0).PhotoGalleryImageUrl);
                Logger.instance().v(TAG,"photoAlbums.get(0)= "+photoAlbums.get(0).Title);

            }
        }
        else if (error != null && error instanceof CTHttpError) {
            Logger.instance().v(TAG,error);
        }
    }

    @Override
    public void requestCanceled(Integer requestId, Throwable error) {

    }

    @Override
    public void updateStatus(Integer requestId, String statusMsg) {

    }
}
