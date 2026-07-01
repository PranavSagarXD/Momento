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
package com.sagar.momento.core.interfaces

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.PaletteStyle

interface SettingsPrefs {
    suspend fun resetAppTheme()

    fun getAppThemePrefFlow(): Flow<AppTheme>

    suspend fun updateAppThemePref(pref: AppTheme)

    fun getSeedColorFlow(): Flow<Color>

    suspend fun updateSeedColor(color: Color)

    fun getAmoledPrefFlow(): Flow<Boolean>

    suspend fun updateAmoledPref(amoled: Boolean)

    fun getPaletteStyle(): Flow<PaletteStyle>

    suspend fun updatePaletteStyle(style: PaletteStyle)

    fun getOnboardingDoneFlow(): Flow<Boolean>

    suspend fun updateOnboardingDone(done: Boolean)

    fun getMaterialYouFlow(): Flow<Boolean>

    suspend fun updateMaterialTheme(pref: Boolean)

    fun getFontFlow(): Flow<Fonts>

    suspend fun updateFonts(font: Fonts)

    fun getLastChangelogShown(): Flow<String>

    suspend fun updateLastChangelogShown(version: String)
}
