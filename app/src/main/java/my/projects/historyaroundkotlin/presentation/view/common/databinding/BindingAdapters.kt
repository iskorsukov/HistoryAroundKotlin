package my.projects.historyaroundkotlin.presentation.view.common.databinding

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:text")
fun setText(textView: TextView, @StringRes textRes: Int) {
    if (textRes != 0) {
        textView.setText(textRes)
    }
}

@BindingAdapter("android:src")
fun setImgSrcUri(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url).into(imageView)
}

@BindingAdapter("android:src")
fun setImgSrcId(imageView: ImageView, resid: Int) {
    if (resid != 0) {
        imageView.setImageDrawable(imageView.context.getDrawable(resid))
    }
}