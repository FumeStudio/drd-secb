package com.secb.android.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.secb.android.R;
import com.secb.android.controller.backend.AlbumOperation;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.controller.backend.ServerKeys;
import com.secb.android.controller.manager.GalleryManager;
import com.secb.android.controller.manager.PagingManager;
import com.secb.android.model.GalleryItem;
import com.secb.android.model.OrganizersFilterData;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.MainActivity;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.UiEngine;
import com.secb.android.view.components.RecyclerViewScrollListener;
import com.secb.android.view.components.dialogs.CustomProgressDialog;
import com.secb.android.view.components.recycler_gallery.GalleryItemRecyclerAdapter;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomClickListener;
import com.secb.android.view.components.recycler_item_click_handlers.RecyclerCustomItemTouchListener;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener, RecyclerCustomClickListener, RequestObserver

{
    private static final String TAG = "AlbumFragment";
    RecyclerView galleryRecyclerView;
    GridLayoutManager layoutManager;
    GalleryItemRecyclerAdapter galleryItemRecyclerAdapter;
    int galleryType;
    String folderPath;
    View view;
    private List<GalleryItem> galleryItemList;

    LinearLayout layout_videoPlayerContainer;
    LinearLayout layout_imagePlayerContainer;
    VideoView vidv_videoAlbum;
    ImageView imgv_imageAlbum;
    TextView txtv_noData;
    private MediaController mediaController;
    private ProgressDialog dialog;
    private String albumId;

    //youtube components
    YouTubePlayerSupportFragment youTubePlayerSupportFragment;
    public static final String YOUTUBE_API_KEY = "AIzaSyBYpLwG4bwNDTpqX5uzAJLFvXfXiE9BW-U";
    public YouTubePlayer youTubePlayer;
    private boolean isPlayerReadey;


    public static AlbumFragment newInstance(int galleryType, String folderPath, String albumId) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("galleryType", galleryType);
        bundle.putString("folderPath", folderPath);
        bundle.putString("albumId", albumId);
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
            folderPath = bundle.getString("folderPath");
            albumId = bundle.getString("albumId");
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
        if (txtv_noData != null) {
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
            case R.id.imageViewBackHeader:
                onBack();
                break;
            case R.id.layout_videoPlayerContainer:
            case R.id.layout_imagePlayerContainer:
                hidePlayers();
                break;
            default:
                break;
        }
    }


    private void initViews(View view) {

        galleryRecyclerView = (RecyclerView) view.findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        galleryRecyclerView.setLayoutManager(layoutManager);

        layout_videoPlayerContainer = (LinearLayout) view.findViewById(R.id.layout_videoPlayerContainer);
        layout_imagePlayerContainer = (LinearLayout) view.findViewById(R.id.layout_imagePlayerContainer);

        layout_videoPlayerContainer.setOnClickListener(this);
        layout_imagePlayerContainer.setOnClickListener(this);

//        vidv_videoAlbum = (VideoView) view.findViewById(R.id.vidv_videoAlbum);
        imgv_imageAlbum = (ImageView) view.findViewById(R.id.imgv_imageAlbum);
        txtv_noData = (TextView) view.findViewById(R.id.txtv_noData);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerCustomItemTouchListener(getActivity(), galleryRecyclerView, this));
        galleryRecyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {

        galleryItemRecyclerAdapter.showLoading(true);
        galleryItemRecyclerAdapter.notifyDataSetChanged();

        if (galleryType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM)
            startAlbumsListOperation(GalleryItem.GALLERY_TYPE_VIDEO_ALBUM, false, (PagingManager.getLastPageNumber((ArrayList<GalleryItem>) galleryItemList) + 1));
        else
            startAlbumsListOperation(GalleryItem.GALLERY_TYPE_IMAGE_ALBUM, false, (PagingManager.getLastPageNumber((ArrayList<GalleryItem>) galleryItemList) + 1));
    }

    private void bindViews() {
        if (galleryItemList.size() > 0) {
            galleryRecyclerView.setVisibility(View.VISIBLE);
            txtv_noData.setVisibility(View.GONE);
            if(galleryItemRecyclerAdapter == null) {
                galleryItemRecyclerAdapter = new GalleryItemRecyclerAdapter(getActivity(), galleryItemList);
                galleryRecyclerView.setAdapter(galleryItemRecyclerAdapter);
            }
            else {
                galleryItemRecyclerAdapter.setItemsList(this.galleryItemList);
//                lastFirstVisiblePosition = Utilities.getScrollYOfRecycler(organizerRecyclerView);
                galleryItemRecyclerAdapter.showLoading(false);
                galleryItemRecyclerAdapter.notifyItemRangeChanged(0, this.galleryItemList.size());
            }
        } else {
            txtv_noData.setText(getString(R.string.album_no_files));
            galleryRecyclerView.setVisibility(View.GONE);
            txtv_noData.setVisibility(View.VISIBLE);
        }
    }

    private void startAlbumsListOperation(int albumTypeInGallery, boolean showDialog, int pageIndex) {
        if (albumTypeInGallery == GalleryItem.GALLERY_TYPE_IMAGE_ALBUM) {
            AlbumOperation operation = new AlbumOperation(GalleryItem.GALLERY_TYPE_IMAGE_ALBUM, RequestIds.PHOTO_ALBUM_REQUEST_ID,
                    showDialog, getActivity(), ServerKeys.PAGE_SIZE_DEFAULT, pageIndex, folderPath, albumId);
            operation.addRequsetObserver(this);
            operation.execute();
        } else {
            AlbumOperation operation = new AlbumOperation(GalleryItem.GALLERY_TYPE_VIDEO_ALBUM, RequestIds.VIDEO_ALBUM_REQUEST_ID,
                    showDialog, getActivity(), ServerKeys.PAGE_SIZE_DEFAULT, pageIndex, folderPath, albumId);
            operation.addRequsetObserver(this);
            operation.execute();
        }
    }

    private void getData() {
        //checked whether list is existing in GalleryManager or not
        //if it exists get it from the manager
        //if it's not exist get it from server

        //get ImageAlbum
        if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_ALBUM) {
            galleryItemList = GalleryManager.getInstance().getImageAlbumList(getActivity(), albumId);
            if (galleryItemList == null || galleryItemList.size() == 0) {
                startAlbumsListOperation(GalleryItem.GALLERY_TYPE_IMAGE_ALBUM, true, 0);
            } else {
                handleRequestFinished(RequestIds.PHOTO_ALBUM_REQUEST_ID, null, galleryItemList);
            }
        }

        //get VideoAlbum
        else if (galleryType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM) {
            galleryItemList = GalleryManager.getInstance().getVideosAlbumList(albumId, getActivity());
            if (galleryItemList == null || galleryItemList.size() == 0) {
                startAlbumsListOperation(GalleryItem.GALLERY_TYPE_VIDEO_ALBUM, true, 0);
            } else {
                handleRequestFinished(RequestIds.VIDEO_ALBUM_REQUEST_ID, null, galleryItemList);
            }
        }
    }

    @Override
    public void onItemClicked(View v, int position) {
        GalleryItem clickedItem = galleryItemList.get(position);
        //play video
        if (galleryType == GalleryItem.GALLERY_TYPE_VIDEO_ALBUM) {
//            ((MainActivity) getActivity()).openPlayerFragment("");
//            clickedItem.videoUrl = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
//            clickedItem.videoUrl = "https://www.youtube.com/watch?v=weGC-A551B4";
//            prepareVideo(clickedItem.videoUrl);
            playVideo(clickedItem.VideoGalleryUrl);
        }
        //display image
        else if (galleryType == GalleryItem.GALLERY_TYPE_IMAGE_ALBUM) {
            playImage(clickedItem.PhotoGalleryImageUrl);
        }
    }


    private void hidePlayers() {
        if (layout_videoPlayerContainer != null) {
            layout_videoPlayerContainer.setVisibility(View.GONE);
//            vidv_videoAlbum.stopPlayback();

        }

        if (layout_imagePlayerContainer != null) {
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
    public void prepareVideo(String videoUrl) {
        layout_videoPlayerContainer.setVisibility(View.VISIBLE);
        if (mediaController == null) {
            mediaController = new MediaController(getActivity());
            mediaController.setAnchorView(vidv_videoAlbum);
            vidv_videoAlbum.setMediaController(mediaController);

        }
        vidv_videoAlbum.setVideoURI(Uri.parse(videoUrl));
        if (dialog == null)
            dialog = CustomProgressDialog.getInstance(getActivity(), false);

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
                ((MainActivity) getActivity()).displayToast("stopped");
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

        if (!Utilities.isNullString(videoUrlvideoUrl) && videoUrlvideoUrl.contains("youtube")) {
            String videoId = MainActivity.getYoutubeVideoId(videoUrlvideoUrl);
            if (!Utilities.isNullString(videoId)/*&&youTubePlayer!=null*/) {
//			    layout_videoPlayerContainer.setVisibility(View.VISIBLE);
//			    youTubePlayer.cueVideo(videoId);

//			    initYoutubePlayer(videoId);
                ((MainActivity) getActivity()).playYouTubeVideo(videoId);
            }
        } else if (!Utilities.isNullString(videoUrlvideoUrl)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(videoUrlvideoUrl), "video/*");
            Intent choserIntent = Intent.createChooser(intent, "");
            if (choserIntent != null)
                startActivity(choserIntent);
            else
                ((MainActivity) getActivity()).displayToast("can't play this video file ");
        }
    }

    private void playImage(String imageUrl) {
        if (imgv_imageAlbum != null && layout_imagePlayerContainer != null) {
            Glide.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.drawable.image_place_holder) // optional
//                    .centerCrop()
                    .into(imgv_imageAlbum);

            layout_imagePlayerContainer.setVisibility(View.VISIBLE);
        }
    }

    //youtube player

    /**
     * Youtube api
     */
    private void initYoutubePlayer(final String videoId) {
        youTubePlayerSupportFragment = new YouTubePlayerSupportFragment();
        youTubePlayerSupportFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {


            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    AlbumFragment.this.youTubePlayer = youTubePlayer;
                    AlbumFragment.this.isPlayerReadey = true;
                    layout_videoPlayerContainer.setVisibility(View.VISIBLE);
                    youTubePlayer.cueVideo(videoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_youtube_player, youTubePlayerSupportFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemLongClicked(View v, int position) {

    }

    @Override
    public void handleRequestFinished(Object requestId, Throwable error, Object resultObject) {
        if (error == null) {
            Logger.instance().v(TAG, "Success \n\t\t" + resultObject);

            //photoAlbum
            if ((int) requestId == RequestIds.PHOTO_ALBUM_REQUEST_ID && resultObject != null) {
                ArrayList _galleryItemList = (ArrayList<GalleryItem>) resultObject;
                int pageIndex = PagingManager.getLastPageNumber(_galleryItemList);
                if (galleryItemList == null || pageIndex == 0)
                    galleryItemList = new ArrayList<>();
                galleryItemList.addAll(_galleryItemList);
                for (Object iterator : galleryItemList) {
                    ((GalleryItem) iterator).galleryItemType = GalleryItem.GALLERY_TYPE_IMAGE_ALBUM;
                }
                bindViews();

            }
            //videoAlbum
            else if ((int) requestId == RequestIds.VIDEO_ALBUM_REQUEST_ID && resultObject != null) {
                ArrayList<GalleryItem> _galleryItemList = (ArrayList<GalleryItem>) resultObject;
                int pageIndex = PagingManager.getLastPageNumber(_galleryItemList);
                if (galleryItemList == null || pageIndex == 0)
                    galleryItemList = new ArrayList<>();
                galleryItemList.addAll(_galleryItemList);
                for (Object iterator : galleryItemList) {
                    ((GalleryItem) iterator).galleryItemType = GalleryItem.GALLERY_TYPE_VIDEO_ALBUM;
                }
                bindViews();

            }
        } else if (error != null && error instanceof CTHttpError) {
            Logger.instance().v(TAG, error);
            int statusCode = ((CTHttpError) error).getStatusCode();
            String errorMsg = ((CTHttpError) error).getErrorMsg();
            if (RequestHandler.isRequestTimedOut(statusCode)) {
                ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), getActivity());
            } else if (statusCode == -1) {
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
