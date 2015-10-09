package com.secb.android.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.secb.android.R;

public class SignUpActivity extends SECBBaseActivity {

   WebView webv_signUpPage;

    public SignUpActivity() {
        super(R.layout.signup, false);
    }

    @Override
    protected void doOnCreate(Bundle arg0)
    {
        initViews();
/*        showHeader(false);
        setHeaderTitleText(getResources().getString(R.string.login_signup));*/
    }

    private void initViews()
    {
        webv_signUpPage = (WebView) findViewById(R.id.webv_signUpPage);
        loadUrl(webv_signUpPage,getResources().getString(R.string.sign_up_registeration_url));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
        }
    }
    public void loadUrl(final WebView myWebView, String url)
    {
        final ProgressDialog dialog = ProgressDialog.show(SignUpActivity.this, "",  getString(R.string.loading), true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.endsWith(".mp4"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video/mp4");
                    view.getContext().startActivity(intent);
                }
                else
                {
                    view.loadUrl(url);
                }

                return true;
            }

            public void onPageFinished(WebView view, String url)
            {
                dialog.dismiss();
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

}
