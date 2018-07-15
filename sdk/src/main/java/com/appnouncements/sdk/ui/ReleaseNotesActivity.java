package com.appnouncements.sdk.ui;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.appnouncements.sdk.BuildConfig;
import com.appnouncements.sdk.R;
import com.appnouncements.sdk.VersionManager;

public class ReleaseNotesActivity extends AppCompatActivity {
    public static String EXTRAS_RELEASE_NOTES_URL = "APPNOUNCEMENTS_RELEASE_NOTES_URL";
    public static String EXTRAS_ACTION_BAR_TITLE = "APPNOUNCEMENTS_TITLE";

    private WebView webview;
    ContentLoadingProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
        setupToolbar();
        setupWebview();
        loadReleaseNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webview.onResume();
    }

    @Override
    protected void onPause() {
        webview.onPause();
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.noop, R.anim.slide_out_bottom);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void loadReleaseNotes() {
        progressbar.show();
        webview.loadUrl(getIntent().getStringExtra(EXTRAS_RELEASE_NOTES_URL));
    }

    private void setupView() {
        setContentView(R.layout.release_notes_dialog);
        webview = findViewById(R.id.webview);
        progressbar = findViewById(R.id.progressbar);
    }

    private void setupWebview() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUserAgentString("appnouncements-android/" + BuildConfig.VERSION_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            settings.setOffscreenPreRaster(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        webview.setWebViewClient(new CustomWebViewClient(this));
    }

    private void setupToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.releaseNotesToolbar));
        getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRAS_ACTION_BAR_TITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void onReleaseNotesLoaded() {
        (new VersionManager(getApplication())).updateLastSeenVersionCode();
    }
}