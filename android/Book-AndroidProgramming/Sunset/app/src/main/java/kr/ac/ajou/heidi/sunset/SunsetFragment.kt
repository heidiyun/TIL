package kr.ac.ajou.heidi.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sunset.view.*

class SunsetFragment : Fragment() {
    companion object {
        fun newInstance(): SunsetFragment {
            return SunsetFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sunset, container, false)
        view.setOnClickListener {
            startAnimation(view)
        }
        return view
    }

    private fun startAnimation(view: View) {

        val sunYStart = view.sun.top.toFloat()
        val sunYEnd = view.sky.bottom.toFloat()

        val upAnimator = ObjectAnimator.ofFloat(
                view.sun,
                "y",
                view.sky.bottom.toFloat(),
                view.sun.top.toFloat()
        ).setDuration(3000)

        val sunriseSkyAnimator = ObjectAnimator
                .ofInt(view.sky,
                        "backgroundColor",
                        ContextCompat.getColor(view.context, R.color.sunsetSky),
                        ContextCompat.getColor(view.context, R.color.blueSky)

                ).setDuration(3000)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())

        val daySkyAnimator = ObjectAnimator
                .ofInt(view.sky, "backgroundColor",
                        ContextCompat.getColor(view.context, R.color.nightSky),
                        ContextCompat.getColor(view.context, R.color.sunsetSky))
                .setDuration(1500)

        daySkyAnimator.setEvaluator(ArgbEvaluator())


        val heightAnimator = ObjectAnimator
                .ofFloat(view.sun, "y", sunYStart, sunYEnd)
                .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
                .ofInt(view.sky, "backgroundColor",
                        ContextCompat.getColor(view.context, R.color.blueSky),
                        ContextCompat.getColor(view.context, R.color.sunsetSky))
                .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
                .ofInt(view.sky, "backgroundColor",
                        ContextCompat.getColor(view.context, R.color.sunsetSky),
                        ContextCompat.getColor(view.context, R.color.nightSky)
                )
                .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

//        heightAnimator.start()
//        sunsetSkyAnimator.start()

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator)
        animatorSet.start()

        val sunriseAnimatorSet = AnimatorSet()
        sunriseAnimatorSet.play(upAnimator)
                .with(sunriseSkyAnimator)
                .after(daySkyAnimator)
                .after(animatorSet)
        sunriseAnimatorSet.start()
    }

}