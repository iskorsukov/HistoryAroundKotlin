package com.iskorsukov.historyaround.presentation.view.common.markdown

import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.RenderProps
import io.noties.markwon.core.spans.LastLineSpacingSpan
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler

class ParagraphTagHandler: SimpleTagHandler() {
    override fun supportedTags(): MutableCollection<String> {
        return mutableListOf("p")
    }

    override fun getSpans(
        configuration: MarkwonConfiguration,
        renderProps: RenderProps,
        tag: HtmlTag
    ): Any {
        return LastLineSpacingSpan(36)
    }

}