/*
 * Copyright (C) 2026  PranavSagarXD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.sagar.momento.data.datastore

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.PaletteStyle
import com.sagar.momento.core.interfaces.SettingsPrefs

class SettingsPrefsImpl(private val dataStore: DataStore<Preferences>) : SettingsPrefs {

    override suspend fun resetAppTheme() {
        dataStore.edit { prefs ->
            prefs.remove(seedColor)
            prefs.remove(appTheme)
            prefs.remove(amoledPref)
            prefs.remove(paletteStyle)
            prefs.remove(materialTheme)
        }
    }

    companion object {
        private val seedColor = intPreferencesKey("seed_color")
        private val appTheme = stringPreferencesKey("app_theme")
        private val amoledPref = booleanPreferencesKey("with_amoled")
        private val paletteStyle = stringPreferencesKey("palette_style")
        private val materialTheme = booleanPreferencesKey("material_theme")
        private val onboardingDone = booleanPreferencesKey("onboarding_done")
        private val selectedFont = stringPreferencesKey("font")
        private val lastChangelogShownKey = stringPreferencesKey("last_changelog_shown")
        private val skippedUpdateVersionKey = stringPreferencesKey("skipped_update_version")
        private val lastUpdateCheckTimeKey = longPreferencesKey("last_update_check_time")
    }

    override fun getAppThemePrefFlow(): Flow<AppTheme> =
        dataStore.data.map { preferences ->
            val theme = preferences[appTheme] ?: AppTheme.SYSTEM.name
            AppTheme.valueOf(theme)
        }

    override suspend fun updateAppThemePref(pref: AppTheme) {
        dataStore.edit { it[appTheme] = pref.name }
    }

    override fun getSeedColorFlow(): Flow<Color> =
        dataStore.data.map { preferences -> Color(preferences[seedColor] ?: Color.White.toArgb()) }

    override suspend fun updateSeedColor(color: Color) {
        dataStore.edit { settings -> settings[seedColor] = color.toArgb() }
    }

    override fun getAmoledPrefFlow(): Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[amoledPref] == true }

    override suspend fun updateAmoledPref(amoled: Boolean) {
        dataStore.edit { settings -> settings[amoledPref] = amoled }
    }

    override fun getPaletteStyle(): Flow<PaletteStyle> =
        dataStore.data.map { preferences ->
            try {
                PaletteStyle.valueOf(preferences[paletteStyle] ?: PaletteStyle.TONALSPOT.name)
            } catch (e: Exception) {
                Log.wtf("OtherPreferencesImpl", "Error getting palette style: ${e.message}")
                PaletteStyle.TONALSPOT
            }
        }

    override suspend fun updatePaletteStyle(style: PaletteStyle) {
        dataStore.edit { settings -> settings[paletteStyle] = style.name }
    }

    override fun getOnboardingDoneFlow(): Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[onboardingDone] == true }

    override suspend fun updateOnboardingDone(done: Boolean) {
        dataStore.edit { settings -> settings[onboardingDone] = done }
    }

    override fun getMaterialYouFlow(): Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[materialTheme] == true }

    override suspend fun updateMaterialTheme(pref: Boolean) {
        dataStore.edit { settings -> settings[materialTheme] = pref }
    }

    override fun getFontFlow(): Flow<Fonts> =
        dataStore.data.map { prefs ->
            val font = prefs[selectedFont] ?: Fonts.FIGTREE.name
            Fonts.valueOf(font)
        }

    override suspend fun updateFonts(font: Fonts) {
        dataStore.edit { settings -> settings[selectedFont] = font.name }
    }

    override fun getLastChangelogShown(): Flow<String> =
        dataStore.data.map { prefs -> prefs[lastChangelogShownKey] ?: "" }

    override suspend fun updateLastChangelogShown(version: String) {
        dataStore.edit { settings -> settings[lastChangelogShownKey] = version }
    }

    override fun getSkippedUpdateVersion(): Flow<String> =
        dataStore.data.map { prefs -> prefs[skippedUpdateVersionKey] ?: "" }

    override suspend fun setSkippedUpdateVersion(version: String) {
        dataStore.edit { settings -> settings[skippedUpdateVersionKey] = version }
    }

    override fun getLastUpdateCheckTime(): Flow<Long> =
        dataStore.data.map { prefs -> prefs[lastUpdateCheckTimeKey] ?: 0L }

    override suspend fun setLastUpdateCheckTime(timestamp: Long) {
        dataStore.edit { settings -> settings[lastUpdateCheckTimeKey] = timestamp }
    }
}
