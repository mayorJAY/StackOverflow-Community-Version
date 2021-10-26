package com.josycom.mayorjay.flowoverstack.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.widget.ProgressBar
import android.os.Bundle
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import android.graphics.PorterDuff
import android.annotation.SuppressLint
import android.webkit.WebViewClient
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.annotation.ColorInt
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import java.lang.Exception
import java.net.URI
import java.net.URISyntaxException

class WebViewActivity : AppCompatActivity() {

    private var url: String? = null
    private lateinit var webView: WebView
    private lateinit var actionBar: ActionBar
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        // get extra object
        url = intent.getStringExtra(AppConstants.WEBVIEW_EXTRA_OBJECT)
        initComponent()
        initToolbar()
        loadWebFromUrl()
    }

    private fun initComponent() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        progressBar.progressDrawable.setColorFilter(resources.getColor(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN)
        progressBar.setBackgroundColor(Color.parseColor("#1A000000"))
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_clear)
        toolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        actionBar.title = null
        changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.colorPrimary))
        setSystemBarColor(this)
        setSystemBarLight(this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebFromUrl() {
        webView.apply {
            settings.javaScriptEnabled = true
            settings.defaultTextEncodingName = "utf-8"
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            setBackgroundColor(Color.TRANSPARENT)
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                actionBar.title = null
                actionBar.subtitle = getHostName(url)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                actionBar.title = view.title
                progressBar.visibility = View.INVISIBLE
            }
        }
        webView.loadUrl(url!!)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                progressBar.progress = progress + 10
                if (progress >= 100) actionBar.title = view.title
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_refresh -> {
                loadWebFromUrl()
            }
            R.id.action_browser -> {
                AppUtils.directLinkToBrowser(this, url)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_webview, menu)
        changeMenuIconColor(menu, resources.getColor(R.color.colorPrimary))
        return true
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        webView.onResume()
        super.onResume()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    private fun setSystemBarColor(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = act.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor("#f2f2f2")
        }
    }

    private fun changeOverflowMenuIconColor(toolbar: Toolbar, @ColorInt color: Int) {
        try {
            val drawable = toolbar.overflowIcon
            drawable?.mutate()
            drawable!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getHostName(url: String): String {
        return try {
            val uri = URI(url)
            var newUrl = uri.host
            if (!newUrl.startsWith("www.")) newUrl = "www.$newUrl"
            newUrl
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            url
        }
    }

    companion object {
        fun setSystemBarLight(act: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val view = act.findViewById<View>(android.R.id.content)
                var flags = view.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
            }
        }

        fun changeMenuIconColor(menu: Menu, @ColorInt color: Int) {
            for (i in 0 until menu.size()) {
                val drawable = menu.getItem(i).icon ?: continue
                drawable.mutate()
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }

        fun navigate(activity: Activity, url: String?) {
            val i = getIntent(activity, url)
            activity.startActivity(i)
        }

        private fun getIntent(context: Context?, url: String?): Intent {
            Intent(context, WebViewActivity::class.java).apply {
                putExtra(AppConstants.WEBVIEW_EXTRA_OBJECT, url)
                return this
            }
        }
    }
}