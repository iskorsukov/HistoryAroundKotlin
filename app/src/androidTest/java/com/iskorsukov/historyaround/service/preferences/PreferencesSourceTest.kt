package com.iskorsukov.historyaround.service.preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class PreferencesSourceTest {
    private val preferencesStorage = Mockito.mock(PreferencesStorage::class.java)
    private val preferencesSource = PreferencesSourceImpl(preferencesStorage)

    private val sampleRadius = 500
    private val sampleLanguageCode = "en"

    private fun pushSampleData() {
        Mockito.`when`(preferencesStorage.getLanguagePreference()).thenReturn(sampleLanguageCode)
        Mockito.`when`(preferencesStorage.getRadiusPreference()).thenReturn(sampleRadius)
    }

    @Test
    fun returnsPreferences() {
        pushSampleData()

        val prefs = preferencesSource.getPreferences().firstOrError().blockingGet()

        assertEquals(sampleRadius, prefs.radius)
        assertEquals(sampleLanguageCode, prefs.languageCode)
    }

    @Test
    fun updatesPreferencesWhenOneChanges() {
        pushSampleData()

        preferencesSource.pushRadiusValueChanged(100)

        val prefsRadiusChanged = preferencesSource.getPreferences().firstOrError().blockingGet()
        assertNotEquals(sampleRadius, prefsRadiusChanged.radius)
        assertEquals(sampleLanguageCode, prefsRadiusChanged.languageCode)

        preferencesSource.pushLanguageCodeChanged("ru")
        val prefsLanguageCodeChanged = preferencesSource.getPreferences().firstOrError().blockingGet()
        assertNotEquals(sampleLanguageCode, prefsLanguageCodeChanged.languageCode)
    }
}