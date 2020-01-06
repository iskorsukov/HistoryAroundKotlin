package my.projects.historyaroundkotlin.presentation.view.preferences

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import my.projects.historyaroundkotlin.HistoryAroundApp
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.service.preferences.PreferencesSource

class PreferencesFragment: PreferenceFragmentCompat() {

    private lateinit var preferencesSource: PreferencesSource

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == getString(R.string.prefs_radius_key)) {
                sharedPreferences.getString(key, null)?.apply {
                    preferencesSource.pushRadiusValueChanged(this.toInt())
                }
            } else if (key == getString(R.string.prefs_lang_code_key)) {
                sharedPreferences.getString(key, null)?.apply {
                    preferencesSource.pushLanguageCodeChanged(this)
                }
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        configureRadiusSummary()
        configureLanguageSummary()
    }

    private fun configureRadiusSummary() {
        val radiusPreference: ListPreference? = findPreference(getString(R.string.prefs_radius_key))
        radiusPreference?.apply {
            summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
                "${preference.value} meters"
            }
        }
    }

    private fun configureLanguageSummary() {
        val languagePreference: ListPreference? = findPreference(getString(R.string.prefs_lang_code_key))
        languagePreference?.apply {
            summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
                if (preference.value.isEmpty()) {
                    getString(R.string.prefs_lang_code_default_summary)
                } else {
                    val codesArray = context.resources.getStringArray(R.array.language_codes)
                    val languagesArray = context.resources.getStringArray(R.array.language_names)
                    languagesArray[codesArray.indexOf(preference.value)]
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreferencesSource()
    }

    private fun initPreferencesSource() {
        preferencesSource = (context!!.applicationContext as HistoryAroundApp).appComponent.preferencesSource()
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}