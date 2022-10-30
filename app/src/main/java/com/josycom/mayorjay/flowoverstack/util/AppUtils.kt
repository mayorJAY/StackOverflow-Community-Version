package com.josycom.mayorjay.flowoverstack.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.view.answer.WebViewActivity
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                launchViewIntent(activity, url)
            }
        } catch (e: Exception) {
            Timber.e(e)
            showToast(activity, activity.getString(R.string.cannot_open_url))
        }
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun toNormalDate(seconds: Long): String? {
        val date = Date(seconds * 1000L)
        val simpleDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getFormattedTags(tagList: List<String>): String = tagList.joinToString(", ")

    fun shareContent(content: String, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, content)
        context.startActivity(Intent.createChooser(intent, "Share Via"))
    }

    fun launchViewIntent(context: Context, url: String?) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun launchEmailIntent(context: Context, address: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$address"))
        context.startActivity(Intent.createChooser(intent, "Send With"))
    }
}