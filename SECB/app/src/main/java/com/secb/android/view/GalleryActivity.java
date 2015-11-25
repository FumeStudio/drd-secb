package com.secb.android.view;

import android.os.Bundle;
import android.view.View;

import com.secb.android.R;
import com.secb.android.controller.backend.RequestIds;
import com.secb.android.model.GalleryItem;
import com.secb.android.view.fragments.AlbumFragment;
import com.secb.android.view.fragments.GalleryFragment;
import com.secb.android.view.fragments.NewsListFragment;

import net.comptoirs.android.common.controller.backend.CTHttpError;
import net.comptoirs.android.common.controller.backend.RequestHandler;
import net.comptoirs.android.common.controller.backend.RequestObserver;
import net.comptoirs.android.common.helper.ErrorDialog;
import net.comptoirs.android.common.helper.Logger;

public class GalleryActivity extends SECBBaseActivity implements RequestObserver {
	private static final String TAG = "GalleryActivity";
//this activity for gallery list fragment , Album list fragment

	public boolean isDoublePane = false;

	/*isComingFromMenu = true means that it  coming from
	 * side menu ,and the intent does not contain Extras
	 * so in mobile screen we should add details
	 * fragment to back stack to save history*/
	public boolean isComingFromMenu = true;
	private GalleryItem currentItemDetails;

	public int galleryType = 0;
	String folderPath;
	private String albumId;

	public GalleryActivity() {
		super(R.layout.activity_activity, true);
	}

	@Override
	protected void doOnCreate(Bundle arg0) {
		initObservers();
		initViews();
		isDoublePane = findViewById(R.id.details_container) != null;

		applyFonts();
		if (getIntent() != null && getIntent().getExtras() != null) {
			if (getIntent().getExtras().containsKey("albumId") &&
					getIntent().getExtras().containsKey("folderPath") )
			{
				isComingFromMenu = false;
				galleryType = getIntent().getExtras().getInt("galleryType");
				albumId=getIntent().getExtras().getString("albumId");
				folderPath=getIntent().getExtras().getString("folderPath");

				openAlbumFragment(galleryType,folderPath,albumId,true);
			}

			if (getIntent().getExtras().containsKey("galleryType"))
				this.galleryType = getIntent().getExtras().getInt("galleryType");

		}
		openGalleryFragment(true);

		//load details of first item
		if (isDoublePane) {

			openAlbumFragment(galleryType, folderPath, albumId, true); // I made it true, to make the gallery get the back event to handle the image appearance.
		}

	}

	public void openGalleryFragment(boolean isAddToBackStack) {
		GalleryFragment fragment = GalleryFragment.newInstance(galleryType);
		inflateFragmentInsideLayout(fragment, R.id.list_container, isAddToBackStack);

	}

	public void openAlbumFragment(int galleryType, String folderPath, String albumId,boolean isAddToBackStack)
	{
		AlbumFragment fragment = AlbumFragment.newInstance(galleryType, folderPath, albumId);
		if(isDoublePane)
			inflateFragmentInsideLayout(fragment, R.id.details_container, isAddToBackStack);
		else
			inflateFragmentInsideLayout(fragment, R.id.list_container, isAddToBackStack);

	}


	private void initObservers() {
	}

	private void initViews() {

	}

	private void applyFonts() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			default:
				break;
		}
	}


	@Override
	public void handleRequestFinished(Object requestId, Throwable error, Object resulObject) {
		if (error == null) {
			if ((int) requestId == RequestIds.NEWS_LIST_REQUEST_ID &&
					resulObject != null) {

			}
		} else if (error != null && error instanceof CTHttpError) {
			int statusCode = ((CTHttpError) error).getStatusCode();
			String errorMsg = ((CTHttpError) error).getErrorMsg();
			if (RequestHandler.isRequestTimedOut(statusCode)) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.timeout), GalleryActivity.this);
			} else if (statusCode == -1) {
				ErrorDialog.showMessageDialog(getString(R.string.attention), getString(R.string.conn_error),
						GalleryActivity.this);
			} else {
				ErrorDialog.showMessageDialog(getString(R.string.attention), errorMsg,
						GalleryActivity.this);
			}

			Logger.instance().v(TAG, error);
		}
	}

	@Override
	public void requestCanceled(Integer requestId, Throwable error) {

	}

	@Override
	public void updateStatus(Integer requestId, String statusMsg) {

	}

	public void setNewsRequstObserver(NewsListFragment newsListFragment) {

	}
}
