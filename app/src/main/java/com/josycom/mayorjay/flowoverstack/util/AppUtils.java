package com.josycom.mayorjay.flowoverstack.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.josycom.mayorjay.flowoverstack.ui.WebViewActivity;

import java.net.URI;

/**
 * FlowOverStack
 * Created by Mbuodile Obiosio on Jun 05, 2020
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class AppUtils {

    public static final boolean OPEN_IN_APP_BROWSER = true;

    public static void directLinkToBrowser(AppCompatActivity activity, String url) {
        try {
            if (OPEN_IN_APP_BROWSER) {
                WebViewActivity.navigate(activity, url);
            } else {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_LONG).show();
        }
    }

    public static void directLinkToBrowser(Context activity, String url) {
        try {
            if (OPEN_IN_APP_BROWSER) {
                WebViewActivity.navigate(activity, url);
            } else {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_LONG).show();
        }
    }

    public static void directLinkToBrowserWithTime(AppCompatActivity activity, String url) {
        url = appendQuery(url, "t=" + System.currentTimeMillis());
        if (!URLUtil.isValidUrl(url)) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_LONG).show();
            return;
        }
        if (OPEN_IN_APP_BROWSER) {
            WebViewActivity.navigate(activity, url);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private static String appendQuery(String uri, String appendQuery) {
        try {
            URI oldUri = new URI(uri);
            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                newQuery = appendQuery;
            } else {
                newQuery += "&" + appendQuery;
            }
            URI newUri = new URI(
                    oldUri.getScheme(),
                    oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment()
            );
            return newUri.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return uri;
        }
    }
}
