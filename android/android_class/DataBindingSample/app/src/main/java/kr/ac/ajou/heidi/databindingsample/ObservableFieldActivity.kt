package kr.ac.ajou.heidi.databindingsample

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.*
import kr.ac.ajou.heidi.databindingsample.databinding.ObservableFieldProfileBinding

class User(var name: String, var lastName: String, var likes: ObservableInt)

class ObservableFieldActivity : AppCompatActivity() {

    val user = User("heidi", "Goo", ObservableInt(0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ObservableFieldProfileBinding = DataBindingUtil.setContentView(this, R.layout.observable_field_profile)
        binding.user = user
    }

    fun onLike(view: View) {
        user.likes.set(user.likes.get() + 1)
    }
}

@BindingMethods(
        BindingMethod(type = ImageView::class,
                attribute = "app:srcCompat",
                method = "setImageResource"))
class MyBindingMethods

object BindingAdapters {
    @BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
    @JvmStatic
    fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
        progressBar.progress = (likes * max / 5).coerceAtMost(max)
    }
}
