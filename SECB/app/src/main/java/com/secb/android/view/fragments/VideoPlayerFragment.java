package com.secb.android.view.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.MediaController;
import android.widget.VideoView;

import com.secb.android.R;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;

public class VideoPlayerFragment extends SECBBaseFragment
        implements FragmentBackObserver, View.OnClickListener

{

    View view;
    String videoUrl;
    VideoView videoView;
    public static VideoPlayerFragment newInstance(String videoUrl) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", videoUrl);
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
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.video_album));
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
        ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.video_player_fragment, container, false);
            handleButtonsEvents();
            applyFonts();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            videoUrl = bundle.getString("videoUrl");
        }
        initViews(view);

        return view;
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
            default:
                break;
        }
    }


    private void initViews(View view) {
        videoView = (VideoView) view.findViewById(R.id.video_view);
        if(videoView!=null){
            doPlayerSetting();
            startPlayer();
        }
    }

    public void doPlayerSetting()
    {
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    private void startPlayer() {
        videoUrl = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
        videoUrl = "https://youtu.be/Mbs0PTyZfxA";
        videoUrl = "http://www.sample-videos.com/video/mp4/360/big_buck_bunny_360p_50mb.mp4";
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.start();
    }

}
