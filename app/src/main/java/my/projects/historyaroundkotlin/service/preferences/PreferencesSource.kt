package my.projects.historyaroundkotlin.service.preferences

import io.reactivex.Observable
import my.projects.historyaroundkotlin.model.preferences.PreferencesBundle

interface PreferencesSource {
    fun getPreferences(): Observable<PreferencesBundle>
    fun pushRadiusValueChanged(radius: Int)
    fun pushLanguageCodeChanged(languageCode: String)
}