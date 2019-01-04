package kr.ac.ajou.heidi.draganddraw

import androidx.fragment.app.Fragment

class DragAndDrawActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return DragAndDrawFragment.newInstance()
    }
}
