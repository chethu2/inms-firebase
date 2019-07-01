package com.invent.inms.helper;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;

import inms.invent.com.i_invent_inms.R;

public class WebViewClass extends Activity {
    private WebView recipeWebView;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_info);
        recipeWebView = findViewById(R.id.recipe_webview);
        bundle = getIntent().getExtras();
        String preparationUrl = bundle.getString("preparationUrl");
        recipeWebView.loadUrl(preparationUrl);
    }
}
