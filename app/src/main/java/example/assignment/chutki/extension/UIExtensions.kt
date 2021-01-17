package example.assignment.chutki.extension

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import example.assignment.chutki.R
import kotlin.math.ceil

object UIExtensions {

    fun ImageView.loadFromUrl(
        imageUrl: String?,
        cache: Boolean = true,
        imageLoadFailure: (() -> Unit)? = null,
        imageLoadSuccessful: () -> Unit
    ) {
        if (context!=null) {
            val placeHolder = ContextCompat.getDrawable(context, R.drawable.ic_placeholder_image)
            val requestOptions =
                RequestOptions().placeholder(placeHolder).fallback(placeHolder)
                    .diskCacheStrategy(if (cache) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE)
            Glide.with(context).load(imageUrl).apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageLoadFailure?.invoke()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageLoadSuccessful.invoke()
                        return false
                    }
                }).into(this)
        }
    }

    fun View.heightAnimator(
        initialHeight: Float,
        finalHeight: Float,
        animationDuration: Long = 300,
        interpolatorFactor: Float? = null,
        onAnimationEnd: () -> Unit = {},
    ) {
        val animation = ValueAnimator.ofFloat(initialHeight, finalHeight)
        animation.addUpdateListener { valueAnimator ->
            this@heightAnimator.alpha = 1f
            val animatedValue = valueAnimator.animatedValue as Float
            val layoutParams = this.layoutParams
            layoutParams.height = ceil(animatedValue).toInt()
            this.layoutParams = layoutParams
        }
        interpolatorFactor?.let { guardedFactor ->
            animation.interpolator = AccelerateInterpolator(guardedFactor)
        }
        animation.duration = animationDuration
        animation.onAnimationEnd(onAnimationEnd)
        animation.start()
    }

    fun Animator.onAnimationEnd(onEnd: () -> Unit) {
        this.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                onEnd.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {

        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

}