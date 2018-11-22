package kr.ac.ajou.heidi.nerdlauncher


class NerdLauncherActivity : SingleFragmentActivity() {
    override fun createFragment(): androidx.fragment.app.Fragment {
        return NerdLauncherFragment.newInstance()
    }


}
