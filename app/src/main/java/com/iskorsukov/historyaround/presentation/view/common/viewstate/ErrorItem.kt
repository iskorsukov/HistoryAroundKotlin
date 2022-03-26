package com.iskorsukov.historyaround.presentation.view.common.viewstate

import androidx.annotation.StringRes
import com.iskorsukov.historyaround.R
import java.io.Serializable

abstract class ErrorItem(
    @StringRes val labelRes: Int,
    @StringRes val messageRes: Int? = null,
    @StringRes val actionLabelRes: Int? = RETRY_LABEL,
    @StringRes val cancelLabelRes: Int? = CLOSE_LABEL
): Serializable {
    companion object {
        const val RETRY_LABEL = R.string.retry
        const val CLOSE_LABEL = R.string.close
    }
}