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

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import com.sagar.momento.BuildConfig
import com.sagar.momento.app.MainAppState
import com.sagar.momento.core.interfaces.SettingsPrefs
import com.sagar.momento.data.ChangelogManager
import com.sagar.momento.update.AppUpdater
import com.sagar.momento.update.UpdateChecker
import com.sagar.momento.update.UpdateInfo

@KoinViewModel
class MainAppViewModel(
    private val context: Context,
    private val datastore: SettingsPrefs,
    private val changelogManager: ChangelogManager,
) : ViewModel() {
    private var observerJob: Job? = null
    private val updateChecker = UpdateChecker()
    private val appUpdater = AppUpdater(context)

    private val _state = MutableStateFlow(MainAppState())
    val state =
        _state
            .asStateFlow()
            .onStart {
                checkChangelog()
                checkForUpdates()
                observeData()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MainAppState(),
            )

    fun dismissChangelog() {
        _state.update { it.copy(currentChangelog = null) }
    }

    fun dismissUpdate() {
        _state.update { it.copy(updateInfo = null) }
    }

    fun startUpdate() {
        val info = _state.value.updateInfo ?: return
        _state.update { it.copy(isDownloading = true, downloadProgress = 0f) }

        viewModelScope.launch {
            val success = appUpdater.downloadAndInstall(info.downloadUrl) { progress ->
                _state.update { it.copy(downloadProgress = progress) }
            }
            if (success) {
                _state.update { it.copy(updateInfo = null, isDownloading = false, downloadProgress = 0f) }
            } else {
                _state.update { it.copy(isDownloading = false, downloadProgress = 0f) }
            }
        }
    }

    private fun checkForUpdates() {
        viewModelScope.launch {
            val info = updateChecker.check()
            if (info != null) {
                _state.update { it.copy(updateInfo = info) }
            }
        }
    }

    private fun checkChangelog() {
        viewModelScope.launch {
            val changeLogs = changelogManager.changelogs.first()
            val lastShownChangelog = datastore.getLastChangelogShown().first()

            if (lastShownChangelog != BuildConfig.VERSION_NAME) {
                _state.update { it.copy(currentChangelog = changeLogs.firstOrNull()) }
                datastore.updateLastChangelogShown(BuildConfig.VERSION_NAME)
            }
        }
    }

    private fun observeData() {
        observerJob?.cancel()
        observerJob =
            viewModelScope.launch {
                datastore
                    .getMaterialYouFlow()
                    .onEach { pref ->
                        _state.update { it.copy(theme = it.theme.copy(isMaterialYou = pref)) }
                    }
                    .launchIn(this)

                combine(
                        datastore.getPaletteStyle(),
                        datastore.getFontFlow(),
                        datastore.getSeedColorFlow(),
                        datastore.getAppThemePrefFlow(),
                        datastore.getAmoledPrefFlow(),
                    ) { style, font, seedColor, appTheme, amoled ->
                        _state.update {
                            it.copy(
                                theme =
                                    it.theme.copy(
                                        paletteStyle = style,
                                        font = font,
                                        seedColor = seedColor,
                                        appTheme = appTheme,
                                        isAmoled = amoled,
                                    )
                            )
                        }
                    }
                    .launchIn(this)

                datastore
                    .getOnboardingDoneFlow()
                    .onEach { pref -> _state.update { it.copy(isOnboardingDone = pref) } }
                    .launchIn(this)
            }
    }
}
