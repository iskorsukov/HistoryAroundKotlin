package com.iskorsukov.historyaround.service.preferences

import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PreferencesSourceImpl @Inject constructor(private val preferencesStorage: PreferencesStorage): PreferencesSource {

    private val radiusFlow: MutableStateFlow<Int> by lazy {
        MutableStateFlow(preferencesStorage.getRadiusPreference())
    }

    private val languageCodeFlow: MutableStateFlow<String> by lazy {
        MutableStateFlow(preferencesStorage.getLanguagePreference())
    }

    override fun getPreferencesFlow(): Flow<PreferencesBundle> {
        return radiusFlow.combine(languageCodeFlow) { radius: Int, languageCode: String ->
            PreferencesBundle(radius, languageCode.ifEmpty { null })
        }
    }

    override fun pushRadiusValueChanged(radius: Int) {
        radiusFlow.value = radius
    }

    override fun pushLanguageCodeChanged(languageCode: String) {
        languageCodeFlow.value = languageCode
    }
}