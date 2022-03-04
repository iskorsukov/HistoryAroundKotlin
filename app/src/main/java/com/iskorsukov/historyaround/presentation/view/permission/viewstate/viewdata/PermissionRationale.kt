package com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.io.Serializable

data class PermissionRationale(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int): Serializable