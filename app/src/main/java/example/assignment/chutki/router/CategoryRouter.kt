package example.assignment.chutki.router

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import example.assignment.chutki.view.activity.VideoPlaybackActivity
import javax.inject.Inject

class CategoryActivityRouterImpl @Inject constructor(private val activity: FragmentActivity)
    : CategoryActivityRouter {

    override fun openVideoPlayback(categorySelected: String) {
        activity.let{safeActivity ->
            val intent = Intent(safeActivity, VideoPlaybackActivity::class.java)
            intent.putExtra(VideoPlaybackActivity.SELECTED_ITEM_POSITION_KEY, categorySelected)
            safeActivity.startActivity(intent)
        }
    }
}

interface CategoryActivityRouter{
    fun openVideoPlayback(categorySelected: String)
}