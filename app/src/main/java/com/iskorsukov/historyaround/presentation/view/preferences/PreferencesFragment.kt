package com.iskorsukov.historyaround.presentation.view.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.iskorsukov.historyaround.HistoryAroundApp
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.service.preferences.PreferencesSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PreferencesFragment: PreferenceFragmentCompat() {

    @Inject
    lateinit var preferencesSource: PreferencesSource

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
                val codesArray = context.resources.getStringArray(R.array.language_codes)
                val languagesArray = context.resources.getStringArray(R.array.language_names)
                languagesArray[codesArray.indexOf(preference.value)]
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setTitle()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setTitle() {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.setTitle(R.string.preferences_fragment_title)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(preferencesSource)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(preferencesSource)
    }
}