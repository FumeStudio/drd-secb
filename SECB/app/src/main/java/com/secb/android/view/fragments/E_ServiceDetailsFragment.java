package com.secb.android.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.secb.android.R;
import com.secb.android.model.E_ServiceItem;
import com.secb.android.view.FragmentBackObserver;
import com.secb.android.view.SECBBaseActivity;
import com.secb.android.view.components.dialogs.CustomProgressDialog;

public class E_ServiceDetailsFragment extends SECBBaseFragment implements FragmentBackObserver, View.OnClickListener {

	WebView webv_E_ServicePage;
    View view;
	E_ServiceItem e_serviceItem;
    public static E_ServiceDetailsFragment newInstance( E_ServiceItem e_serviceItem)
    {
        E_ServiceDetailsFragment fragment = new E_ServiceDetailsFragment();
	    Bundle bundle = new Bundle();
	    bundle.putSerializable("e_serviceItem", e_serviceItem);
	    fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SECBBaseActivity) getActivity()).addBackObserver(this);
        ((SECBBaseActivity) getActivity()).setHeaderTitleText(getString(R.string.eservices_form));
 //        SECBBaseActivity.setMenuItemSelected(MenuItem.MENU_HOME);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
	    ((SECBBaseActivity) getActivity()).enableHeaderBackButton(this);
	    ((SECBBaseActivity) getActivity()).disableHeaderMenuButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SECBBaseActivity) getActivity()).removeBackObserver(this);
        ((SECBBaseActivity) getActivity()).showFilterButton(false);
	    ((SECBBaseActivity) getActivity()).disableHeaderBackButton();
	    ((SECBBaseActivity) getActivity()).enableHeaderMenuButton();
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
            view = LayoutInflater.from(getActivity()).inflate(R.layout.e_service_details_fragment, container, false);
            handleButtonsEvents();

        }
	    Bundle bundle = getArguments();
	    if(bundle!=null)
	    {
		    e_serviceItem = (E_ServiceItem)bundle.getSerializable("e_serviceItem");
	    }
        initViews(view);
        applyFonts();
        return view;
    }

    private void handleButtonsEvents() {
    }

    /*
     * Apply Fonts
     */
    private void applyFonts()
    {
    }

    private void goBack()
    {
        String backStateName = this.getClass().getName();
//     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity();
     ((SECBBaseActivity) getActivity()).finishFragmentOrActivity(backStateName);
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

    private void initViews(View view)
    {
	    webv_E_ServicePage = (WebView) view.findViewById(R.id.webv_E_ServicePage);
//	    loadUrl(webv_E_ServicePage, getResources().getString(R.string.sign_up_registeration_url));

    }

	public void loadUrl(final WebView myWebView, String url)
	{
//        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",  getString(R.string.loading), true);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);

		final ProgressDialog dialog =  CustomProgressDialog.getInstance(getActivity() ,true);
		dialog.show();
		myWebView.setVerticalScrollBarEnabled(false);
		myWebView.setHorizontalScrollBarEnabled(false);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);


		myWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.endsWith(".mp4")) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), "video/mp4");
					view.getContext().startActivity(intent);
				} else {
					view.loadUrl(url);
				}

				return true;
			}

			public void onPageFinished(WebView view, String url) {
				dialog.dismiss();
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
				String summary = "<html><body><strong>" + getString(R.string.lost_connection) + "</body></html>";
				myWebView.loadData(summary, "text/html", "utf-8");
			}

		}); //End WebViewClient

		myWebView.loadUrl(url);
	}


}