package kr.ac.ajou.heidi.photogallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_photo_page.view.*

class PhotoPageFragment : VisibleFragment() {
    companion object {
        val ARG_URI = "photo_page_url"

        fun newInstance(uri: Uri): Fragment {
            val args = Bundle()
            args.putParcelable(ARG_URI, uri)

            val fragment = PhotoPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var uri: Uri? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uri = arguments?.getParcelable(ARG_URI)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_page, container, false)

        progressBar = view.fragmentPhotoPageProgressBar
        view.fragmentPhotoPageProgressBar.max = 100
        view.fragmentPhotoPageWebView.settings.javaScriptEnabled = true

        view.fragmentPhotoPageWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar?.visibility = View.GONE
                } else {
                    progressBar?.visibility = View.VISIBLE
                    progressBar?.progress = newProgress
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                val activity = activity as AppCompatActivity
                activity.supportActionBar?.subtitle = title

            }
        }


        view.fragmentPhotoPageWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return !(uri?.scheme == "http" || uri?.scheme == "https")
            }
        }

        view.fragmentPhotoPageWebView.loadUrl(uri.toString())

        return view
    }


}