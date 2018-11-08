package kr.ac.ajou.heidi.beatbox

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_beat_box.view.*
import kotlinx.android.synthetic.main.list_item_sound.view.*


class BeatBoxFragment : Fragment() {

    companion object {
        fun newInstance(): BeatBoxFragment {
            return BeatBoxFragment()
        }
    }

    private lateinit var beatBox: BeatBox
    private val recyclerViewAdapter = SoundAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beatBox = BeatBox(activity?.applicationContext!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_beat_box, container, false)

        view.fragmentBeatBoxRecyclerView.layoutManager = GridLayoutManager(activity?.applicationContext!!, 3)
        recyclerViewAdapter.sounds = beatBox.sounds
        view.fragmentBeatBoxRecyclerView.adapter = recyclerViewAdapter

        return view
    }

    private class SoundHolder( container: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(container.context)
                    .inflate(R.layout.list_item_sound, container, false))

    private class SoundAdapter : RecyclerView.Adapter<SoundHolder>() {
        var sounds = emptyList<Sound>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder = SoundHolder(parent)


        override fun getItemCount(): Int = sounds.size

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            holder.itemView.list_item_sound_button.text = sounds[position].name
        }


    }
}