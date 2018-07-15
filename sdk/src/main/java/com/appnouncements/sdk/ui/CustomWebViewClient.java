package com.appnouncements.sdk.ui;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.appnouncements.sdk.R;

import java.lang.ref.WeakReference;

public class CustomWebViewClient extends WebViewClient {
    private static String dummyBaseUrl = "https://www.appnouncements-dummy.com/";
    private final WeakReference<ReleaseNotesActivity> activity;

    CustomWebViewClient(ReleaseNotesActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        activity.get().progressbar.hide();

        // Hack to determine if our release notes page loaded without a major error
        if (view.getTitle().startsWith("Appnouncements - ")) {
            activity.get().onReleaseNotesLoaded();
        } else {
            loadErrorPage(view);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        loadErrorPage(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (request.isForMainFrame()) {
            loadErrorPage(view);
        }
    }

    private void loadErrorPage(WebView view) {
        if(view != null){
            String htmlData ="<html style='height: 100%; color: #343a40; background-color: #f8f9fa; font-family: sans-serif;'><body style='height: 100%'>" +
                    "<table style=\"width: 100%; height: 100%\"><tr><td style=\"text-align: center; vertical-align: middle;\">" +
                    "<svg fill='#343a40' height=\"128\" class=\"octicon octicon-radio-tower\" viewBox=\"0 0 16 16\" version=\"1.1\" width=\"96\" aria-hidden=\"true\"><path fill-rule=\"evenodd\" d=\"M4.79 6.11c.25-.25.25-.67 0-.92-.32-.33-.48-.76-.48-1.19 0-.43.16-.86.48-1.19.25-.26.25-.67 0-.92a.613.613 0 0 0-.45-.19c-.16 0-.33.06-.45.19-.57.58-.85 1.35-.85 2.11 0 .76.29 1.53.85 2.11.25.25.66.25.9 0zM2.33.52a.651.651 0 0 0-.92 0C.48 1.48.01 2.74.01 3.99c0 1.26.47 2.52 1.4 3.48.25.26.66.26.91 0s.25-.68 0-.94c-.68-.7-1.02-1.62-1.02-2.54 0-.92.34-1.84 1.02-2.54a.66.66 0 0 0 .01-.93zm5.69 5.1A1.62 1.62 0 1 0 6.4 4c-.01.89.72 1.62 1.62 1.62zM14.59.53a.628.628 0 0 0-.91 0c-.25.26-.25.68 0 .94.68.7 1.02 1.62 1.02 2.54 0 .92-.34 1.83-1.02 2.54-.25.26-.25.68 0 .94a.651.651 0 0 0 .92 0c.93-.96 1.4-2.22 1.4-3.48A5.048 5.048 0 0 0 14.59.53zM8.02 6.92c-.41 0-.83-.1-1.2-.3l-3.15 8.37h1.49l.86-1h4l.84 1h1.49L9.21 6.62c-.38.2-.78.3-1.19.3zm-.01.48L9.02 11h-2l.99-3.6zm-1.99 5.59l1-1h2l1 1h-4zm5.19-11.1c-.25.25-.25.67 0 .92.32.33.48.76.48 1.19 0 .43-.16.86-.48 1.19-.25.26-.25.67 0 .92a.63.63 0 0 0 .9 0c.57-.58.85-1.35.85-2.11 0-.76-.28-1.53-.85-2.11a.634.634 0 0 0-.9 0z\"></path></svg>" +
                    "<h2>Can't Connect</h2>Check your network and try again." +
                    "<br><br><a style=\"color: #343a40; text-decoration: none; display: inline-block; text-align: center; vertical-align: middle; border: 2px solid #343a40; border-radius: .5rem; padding: 6px 12px\" href=\"/retry\">Retry</a>" +
                    "</td></tr></table></body>";
            view.loadUrl("about:blank");
            view.loadDataWithBaseURL(dummyBaseUrl, htmlData, "text/html", "UTF-8",null);
            view.invalidate();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return internalShouldOverrideUrlLoading(url);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return internalShouldOverrideUrlLoading(request.getUrl().toString());
    }

    private boolean internalShouldOverrideUrlLoading(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.equals(dummyBaseUrl + "retry")) {
                activity.get().loadReleaseNotes();
                return true;
            }

            String action;
            if (url.startsWith("mailto:")) {
                action = Intent.ACTION_SENDTO;
            } else if (url.startsWith("tel:")) {
                action = Intent.ACTION_DIAL;
            } else {
                action = Intent.ACTION_VIEW;
            }

            try {
                activity.get().startActivity(new Intent(action, Uri.parse(url)));
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(activity.get(), R.string.error_opening_link, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return true;
    }
}