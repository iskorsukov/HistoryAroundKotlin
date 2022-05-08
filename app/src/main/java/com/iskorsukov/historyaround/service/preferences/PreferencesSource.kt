package com.iskorsukov.historyaround.service.preferences

import android.content.SharedPreferences
import com.iskorsukov.historyaround.model.preferences.PreferencesBundle
import kotlinx.coroutines.flow.Flow

interface PreferencesSource: SharedPreferences.OnSharedPreferenceChangeListener {
    fun getPreferencesFlow(): Flow<PreferencesBundle>
}