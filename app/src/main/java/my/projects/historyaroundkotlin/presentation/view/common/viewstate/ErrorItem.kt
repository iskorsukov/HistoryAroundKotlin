package my.projects.historyaroundkotlin.presentation.view.common.viewstate

import androidx.annotation.StringRes
import java.io.Serializable

abstract class ErrorItem(@StringRes val labelRes: Int, @StringRes val messageRes: Int): Serializable