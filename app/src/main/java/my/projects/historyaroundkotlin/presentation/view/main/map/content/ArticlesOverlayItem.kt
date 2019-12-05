package my.projects.historyaroundkotlin.presentation.view.main.map.content

import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticleItemViewData
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.overlay.OverlayItem

class ArticlesOverlayItem(aTitle: String?, aSnippet: String?, aGeoPoint: IGeoPoint?, val articleItems: List<ArticleItemViewData>) :
    OverlayItem(aTitle, aSnippet, aGeoPoint)