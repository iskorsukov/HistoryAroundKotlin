package my.projects.historyaroundkotlin.presentation.view.preferences

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import my.projects.historyaroundkotlin.HistoryAroundApp
import my.projects.historyaroundkotlin.R

class PreferencesFragment: PreferenceFragmentCompat() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navControllerSource = (requireContext().applicationContext as HistoryAroundApp).appComponent.navControllerSource()
        val navController = navControllerSource.navController(this)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate(PreferencesFragmentDirections.actionPreferencesFragmentToLoadingLocationFragment())
            }
        })
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        configureRadiusSummary()
    }

    private fun configureRadiusSummary() {
        val radiusPreference: ListPreference? = findPreference(getString(R.string.prefs_radius_key))
        radiusPreference?.apply {
            summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
                "${preference.value} meters"
            }
        }
    }
}