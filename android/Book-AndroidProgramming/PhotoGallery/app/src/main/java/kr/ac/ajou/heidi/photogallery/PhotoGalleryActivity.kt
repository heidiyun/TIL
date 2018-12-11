package kr.ac.ajou.heidi.photogallery

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

class PhotoGalleryActivity : SingleFragmentActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PhotoGalleryActivity::class.java)
        }
    }
    override fun createFragment(): Fragment {
        return PhotoGalleryFragment.newInstance()
    }


}
