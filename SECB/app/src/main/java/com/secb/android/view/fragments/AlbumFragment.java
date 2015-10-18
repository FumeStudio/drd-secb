package com.secb.android.view.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.secb.android.R;
import com.secb.android.controller.manager.DevData;
import com.secb.android.model.GalleryItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
import com.secb.android.view.components.recycler_gallery.GalleryItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import java.util.ArrayList;

public class AlbumFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener, RecyclerCustomClickListener

{
    RecyclerView galleryRecyclerView;
    GridLayoutManager layoutManager;
    GalleryItemRecyclerAdapter galleryItemRecyclerAdapter;
    ArrayList<GalleryItem> galleryItemsList;
    int galleryType;
    int galleryId;
    View view;

    FrameLayout layout_videoPlayerContainer;
    LinearLayout layout_imagePlayerContainer;
    VideoView vidv_videoAlbum ;
    ImageView imgv_imageAlbum;
    private MediaController mediaController;
    private CustomProgressDialog dialog;

    public static AlbumFragment newInstance(int galleryType, int galleryId) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("galleryType", galleryType);
        bundle.putInt("galleryId", galleryId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
        ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
        hidePlayers();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewParent oldParent = (ViewParent) view.getRootView();
            if (oldParent != container && oldParent != null) {
                ((ViewGroup) oldParent).removeView(view);
            }
        } else {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.album_list_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            galleryType = bundle.getInt("galleryType");
            galleryId = bundle.getInt("galleryId");
        }
        setHeaderTitle();
        initViews(view);
        return view;
    }

    private void setHeaderTitle() {
        if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_ALBUM) {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.image_album));
        } else if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_GALLERY) {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.image_gallery));
        } else if (galleryType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM) {
            ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.video_album));
        } else if (galleryType == GalleryItem.GALLERY_TYPE_VIDEO_GALLERY) {
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
            case R.id.imageViewBackHeader:
                onBack();
                break;
//            case R.id.layout_videoPlayerContainer:
            case R.id.layout_imagePlayerContainer:
                hidePlayers();
                break;
            default:
                break;
        }
    }


    private void initViews(View view) {

        getData();

        galleryRecyclerView = (RecyclerView) view.findViewById(R.id.galleryRecyclerView);
        galleryItemRecyclerAdapter = new GalleryItemRecyclerAdapter(getActivity(), galleryItemsList);
        galleryRecyclerView.setAdapter(galleryItemRecyclerAdapter);
        galleryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        galleryRecyclerView.setLayoutManager(layoutManager);

//        layout_videoPlayerContainer = (FrameLayout) view.findViewById(R.id.layout_videoPlayerContainer);
        layout_imagePlayerContainer = (LinearLayout) view.findViewById(R.id.layout_imagePlayerContainer);

//        layout_videoPlayerContainer.setOnClickListener(this);
        layout_imagePlayerContainer.setOnClickListener(this);

//        vidv_videoAlbum = (VideoView) view.findViewById(R.id.vidv_videoAlbum);
        imgv_imageAlbum = (ImageView) view.findViewById(R.id.imgv_imageAlbum);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), galleryRecyclerView, this));



    }

    private void getData() {

        if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_GALLERY || galleryType == GalleryItem.GALLERY_TYPE_VIDEO_GALLERY) {
            galleryItemsList = DevData.getGalleryList(galleryType);
        } else {
            galleryItemsList = DevData.getAlbumList(galleryType, galleryId);
        }
    }

    @Override
    public void onItemClicked(View v, int position) {
        GalleryItem clickedItem = galleryItemsList.get(position);
        if (clickedItem.galleryItemType == GalleryItem.GALLERY_TYPE_IMAGE_GALLERY || clickedItem.galleryItemType == GalleryItem.GALLERY_TYPE_VIDEO_GALLERY) {
            ((MainActivity) getActivity()).openGalleryFragment(clickedItem.galleryItemType + 1, clickedItem.imgResource);
        }
        //play video
        else if (galleryType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM) {
//            ((MainActivity) getActivity()).openPlayerFragment("");
            clickedItem.videoUrl = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
//            clickedItem.videoUrl = "https://www.youtube.com/watch?v=weGC-A551B4";
//            prepareVideo(clickedItem.videoUrl);
            playVideo(clickedItem.videoUrl);
        }
        //display image
        else if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_ALBUM) {
            playImage(clickedItem.imgResource);
        }
    }


    private void hidePlayers() {
//        if(layout_videoPlayerContainer!=null)
//        {
//            layout_videoPlayerContainer.setVisibility(View.GONE);
//            vidv_videoAlbum.stopPlayback();
//
//        }

        if(layout_imagePlayerContainer !=null)
        {
            layout_imagePlayerContainer.setVisibility(View.GONE);
            imgv_imageAlbum.setImageBitmap(null);
        }

    }
    /*public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }*/
    public void prepareVideo(String videoUrl)
    {
        layout_videoPlayerContainer.setVisibility(View.VISIBLE);
        if(mediaController ==null)
        {
            mediaController = new MediaController(getActivity());
            mediaController.setAnchorView(vidv_videoAlbum);
            vidv_videoAlbum.setMediaController(mediaController);

        }
        vidv_videoAlbum.setVideoURI(Uri.parse(videoUrl));
        if(dialog ==null)
            dialog =CustomProgressDialog.getInstance(getActivity(),false);

        dialog.show();

        vidv_videoAlbum.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {

                // close the progress bar and play the video
                dialog.dismiss();
                vidv_videoAlbum.seekTo(0);
                vidv_videoAlbum.start();
                /*if (position == 0) {
                    vidv_videoAlbum.start();
                } else {
                    vidv_videoAlbum.pause();
                }*/
            }
        });
        vidv_videoAlbum.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                dialog.dismiss();
                hidePlayers();
                return false;

            }
        });
        vidv_videoAlbum.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ((MainActivity)getActivity()).displayToast("stopped");
            }
        });
    }
    private void playVideo(String videoUrlvideoUrl) {
        /*if(vidv_videoAlbum!=null &&layout_videoPlayerContainer !=null)
        {
            layout_videoPlayerContainer.setVisibility(View.VISIBLE);
            vidv_videoAlbum.setVideoURI(Uri.parse(videoUrl));
            vidv_videoAlbum.start();
        }*/

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoUrlvideoUrl), "video/*");
        Intent choserIntent = Intent.createChooser(intent, "");
        if (choserIntent!=null)
            startActivity(choserIntent);
        else
            ((MainActivity)getActivity()).displayToast("can't play this video file ");
    }

    private void playImage(int imgResource)
    {
        if(imgv_imageAlbum!=null &&layout_imagePlayerContainer !=null)
        {
            imgv_imageAlbum.setImageResource(imgResource);
            layout_imagePlayerContainer.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemLongClicked(View v, int position) {

    }
}
