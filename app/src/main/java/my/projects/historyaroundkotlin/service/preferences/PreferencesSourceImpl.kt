package my.projects.historyaroundkotlin.service.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import my.projects.historyaroundkotlin.R
import javax.inject.Inject

class PreferencesSourceImpl @Inject constructor(private val context: Context): PreferencesSource {

    private val defaultRadiusString = context.getString(R.string.prefs_radius_default_value)
    private val radiusKey = context.getString(R.string.prefs_radius_key)

    private val radiusSubject: BehaviorSubject<Int> by lazy {
        BehaviorSubject.create<Int>().also {
            val radius = PreferenceManager.getDefaultSharedPreferences(context).getString(radiusKey, defaultRadiusString)?.toInt() ?: defaultRadiusString.toInt()
            it.onNext(radius)
        }
    }

    override fun getRadiusPreference(): Observable<Int> {
        return radiusSubject
    }

    override fun pushRadiusValueChanged(radius: Int) {
        radiusSubject.onNext(radius)
    }
}