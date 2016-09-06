package com.example.salah.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by salah on 2015/8/19.
 */
public class TrainSearch  extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train);
        Bundle bundle =this.getIntent().getExtras();
        String URL = bundle.getString("train");
        WebView myBrowser = (WebView) findViewById(R.id.mybrowser);

        WebSettings websettings = myBrowser.getSettings();
        websettings.setSupportZoom(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);

        myBrowser.setWebViewClient(new WebViewClient());

        myBrowser.loadUrl(URL);
    }
}
