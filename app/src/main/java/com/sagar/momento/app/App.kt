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
package com.sagar.momento.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import com.sagar.momento.navigation.horizontalTransitionMetadata
import com.sagar.momento.navigation.verticalTransitionMetadata
import com.sagar.momento.presentation.home.HomeGraph
import com.sagar.momento.presentation.onboarding.Onboarding
import com.sagar.momento.presentation.project.ProjectGraph
import com.sagar.momento.presentation.settings.SettingsGraph
import com.sagar.momento.presentation.shared.ChangelogSheet
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.UpdateSheet
import com.sagar.momento.viewmodels.HomeViewModel
import com.sagar.momento.viewmodels.MainAppViewModel
import com.sagar.momento.viewmodels.OnboardingViewModel
import com.sagar.momento.viewmodels.ProjectViewModel
import com.sagar.momento.viewmodels.SettingsViewModel

@Serializable data object Onboarding : NavKey

@Serializable data object HomeGraph : NavKey

@Serializable data object ProjectGraph : NavKey

@Serializable data object SettingsGraph : NavKey

@Composable
fun App() {
    val backStack = rememberNavBackStack(HomeGraph)

    val mainViewModel: MainAppViewModel = koinInject()
    val state by mainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isOnboardingDone) {
        if (state.isOnboardingDone == false) {
            backStack.add(Onboarding)
        }
    }

    MomentoTheme(theme = state.theme) {
        if (state.currentChangelog != null && state.isOnboardingDone == true) {
            ChangelogSheet(
                currentLog = state.currentChangelog!!,
                onDismissRequest = { mainViewModel.dismissChangelog() },
            )
        }

        val updateInfo = state.updateInfo
        if (updateInfo != null && state.isOnboardingDone == true) {
            UpdateSheet(
                updateInfo = updateInfo,
                isDownloading = state.isDownloading,
                downloadProgress = state.downloadProgress,
                updateError = state.updateError,
                onUpdate = { mainViewModel.startUpdate() },
                onDismiss = { mainViewModel.dismissUpdate() },
                onDismissError = { mainViewModel.dismissError() },
            )
        }

        NavDisplay(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
            backStack = backStack,
            entryProvider =
                entryProvider {
                    entry<HomeGraph> {
                        val homeViewModel: HomeViewModel = koinInject()
                        val homeState by homeViewModel.state.collectAsStateWithLifecycle()

                        HomeGraph(
                            state = homeState,
                            onAction = homeViewModel::onAction,
                            onNavigateToSettings = { backStack.add(SettingsGraph) },
                            onNavigateToProject = { backStack.add(ProjectGraph) },
                        )
                    }

                    entry<Onboarding>(metadata = verticalTransitionMetadata()) {
                        val onboardingViewModel: OnboardingViewModel = koinViewModel()
                        val onboardingState by
                            onboardingViewModel.state.collectAsStateWithLifecycle()

                        Onboarding(
                            state = onboardingState,
                            onAction = onboardingViewModel::onAction,
                            onNavigateBack = {
                                if (backStack.size != 1) backStack.removeLastOrNull()
                            },
                        )
                    }

                    entry<ProjectGraph>(metadata = horizontalTransitionMetadata()) {
                        val projectViewModel: ProjectViewModel = koinInject()
                        val projectState by projectViewModel.state.collectAsStateWithLifecycle()
                        val exoPlayer by projectViewModel.exoPlayer.collectAsStateWithLifecycle()

                        ProjectGraph(
                            state = projectState,
                            exoPlayer = exoPlayer,
                            onAction = projectViewModel::onAction,
                            onNavigateBack = {
                                if (backStack.size != 1) backStack.removeLastOrNull()
                            },
                        )
                    }

                    entry<SettingsGraph>(metadata = horizontalTransitionMetadata()) {
                        val settingsViewModel: SettingsViewModel = koinViewModel()
                        val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()

                        SettingsGraph(
                            state = settingsState,
                            onAction = settingsViewModel::onAction,
                            onNavigateBack = {
                                if (backStack.size != 1) backStack.removeLastOrNull()
                            },
                            onNavigateToOnboarding = { backStack.add(Onboarding) },
                        )
                    }

                },
        )
    }
}
