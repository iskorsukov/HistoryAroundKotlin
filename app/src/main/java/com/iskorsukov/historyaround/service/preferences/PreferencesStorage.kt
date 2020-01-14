package com.iskorsukov.historyaround.service.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.mock.Mockable
import javax.inject.Inject

@Mockable
class PreferencesStorage @Inject constructor(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val defaultRadiusString = context.getString(R.string.prefs_radius_default_value)
    private val radiusKey = context.getString(R.string.prefs_radius_key)

    private val defaultLanguageString = context.getString(R.string.prefs_lang_code_default_value)
    private val languageCodeKey = context.getString(R.string.prefs_lang_code_key)

    fun getRadiusPreference(): Int {
        return sharedPreferences.getString(radiusKey, defaultRadiusString)?.toInt() ?: defaultRadiusString.toInt()
    }

    fun getLanguagePreference(): String {
        return sharedPreferences.getString(languageCodeKey, defaultLanguageString) ?: defaultLanguageString
    }
}