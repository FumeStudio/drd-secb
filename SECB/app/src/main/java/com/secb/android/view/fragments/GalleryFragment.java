package com.secb.android.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.secb.android.R;
import com.secb.android.controller.backend.GalleryOperation;
import com.secb.android.controller.manager.GalleryManager;
import com.secb.android.model.GalleryItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.recycler_gallery.GalleryItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;

import java.util.List;

public class GalleryFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener , RecyclerCustomClickListener, RequestObserver

{
    private static final String TAG = "GalleryFragment";
    private static final int PHOTO_GALLERY_REQUEST_ID = 1;
    private static final int VIDEO_GALLERY_REQUEST_ID = 2;
    RecyclerView galleryRecyclerView;
    GridLayoutManager layoutManager;
    GalleryItemRecyclerAdapter galleryItemRecyclerAdapter;
    public int galleryType;
    View view;
    private List galleryItemList;
    TextView txtv_noData;


    public static GalleryFragment newInstance(int galleryType , int galleryId)
    {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("galleryType", galleryType);
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
        }
        setHeaderTitle();
        initViews(view);
        getData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        galleryRecyclerView.setAdapter(null);
    }

    private void setHeaderTitle() {
        if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_ALBUM)
        {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.image_album));
        }
        else if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_GALLERY)
        {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.image_gallery));
        }
        else if(galleryType== GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
        {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.video_album));
        }
        else if(galleryType== GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
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
        galleryRecyclerView = (RecyclerView) view.findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(),2);
        galleryRecyclerView.setLayoutManager(layoutManager);
        txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), galleryRecyclerView, this));
    }

    private void bindViews(){
        if(galleryItemList.size()>0)
        {
            galleryRecyclerView.setVisibility(View.VISIBLE);
            txtv_noData.setVisibility(View.GONE);
            galleryItemRecyclerAdapter = new GalleryItemRecyclerAdapter(getActivity(), galleryItemList);
            galleryRecyclerView.setAdapter(galleryItemRecyclerAdapter);
        }
        else {

            galleryRecyclerView.setVisibility(View.GONE);
            txtv_noData.setVisibility(View.VISIBLE);
        }
    }

    private void getData()
    {
        //checked whether list is existing in GalleryManager or not
        //if it exists get it from the manager
        //if it's not exist get it from server

        //get PhotoGallery
        if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_GALLERY )
        {
            galleryItemList = GalleryManager.getInstance().getImageGalleryList(getActivity());
            if(galleryItemList == null || galleryItemList.size()==0)
            {
                GalleryOperation operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY,PHOTO_GALLERY_REQUEST_ID, true,getActivity(), 100,0);
                operation.addRequsetObserver(this);
                operation.execute();
            }
            else{
                handleRequestFinished(PHOTO_GALLERY_REQUEST_ID , null,galleryItemList);
            }
        }

        //get VideoGallery
        else if(galleryType== GalleryItem.GALLERY_TYPE_VIDEO_GALLERY )
        {
            galleryItemList = GalleryManager.getInstance().getVideoGalleryList();
            if(galleryItemList == null || galleryItemList.size()==0)
            {
                GalleryOperation operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY,VIDEO_GALLERY_REQUEST_ID, true,getActivity(), 100,0);
                operation.addRequsetObserver(this);
                operation.execute();
            }
            else{
                handleRequestFinished(VIDEO_GALLERY_REQUEST_ID , null,galleryItemList);
            }
        }
    }

    @Override
    public void onItemClicked(View v, int position)
    {
        GalleryItem clickedItem = (GalleryItem) galleryItemList.get(position);
        if(clickedItem.galleryItemType== GalleryItem.GALLERY_TYPE_IMAGE_GALLERY ||clickedItem.galleryItemType== GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
        {
            ((MainActivity) getActivity()).openAlbumFragment(clickedItem.galleryItemType+1, clickedItem.FolderPath , clickedItem.Id);
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

            //photoGallery
            if((int)requestId == PHOTO_GALLERY_REQUEST_ID && resultObject!=null)
            {
                galleryItemList = (List) resultObject;
                for(Object iterator : galleryItemList){
                    ((GalleryItem)iterator).galleryItemType= GalleryItem.GALLERY_TYPE_IMAGE_GALLERY;
                }
                bindViews();

            }
            //videoGallery
            else if((int)requestId == VIDEO_GALLERY_REQUEST_ID && resultObject!=null)
            {
                galleryItemList = (List) resultObject;
                for(Object iterator : galleryItemList){
                    ((GalleryItem)iterator).galleryItemType= GalleryItem.GALLERY_TYPE_VIDEO_GALLERY;
                }
                bindViews();

            }
        }
        else if (error != null && error instanceof CTHttpError)
        {
            Logger.instance().v(TAG,error);
            int statusCode = ((CTHttpError) error).getStatusCode();
            String errorMsg = ((CTHttpError) error).getErrorMsg();
            if (RequestHandler.isRequestTimedOut(statusCode))
            {
                ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
            }
            else if (statusCode == -1)
            {
                ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
                        getActivity());
            }
        }
    }

    @Override
    public void requestCanceled(Integer requestId, Throwable error) {

    }

    @Override
    public void updateStatus(Integer requestId, String statusMsg) {

    }
}
