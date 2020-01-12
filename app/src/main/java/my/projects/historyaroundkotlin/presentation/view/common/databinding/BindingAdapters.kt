package my.projects.historyaroundkotlin.presentation.view.common.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import my.projects.historyaroundkotlin.R

@BindingAdapter("android:text")
fun setText(textView: TextView, @StringRes textRes: Int) {
    if (textRes != 0) {
        textView.setText(textRes)
    }
}

@BindingAdapter("android:src")
fun setImgSrcUri(imageView: ImageView, url: String?) {
    Glide
        .with(imageView.context)
        .load(url)
        .centerInside()
        .placeholder(R.drawable.ic_image_placeholder)
        .error(R.drawable.ic_broken_image_placeholder)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                return false
            }
        })
        .into(imageView)
}

@BindingAdapter("android:src")
fun setImgSrcId(imageView: ImageView, resid: Int) {
    if (resid != 0) {
        imageView.setImageDrawable(imageView.context.getDrawable(resid))
    } else {
        imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_image_placeholder))
    }
}