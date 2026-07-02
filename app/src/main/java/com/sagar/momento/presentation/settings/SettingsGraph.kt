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
package com.sagar.momento.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import com.sagar.momento.navigation.horizontalTransitionMetadata
import com.sagar.momento.presentation.settings.ui.sections.About
import com.sagar.momento.presentation.settings.ui.sections.Changelog
import com.sagar.momento.presentation.settings.ui.sections.LookAndFeel
import com.sagar.momento.presentation.settings.ui.sections.Root

@Serializable
private sealed interface Routes : NavKey {
    @Serializable data object Root : Routes

    @Serializable data object About : Routes

    @Serializable data object LookAndFeel : Routes

    @Serializable data object Changelog : Routes
}

@Composable
fun SettingsGraph(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(Routes.Root)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider =
            entryProvider {
                entry<Routes.Root> {
                    Root(
                        onNavigateBack = onNavigateBack,
                        onNavigateToOnboarding = onNavigateToOnboarding,
                        onNavigateToLookAndFeel = { backStack.add(Routes.LookAndFeel) },
                        onNavigateToChangelog = { backStack.add(Routes.Changelog) },
                        onNavigateToAppInfo = { backStack.add(Routes.About) },
                        currentVersion = state.changelog.firstOrNull()?.version ?: "",
                    )
                }

                entry<Routes.LookAndFeel>(metadata = horizontalTransitionMetadata()) {
                    LookAndFeel(
                        state = state,
                        onAction = onAction,
                        onNavigateBack = { if (backStack.size != 1) backStack.removeLastOrNull() },
                    )
                }

                entry<Routes.Changelog>(metadata = horizontalTransitionMetadata()) {
                    Changelog(
                        changelog = state.changelog,
                        onNavigateBack = { if (backStack.size != 1) backStack.removeLastOrNull() },
                    )
                }

                entry<Routes.About>(metadata = horizontalTransitionMetadata()) {
                    About(
                        versionName = state.changelog.firstOrNull()?.version ?: "",
                        onNavigateBack = { if (backStack.size != 1) backStack.removeLastOrNull() },
                    )
                }
            },
    )
}
