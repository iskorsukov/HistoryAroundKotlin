package com.iskorsukov.historyaround.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcherUtils {

    companion object {
        fun onViewInPosition(position: Int, targetRecyclerViewId: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {
                    description?.appendText("View in position $position in target RecyclerView")
                }

                override fun matchesSafely(item: View?): Boolean {
                    val recyclerView: RecyclerView? = item?.rootView?.findViewById(targetRecyclerViewId)
                    return recyclerView?.run {
                        val recyclerItemView: View? = this.findViewHolderForAdapterPosition(position)?.itemView
                        recyclerItemView == item
                    } ?: false
                }
            }
        }
    }
}