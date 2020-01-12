package com.iskorsukov.historyaround.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class DrawableMatcherUtils {
    companion object {
        fun withDrawable(drawable: Drawable): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    description?.appendText("View with drawable $drawable")
                }

                override fun matchesSafely(item: View?): Boolean {
                    return when (item) {
                        is ImageView -> getBitmap(item.drawable).sameAs(getBitmap(drawable))
                        is ImageButton -> getBitmap(item.drawable).sameAs(getBitmap(drawable))
                        else -> false
                    }
                }

                private fun getBitmap(drawable: Drawable): Bitmap {
                    val bitmap = Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
                    drawable.draw(canvas)
                    return bitmap
                }

            }
        }
    }
}