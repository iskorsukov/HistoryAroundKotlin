package my.projects.historyaroundkotlin.service.preferences

import io.reactivex.Observable

interface PreferencesSource {
    fun getRadiusPreference(): Observable<Int>
    fun pushRadiusValueChanged(radius: Int)
}