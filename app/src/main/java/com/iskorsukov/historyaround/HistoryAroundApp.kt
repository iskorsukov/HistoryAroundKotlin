package com.iskorsukov.historyaround

import android.app.Application
import com.iskorsukov.historyaround.injection.AppComponent
import com.iskorsukov.historyaround.injection.AppModule
import com.iskorsukov.historyaround.injection.DaggerAppComponent
import com.iskorsukov.historyaround.presentation.view.common.markdown.ParagraphTagHandler
import io.noties.markwon.*
import io.noties.markwon.core.spans.LastLineSpacingSpan
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import org.commonmark.node.ListItem
import org.commonmark.node.Paragraph

class HistoryAroundApp : Application() {

    lateinit var appComponent: AppComponent

    lateinit var markwon: Markwon

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        markwon = Markwon.builder(this)
            .usePlugin(HtmlPlugin.create().addHandler(ParagraphTagHandler()))
            .build()
    }
}