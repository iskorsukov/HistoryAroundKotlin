package my.projects.historyaroundkotlin.presentation.viewstate.start.permission

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.io.Serializable

data class PermissionRationale(@DrawableRes val iconRes: Int, @StringRes val titleRes: Int, @StringRes val messageRes: Int): Serializable