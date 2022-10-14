package com.josycom.mayorjay.flowoverstack.view.answer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import timber.log.Timber
import java.net.URI
import java.net.URISyntaxException

class WebViewActivity : AppCompatActivity() {

    private var url: String? = null
    private lateinit var webView: WebView
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
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = null
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
                supportActionBar?.title = null
                supportActionBar?.subtitle = getHostName(url)
                progressBar.isVisible = true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                supportActionBar?.title = view.title
                progressBar.isInvisible = true
            }
        }
        webView.loadUrl(url ?: "")
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                progressBar.progress = progress + 10
                if (progress >= 100) supportActionBar?.title = view.title
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
            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getHostName(url: String): String {
        return try {
            val uri = URI(url)
            var newUrl = uri.host
            if (!newUrl.startsWith("www.")) newUrl = "www.$newUrl"
            newUrl
        } catch (e: URISyntaxException) {
            Timber.e(e)
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
            activity.startActivity(getIntent(activity, url))
        }

        private fun getIntent(context: Context?, url: String?): Intent {
            Intent(context, WebViewActivity::class.java).apply {
                putExtra(AppConstants.WEBVIEW_EXTRA_OBJECT, url)
                return this
            }
        }
    }
}