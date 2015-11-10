package com.secb.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.secb.android.R;

import net.comptoirs.android.common.helper.Utilities;

public class YoutubePlayerActivity extends YouTubeBaseActivity /*SECBBaseActivity */implements YouTubePlayer.OnInitializedListener
{
	public static final String API_KEY="AIzaSyBS_QWCoBrPxv3BR7rj3K5ERq0LJZwStXo";
	public String video_id="_oEA18Y8gM0";
	private static final int RECOVERY_DIALOG_REQUEST = 1;

	// YouTube player view
	private YouTubePlayerView youTubeView;


/*	public YoutubePlayerActivity ()
	{
		super(R.layout.youtube_player_activity, true);
	}*/


/*	@Override
	protected void doOnCreate(Bundle arg0) {
		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		// Initializing video player with developer key
		if(getIntent()!=null && getIntent().getExtras()!=null
				&& !Utilities.isNullString( getIntent().getExtras().getString("youtubeVideoId")))
		{
			video_id =  getIntent().getExtras().getString("youtubeVideoId");

		}
		youTubeView.initialize(API_KEY, this);


	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.youtube_player_activity);

		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

		if(getIntent()!=null && getIntent().getExtras()!=null
				&& !Utilities.isNullString( getIntent().getExtras().getString("youtubeVideoId")))
		{
			video_id =  getIntent().getExtras().getString("youtubeVideoId");

		}
		youTubeView.initialize(API_KEY, this);
	}



	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
	                                    YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {

			// loadVideo() will auto play video
			// Use cueVideo() method, if you don't want to play it automatically
			player.loadVideo(video_id);

			// Hiding player controls
//			player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
//			player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
		}
	}


	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
	                                    YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			// Retry initialization if user performed a recovery action
			getYouTubePlayerProvider().initialize(API_KEY, this);
		}
	}

	private YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

}
