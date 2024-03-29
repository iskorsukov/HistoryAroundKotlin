package com.iskorsukov.historyaround.service.preferences

import android.content.Context
import android.content.SharedPreferences
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val sharedPreferences: SharedPreferences): PreferencesSource {

    private val defaultRadiusString = context.getString(R.string.prefs_radius_default_value)
    private val radiusKey = context.getString(R.string.prefs_radius_key)

    private val languageCodeKey = context.getString(R.string.prefs_lang_code_key)

    private val radiusFlow: MutableStateFlow<Int> by lazy {
        MutableStateFlow(getRadiusPreference())
    }

    private val languageCodeFlow: MutableStateFlow<String> by lazy {
        MutableStateFlow(getLanguagePreference())
    }

    override fun getPreferencesFlow(): Flow<PreferencesBundle> {
        return radiusFlow.combine(languageCodeFlow) { radius: Int, languageCode: String ->
            PreferencesBundle(radius, languageCode.ifEmpty { null })
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == radiusKey) {
            radiusFlow.value = getRadiusPreference()
        } else if (key == languageCodeKey) {
            languageCodeFlow.value = getLanguagePreference()
        }
    }

    private fun getRadiusPreference(): Int {
        return sharedPreferences.getString(radiusKey, defaultRadiusString)?.toInt() ?: defaultRadiusString.toInt()
    }

    private fun getLanguagePreference(): String {
        return sharedPreferences.getString(languageCodeKey, Locale.getDefault().language) ?: Locale.getDefault().language
    }
}