package com.iskorsukov.historyaround.service.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import com.iskorsukov.historyaround.R
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PreferencesSourceTest {

    private val context: Context = mockk(relaxed = true)

    private val sharedPreferences: SharedPreferences = mockk()

    private val defaultRadiusString = "250"
    private val radiusKey = "pref_radius"

    private val defaultLanguageCode = ""
    private val languageCodeKey = "pref_lang_code"

    private lateinit var preferencesSource: PreferencesSource

    @Before
    fun mockData() {
        mockContextStrings()
        mockSharedPreferences_Defaults()

        preferencesSource = PreferencesSourceImpl(context, sharedPreferences)
    }

    @Test
    fun getPreferencesFlow_Defaults() = runBlocking {
        val preferences = preferencesSource.getPreferencesFlow().first()

        assertThat(preferences.radius).isEqualTo(defaultRadiusString.toInt())
        assertThat(preferences.languageCode).isNull()
    }

    @Test
    fun getPreferencesFlow_AfterRadiusChanged() = runBlocking {
        val radius = "500"

        mockRadiusPreference(radius)
        preferencesSource.onSharedPreferenceChanged(sharedPreferences, radiusKey)
        val preferences = preferencesSource.getPreferencesFlow().first()

        assertThat(preferences.radius).isEqualTo(radius.toInt())
        assertThat(preferences.languageCode).isNull()
    }

    @Test
    fun getPreferencesFlow_AfterLanguageCodeChanged() = runBlocking {
        val languageCode = "en"

        mockLanguageCodePreference(languageCode)
        preferencesSource.onSharedPreferenceChanged(sharedPreferences, languageCodeKey)
        val preferences = preferencesSource.getPreferencesFlow().first()

        assertThat(preferences.radius).isEqualTo(defaultRadiusString.toInt())
        assertThat(preferences.languageCode).isEqualTo(languageCode)
    }

    @Test
    fun getPreferencesFlow_AfterBothChanged() = runBlocking {
        val languageCode = "en"
        val radius = "500"

        mockLanguageCodePreference(languageCode)
        mockRadiusPreference(radius)
        preferencesSource.onSharedPreferenceChanged(sharedPreferences, languageCodeKey)
        preferencesSource.onSharedPreferenceChanged(sharedPreferences, radiusKey)
        val preferences = preferencesSource.getPreferencesFlow().first()

        assertThat(preferences.radius).isEqualTo(radius.toInt())
        assertThat(preferences.languageCode).isEqualTo(languageCode)
    }

    private fun mockSharedPreferences_Defaults() {
        every {
            sharedPreferences.getString(radiusKey, any())
        } returns defaultRadiusString
        every {
            sharedPreferences.getString(languageCodeKey, any())
        } returns defaultLanguageCode
    }

    private fun mockRadiusPreference(radius: String) {
        every {
            sharedPreferences.getString(radiusKey, any())
        } returns radius
    }

    private fun mockLanguageCodePreference(languageCode: String) {
        every {
            sharedPreferences.getString(languageCodeKey, any())
        } returns languageCode
    }

    private fun mockContextStrings() {
        every { context.getString(R.string.prefs_radius_default_value) } returns defaultRadiusString
        every { context.getString(R.string.prefs_radius_key) } returns radiusKey
        every { context.getString(R.string.prefs_lang_code_default_value) } returns defaultLanguageCode
        every { context.getString(R.string.prefs_lang_code_key) } returns languageCodeKey
    }
}