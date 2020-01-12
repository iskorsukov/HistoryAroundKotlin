package com.iskorsukov.historyaround.service.preferences

import io.reactivex.Observable
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle

interface PreferencesSource {
    fun getPreferences(): Observable<PreferencesBundle>
    fun pushRadiusValueChanged(radius: Int)
    fun pushLanguageCodeChanged(languageCode: String)
}