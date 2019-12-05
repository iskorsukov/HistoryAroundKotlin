package my.projects.historyaroundkotlin.service.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import my.projects.historyaroundkotlin.R
import javax.inject.Inject

class PreferencesSourceImpl @Inject constructor(private val context: Context): PreferencesSource {

    private val defaultRadiusString = context.getString(R.string.prefs_radius_default_value)

    override fun getRadiusPreference(): Int {
        val radiusKey = context.getString(R.string.prefs_radius_key)
        return PreferenceManager.getDefaultSharedPreferences(context).getString(radiusKey, defaultRadiusString)?.toInt() ?: defaultRadiusString.toInt()
    }
}