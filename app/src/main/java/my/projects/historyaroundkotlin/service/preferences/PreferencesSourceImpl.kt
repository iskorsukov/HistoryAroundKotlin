package my.projects.historyaroundkotlin.service.preferences

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.rxkotlin.zipWith
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.MaybeSubject
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.model.preferences.PreferencesBundle
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
        return radiusSubject.withLatestFrom(languageCodeSubject) { radius: Int, languageCode: String ->
            PreferencesBundle(radius, if (languageCode.isEmpty()) null else languageCode)
        }
    }

    override fun pushRadiusValueChanged(radius: Int) {
        radiusSubject.onNext(radius)
    }

    override fun pushLanguageCodeChanged(languageCode: String) {
        languageCodeSubject.onNext(languageCode)
    }
}