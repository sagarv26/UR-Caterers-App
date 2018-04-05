package com.example.sagar.urcatters.SubClass;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.sagar.urcatters.C0290R;

public class WebActivity extends Activity {
    String[] STORAGE_PERMISSION = new String[]{"android.permission.INTERNET"};
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0290R.layout.fb_webview);
        if (ContextCompat.checkSelfPermission(this, "android.permission.INTERNET") != 0) {
            ActivityCompat.requestPermissions(this, this.STORAGE_PERMISSION, 1);
        }
        this.webView = (WebView) findViewById(C0290R.id.webView);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient());
        this.webView.loadUrl("https://www.facebook.com/towardschange11/?hc_ref=ARR8bKHOHkQzvFGc_HPbcsDKT7PXqdQv2L6syGEMPHoljMt2rM9YHbBX8SI4a-5LHAs&pnref=story");
    }
}
