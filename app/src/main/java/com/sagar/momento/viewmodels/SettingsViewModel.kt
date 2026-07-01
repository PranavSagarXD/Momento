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
package com.sagar.momento.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import com.sagar.momento.core.interfaces.SettingsPrefs
import com.sagar.momento.data.ChangelogManager
import com.sagar.momento.presentation.settings.SettingsAction
import com.sagar.momento.presentation.settings.SettingsState

@KoinViewModel
class SettingsViewModel(
    private val datastore: SettingsPrefs,
    private val changelogManager: ChangelogManager,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state =
        _state
            .asStateFlow()
            .onStart {
                observeDatastore()
                getChangeLogs()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SettingsState(),
            )

    fun onAction(action: SettingsAction) =
        viewModelScope.launch {
            when (action) {
                is OnAmoledSwitch -> datastore.updateAmoledPref(action.amoled)
                is OnFontChange -> datastore.updateFonts(action.fonts)
                is OnMaterialThemeToggle -> datastore.updateMaterialTheme(action.pref)

                is OnPaletteChange -> datastore.updatePaletteStyle(action.style)
                is OnSeedColorChange -> datastore.updateSeedColor(action.color)
                is OnThemeSwitch -> datastore.updateAppThemePref(action.appTheme)
            }
        }

    private fun getChangeLogs() {
        viewModelScope.launch {
            _state.update { it.copy(changelog = changelogManager.changelogs.first()) }
        }
    }

    private fun observeDatastore() =
        viewModelScope.launch {
            datastore
                .getAmoledPrefFlow()
                .onEach { pref ->
                    _state.update { it.copy(theme = it.theme.copy(isAmoled = pref)) }
                }
                .launchIn(this)

            datastore
                .getFontFlow()
                .onEach { pref -> _state.update { it.copy(theme = it.theme.copy(font = pref)) } }
                .launchIn(this)

            datastore
                .getMaterialYouFlow()
                .onEach { pref ->
                    _state.update { it.copy(theme = it.theme.copy(isMaterialYou = pref)) }
                }
                .launchIn(this)

            datastore
                .getPaletteStyle()
                .onEach { pref ->
                    _state.update { it.copy(theme = it.theme.copy(paletteStyle = pref)) }
                }
                .launchIn(this)

            datastore
                .getSeedColorFlow()
                .onEach { pref ->
                    _state.update { it.copy(theme = it.theme.copy(seedColor = pref)) }
                }
                .launchIn(this)

            datastore
                .getAppThemePrefFlow()
                .onEach { pref ->
                    _state.update { it.copy(theme = it.theme.copy(appTheme = pref)) }
                }
                .launchIn(this)
        }
}
