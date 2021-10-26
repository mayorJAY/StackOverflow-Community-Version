package com.josycom.mayorjay.flowoverstack.util;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.josycom.mayorjay.flowoverstack.ui.activity.WebViewActivity;

/**
 * FlowOverStack
 * Created by Mbuodile Obiosio on Jun 05, 2020
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class AppUtils {

    private static final boolean OPEN_IN_APP_BROWSER = true;

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

}
