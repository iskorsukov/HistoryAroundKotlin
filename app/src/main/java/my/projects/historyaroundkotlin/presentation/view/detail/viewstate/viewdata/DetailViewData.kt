package my.projects.historyaroundkotlin.presentation.view.detail.viewstate.viewdata

import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData

data class DetailViewData(val item: ArticleDetails, val isFavorite: Boolean): ViewData