package kr.ac.ajou.heidi.nerdlauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_nerd_launcher.view.*
import kotlinx.android.synthetic.main.simple_list_item_1.view.*
import java.util.*

class NerdLauncherFragment : Fragment() {

    companion object {
        val TAG = NerdLauncherFragment::class.java.name

        fun newInstance(): NerdLauncherFragment {
            return NerdLauncherFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false)
        view.fragmentNerdLauncherRecyclerView.layoutManager = LinearLayoutManager(activity)
        setupAdapter(view)
        return view
    }


    fun setupAdapter(view: View) {
        val startupIntent = Intent(Intent.ACTION_MAIN)
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val pm = activity?.packageManager
        val activities = pm?.queryIntentActivities(startupIntent, 0) ?: return
        view.fragmentNerdLauncherRecyclerView.adapter = ActivityAdapter(activities)
        activities.sortWith(Comparator { a, b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                    a.loadLabel(pm).toString(),
                    b.loadLabel(pm).toString()
            )
        })

        Log.i(TAG, "Found ${activities.size} activities.")
    }

    private inner class ActivityHolder(parent: ViewGroup) : RecyclerView
    .ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item_1, parent, false))

    private inner class ActivityAdapter(val activities: List<ResolveInfo>) : RecyclerView.Adapter<ActivityHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
            return ActivityHolder(parent)
        }

        override fun getItemCount(): Int = activities.size

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val resolveInfo = activities[position]
            with(holder.itemView) {
                val pm = activity?.packageManager
                val appName = resolveInfo.loadLabel(pm).toString()
                nameTextView.text = appName
                nameTextView.setOnClickListener {
                    val activityInfo = resolveInfo.activityInfo
                    val intent = Intent(Intent.ACTION_MAIN).setClassName(activityInfo.applicationInfo.packageName,
                            activityInfo.name).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }

    }
}
