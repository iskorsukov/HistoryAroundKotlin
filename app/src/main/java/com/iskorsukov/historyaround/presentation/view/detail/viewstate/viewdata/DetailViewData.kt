package com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata

import com.iskorsukov.historyaround.model.detail.ArticleDetails
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewdata.ViewData

data class DetailViewData(val item: ArticleDetails, val isFavorite: Boolean): ViewData