package com.example.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getWindow().setStatusBarColor(ContextCompat.getColor(WebActivity.this,R.color.statusbar));

        // Get the data passed from the intent
        String weblink = getIntent().getStringExtra("Description");

        // Initialize WebView
        webView = findViewById(R.id.webView);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set a WebViewClient to handle page navigation
        webView.setWebViewClient(new WebViewClient());

        // Load the URL
        webView.loadUrl(weblink);
    }

    // Override the method to intercept key events
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Return true to indicate that the key event has been consumed
        // and should not be dispatched further, or false to indicate
        // that you want the key event to continue to be dispatched to
        // the next receiver.
        return super.dispatchKeyEvent(event);
    }
}
