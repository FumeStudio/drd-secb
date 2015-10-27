package com.secb.android.view.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.manager.GalleryManager;
import com.secb.android.model.GalleryItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
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
    RecyclerView galleryRecyclerView;
    GridLayoutManager layoutManager;
    GalleryItemRecyclerAdapter galleryItemRecyclerAdapter;
    public int galleryType;
    View view;
    private List galleryItemList;
    private List photoGalleryItemList;
    private List videoGalleryItemList;
    TextView txtv_noData;
	private ProgressDialog progressDialog;


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
	    ((MainActivity)getActivity()).setGalleryRequstObserver(this);
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
	    if(txtv_noData!=null)
	    {
		    UiEngine.applyCustomFont(txtv_noData, UiEngine.Fonts.HVAR);
	    }
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
	    progressDialog =  CustomProgressDialog.getInstance(getActivity(),true);
	    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		    @Override
		    public void onCancel(DialogInterface dialog) {
			    bindViews();
		    }
	    });
        galleryRecyclerView = (RecyclerView) view.findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(),2);
        galleryRecyclerView.setLayoutManager(layoutManager);
        txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), galleryRecyclerView, this));
    }

    private void bindViews()
    {
	    if (galleryType==GalleryItem.GALLERY_TYPE_IMAGE_GALLERY){
		    galleryItemList=photoGalleryItemList;
	    }
	    else if (galleryType==GalleryItem.GALLERY_TYPE_VIDEO_GALLERY){
		    galleryItemList=videoGalleryItemList;
	    }

        if(this.galleryItemList !=null && this.galleryItemList.size()>0)
        {
            galleryRecyclerView.setVisibility(View.VISIBLE);
            txtv_noData.setVisibility(View.GONE);
            galleryItemRecyclerAdapter = new GalleryItemRecyclerAdapter(getActivity(), this.galleryItemList);
            galleryRecyclerView.setAdapter(galleryItemRecyclerAdapter);
        }
        else {
	        txtv_noData.setText(getString(R.string.gallery_no_albums));
            galleryRecyclerView.setVisibility(View.GONE);
            txtv_noData.setVisibility(View.VISIBLE);
        }
    }

    private void getData()
    {
	    //if gallery list is loaded in the manager get it and bind
	    //if not and the main activity is still loading the news list
	        // wait for it and it will notify handleRequestFinished in this fragment.
	    //if the main activity finished loading gallery list and the manager is still empty
	        //start operation here.

        //get PhotoGallery
        if(galleryType== GalleryItem.GALLERY_TYPE_IMAGE_GALLERY )
        {
            photoGalleryItemList = GalleryManager.getInstance().getImageGalleryList(getActivity());

	        if(photoGalleryItemList != null && photoGalleryItemList.size()>0){
		        handleRequestFinished(RequestIds.PHOTO_GALLERY_REQUEST_ID , null,photoGalleryItemList);
	        }
	        else
	        {
		        if (((MainActivity) getActivity()).isPhotoGalleryLoadingFinished== false) {
			        startWaiting();
		        }
		        else{
			        startGalleryListOperation(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY, false);
		        }
	        }
        }

        //get VideoGallery
        else if(galleryType== GalleryItem.GALLERY_TYPE_VIDEO_GALLERY )
        {
	        videoGalleryItemList = GalleryManager.getInstance().getVideoGalleryList(getActivity());
	        if(videoGalleryItemList != null && videoGalleryItemList.size()>0){
		        handleRequestFinished(RequestIds.VIDEO_GALLERY_REQUEST_ID , null,videoGalleryItemList);
	        }
	        else
	        {
		        if (((MainActivity) getActivity()).isVideoGalleryLoadingFinished == false) {
			        startWaiting();
		        }
		        else{
			        startGalleryListOperation(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY, false);
		        }
	        }
        }
    }

	private void startGalleryListOperation(int galleryTypeVideoGallery, boolean showDialog)
	{
		GalleryOperation operation=null ;
		if(galleryTypeVideoGallery ==GalleryItem.GALLERY_TYPE_IMAGE_GALLERY)
			operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_IMAGE_GALLERY,RequestIds.PHOTO_GALLERY_REQUEST_ID, showDialog,getActivity(), 100,0);

		else if(galleryTypeVideoGallery ==GalleryItem.GALLERY_TYPE_VIDEO_GALLERY)
			operation = new GalleryOperation(GalleryItem.GALLERY_TYPE_VIDEO_GALLERY,RequestIds.VIDEO_GALLERY_REQUEST_ID, showDialog,getActivity(), 100,0);

		if(operation!=null)
		{
			operation.addRequsetObserver(this);
			operation.execute();
		}
	}

	private void startWaiting() {
		if(progressDialog!=null&& !progressDialog.isShowing())
		{
			progressDialog.show();
		}
	}

	private void stopWaiting() {
		if(progressDialog!=null&& progressDialog.isShowing())
		{
			progressDialog.dismiss();
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
	    stopWaiting();
	    if (error == null)
        {
            Logger.instance().v(TAG,"Success \n\t\t"+resultObject);

            //photoGallery
            if((int)requestId == RequestIds.PHOTO_GALLERY_REQUEST_ID && resultObject!=null)
            {
                photoGalleryItemList = (List) resultObject;
                for(Object iterator : photoGalleryItemList){
                    ((GalleryItem)iterator).galleryItemType= GalleryItem.GALLERY_TYPE_IMAGE_GALLERY;
                }
                bindViews();

            }
            //videoGallery
            else if((int)requestId == RequestIds.VIDEO_GALLERY_REQUEST_ID && resultObject!=null)
            {
                videoGalleryItemList = (List) resultObject;
                for(Object iterator : videoGalleryItemList){
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
	        bindViews();
        }
    }

    @Override
    public void requestCanceled(Integer requestId, Throwable error) {

    }

    @Override
    public void updateStatus(Integer requestId, String statusMsg) {

    }
}
