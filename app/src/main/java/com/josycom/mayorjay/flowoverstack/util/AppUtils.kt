package com.josycom.mayorjay.flowoverstack.util

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.josycom.mayorjay.flowoverstack.ui.activity.WebViewActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * FlowOverStack
 * Created by Mbuodile Obiosio on Jun 05, 2020
 * https://twitter.com/cazewonder
 * Modified by MayorJay
 */
object AppUtils {

    private const val OPEN_IN_APP_BROWSER = true

    fun directLinkToBrowser(activity: AppCompatActivity, url: String?) {
        try {
            if (OPEN_IN_APP_BROWSER) {
                WebViewActivity.navigate(activity, url)
            } else {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_LONG).show()
        }
    }

    fun toNormalDate(seconds: Long): String? {
        val date = Date(seconds * 1000L)
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getFormattedTags(tagList: List<String>): String {
        val builder = StringBuilder()
        for (i in tagList.indices) {
            builder.append(tagList[i])
            if (i != tagList.size - 1) {
                builder.append(", ")
            }
        }
        return builder.toString()
    }
}