package com.iskorsukov.historyaround.service.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import javax.inject.Inject

class PreferencesSourceImpl @Inject constructor(private val context: Context): PreferencesSource {

    private val defaultRadiusString = context.getString(R.string.prefs_radius_default_value)
    private val radiusKey = context.getString(R.string.prefs_radius_key)

    private val defaultLanguageString = context.getString(R.string.prefs_lang_code_default_value)
    private val languageCodeKey = context.getString(R.string.prefs_lang_code_key)

    private val radiusSubject: BehaviorSubject<Int> by lazy {
        BehaviorSubject.create<Int>().also {
            val radius = PreferenceManager.getDefaultSharedPreferences(context).getString(radiusKey, defaultRadiusString)?.toInt() ?: defaultRadiusString.toInt()
            it.onNext(radius)
        }
    }

    private val languageCodeSubject: BehaviorSubject<String> by lazy {
        BehaviorSubject.create<String>().also {
            val languageCode = PreferenceManager.getDefaultSharedPreferences(context).getString(languageCodeKey, defaultLanguageString) ?: defaultLanguageString
            it.onNext(languageCode)
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