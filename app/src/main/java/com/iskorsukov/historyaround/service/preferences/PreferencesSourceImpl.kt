package com.iskorsukov.historyaround.service.preferences

import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class PreferencesSourceImpl @Inject constructor(private val preferencesStorage: PreferencesStorage): PreferencesSource {

    private val radiusSubject: BehaviorSubject<Int> by lazy {
        BehaviorSubject.create<Int>().also {
            it.onNext(preferencesStorage.getRadiusPreference())
        }
    }

    private val languageCodeSubject: BehaviorSubject<String> by lazy {
        BehaviorSubject.create<String>().also {
            it.onNext(preferencesStorage.getLanguagePreference())
        }
    }

    override fun getPreferences(): Observable<PreferencesBundle> {
        return Observable.combineLatest(radiusSubject, languageCodeSubject, BiFunction {radius: Int, languageCode: String ->
            PreferencesBundle(radius, if (languageCode.isEmpty()) null else languageCode)
        })
    }

    override fun pushRadiusValueChanged(radius: Int) {
        radiusSubject.onNext(radius)
    }

    override fun pushLanguageCodeChanged(languageCode: String) {
        languageCodeSubject.onNext(languageCode)
    }
}