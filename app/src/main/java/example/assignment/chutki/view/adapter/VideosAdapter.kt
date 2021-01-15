package example.assignment.chutki.view.adapter

import android.content.Context
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.assignment.chutki.R
import example.assignment.chutki.extension.UIExtensions.loadFromUrl
import example.assignment.chutki.model.categories.Category
import example.assignment.chutki.model.videos.Video
import kotlinx.android.synthetic.main.layout_category_list_item.view.*
import kotlinx.android.synthetic.main.layout_video_list_item.view.*

class VideosAdapter(val context: Context,
                        var dataset: List<Video>,
                        val callback: VideoItemCallback,
                        val layoutManager: LinearLayoutManager
): RecyclerView.Adapter<VideosAdapter.VideosViewHolder>() {

    var selectedVideoPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_video_list_item,
            parent,
            false
        ) as ConstraintLayout
        return VideosViewHolder(layout)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun changeSelection(newlySelectedView: View?, newlySelectedPosition: Int) {

        layoutManager.findViewByPosition(selectedVideoPosition)?.let{
            showHideSelection(it, newlySelectedPosition)
        } ?: run {
            notifyItemChanged(selectedVideoPosition)
        }
        selectedVideoPosition = newlySelectedPosition
        newlySelectedView?.let{
            showHideSelection(it, newlySelectedPosition)
        } ?: run {
            layoutManager.findViewByPosition(selectedVideoPosition)?.let{
                showHideSelection(it, newlySelectedPosition)
            } ?: run {
                notifyItemChanged(selectedVideoPosition)
            }
        }
    }

    private fun showHideSelection(layout: View, position: Int) {

        if(selectedVideoPosition == position) {
            layout.background = ContextCompat.getDrawable(context,R.drawable.video_selection_rectangle)
        } else {
            layout.background = null
        }
    }

    inner class VideosViewHolder(itemView: ConstraintLayout) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.video_thumbnail_image
        private val loader: ProgressBar = itemView.video_thumbnail_progress_bar

        private val video: Video?
        get() {
            return dataset.getOrNull(adapterPosition)
        }

        init {
            itemView.setOnClickListener {
                changeSelection(itemView, adapterPosition)
                callback.onItemClick(adapterPosition)
            }
        }

        fun bind() {
            imageView.loadFromUrl(video?.thumbnailURL, imageLoadFailure = this::onImageFailure,
                imageLoadSuccessful = this::onImageLoadSuccess)
            showHideSelection(itemView, adapterPosition)
        }

        private fun onImageFailure() {
            Toast.makeText(context, "Image load failed", Toast.LENGTH_SHORT).show()
            loader.visibility = View.GONE
        }

        private fun onImageLoadSuccess() {
            loader.visibility = View.GONE
        }
    }
}

interface VideoItemCallback{
    fun onItemClick(position: Int)
}