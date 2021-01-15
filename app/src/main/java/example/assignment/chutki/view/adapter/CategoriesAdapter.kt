package example.assignment.chutki.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import example.assignment.chutki.R
import example.assignment.chutki.extension.UIExtensions.loadFromUrl
import example.assignment.chutki.model.categories.Category
import kotlinx.android.synthetic.main.layout_category_list_item.view.*

class CategoriesAdapter(val context: Context,
                        var dataset: List<Category>,
                        val callback: CategoryItemCallback
): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_category_list_item,
            parent,
            false
        ) as ConstraintLayout
        return CategoriesViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class CategoriesViewHolder(layout: ConstraintLayout) : RecyclerView.ViewHolder(layout) {
        private val imageView: ImageView = layout.category_image
        private val loader: ProgressBar = layout.category_progress_bar
        private val category: Category?
        get() {
            return dataset.getOrNull(adapterPosition)
        }
        init {
            layout.setOnClickListener {
                callback.onItemClick(adapterPosition)
            }
        }

        fun bind() {
            imageView.loadFromUrl(category?.image, imageLoadFailure = this::onImageFailure,
                imageLoadSuccessful = this::onImageLoadSuccess)
        }

        private fun onImageFailure() {
            Toast.makeText(context, context.getString(R.string.image_load_failed_error), Toast.LENGTH_SHORT).show()
            loader.visibility = View.GONE
        }

        private fun onImageLoadSuccess() {
            loader.visibility = View.GONE
        }
    }
}

interface CategoryItemCallback{
    fun onItemClick(position: Int)
}