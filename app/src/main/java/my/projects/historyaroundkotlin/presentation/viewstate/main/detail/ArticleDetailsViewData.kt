package my.projects.historyaroundkotlin.presentation.viewstate.main.detail

import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import java.io.Serializable

class ArticleDetailsViewData(val item: ArticleDetails, val isFavorite: Boolean): Serializable