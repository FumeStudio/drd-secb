package com.secb.android.view;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.secb.android.R;
import com.secb.android.view.components.dialogs.CustomProgressDialog;

public class SignUpActivity extends SECBBaseActivity implements View.OnClickListener{

   WebView webv_signUpPage;

	boolean loadingFinished = true;
	boolean redirect = false;


	public SignUpActivity() {
        super(R.layout.signup, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableHeaderBackButton(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableHeaderBackButton();
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
        initViews();
        showHeader(false);
        setHeaderTitleText(getResources().getString(R.string.login_signup));
    }

    private void initViews()
    {
        webv_signUpPage = (WebView) findViewById(R.id.webv_signUpPage);
        loadUrl(webv_signUpPage, getResources().getString(R.string.sign_up_registeration_url));

    }

    public void loadUrl(final WebView myWebView, String url)
    {
//        final ProgressDialog dialog = ProgressDialog.show(SignUpActivity.this, "",  getString(R.string.loading), true);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);

        final ProgressDialog dialog =  CustomProgressDialog.getInstance(SignUpActivity.this ,true);
        dialog.show();

//	    clear cookie
	    myWebView.clearCache(true);
	    myWebView.clearHistory();


        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
	            if (!loadingFinished) {
		            redirect = true;
	            }
	            loadingFinished = false;
/*                if (url.endsWith(".mp4"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video/mp4");
                    view.getContext().startActivity(intent);
                }
                else*/
                {
                    view.loadUrl(url);
                }

                return true;
            }

	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
		        super.onPageStarted(view, url, favicon);
		        loadingFinished = false;
		        if(!dialog.isShowing())
			        dialog.show();
	        }
            public void onPageFinished(WebView view, String url)
            {
	            if(!redirect){
		            loadingFinished = true;
	            }

	            if(loadingFinished && !redirect){
		            //HIDE LOADING IT HAS FINISHED
		            dialog.dismiss();
	            }
	            else{
		            redirect = false;
	            }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                Toast.makeText(SignUpActivity.this, description, Toast.LENGTH_SHORT).show();
                String summary = "<html><body><strong>" + getString(R.string.lost_connection) + "</body></html>";
                myWebView.loadData(summary, "text/html", "utf-8");
            }

        }); //End WebViewClient

        myWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewBackHeader:
                finish();
                break;

            default:
                break;
        }
    }
}
