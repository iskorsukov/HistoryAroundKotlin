package com.iskorsukov.historyaround.presentation.view.common.appbar

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.absoluteValue

abstract class AppBarStateChangeListener: AppBarLayout.OnOffsetChangedListener {

    private var currentState = State.IDLE

    override fun onOffsetChanged(layout: AppBarLayout?, offset: Int) {
        if (layout == null) return
        if (offset == 0) {
            if (currentState != State.EXPANDED) {
                onStateChange(layout, State.EXPANDED)
            }
            currentState = State.EXPANDED
        } else if (offset.absoluteValue >= layout.totalScrollRange) {
            if (currentState != State.COLLAPSED) {
                onStateChange(layout, State.COLLAPSED)
            }
            currentState = State.COLLAPSED
        } else {
            if (currentState != State.IDLE) {
                onStateChange(layout, State.IDLE)
            }
            currentState = State.IDLE
        }
    }

    abstract fun onStateChange(layout: AppBarLayout, state: State)

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}