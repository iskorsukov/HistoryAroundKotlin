package com.iskorsukov.historyaround.service.preferences

import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import kotlinx.coroutines.flow.Flow

interface PreferencesSource {
    fun getPreferencesFlow(): Flow<PreferencesBundle>
    fun pushRadiusValueChanged(radius: Int)
    fun pushLanguageCodeChanged(languageCode: String)
}