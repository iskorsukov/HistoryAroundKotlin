package com.iskorsukov.historyaround

import android.app.Application
import com.iskorsukov.historyaround.presentation.view.common.markdown.ParagraphTagHandler
import dagger.hilt.android.HiltAndroidApp
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

@HiltAndroidApp
class HistoryAroundApp : Application() {

    lateinit var markwon: Markwon

    override fun onCreate() {
        super.onCreate()
        markwon = Markwon.builder(this)
            .usePlugin(HtmlPlugin.create().addHandler(ParagraphTagHandler()))
            .build()
    }
}