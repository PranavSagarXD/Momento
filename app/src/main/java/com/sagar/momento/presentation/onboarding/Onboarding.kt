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
package com.sagar.momento.presentation.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.presentation.onboarding.ui.components.AutoAnimation
import com.sagar.momento.presentation.onboarding.ui.components.CameraAnimation
import com.sagar.momento.presentation.onboarding.ui.components.PrivateAnimation
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.flexFontRounded

@Composable
fun Onboarding(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 3 }

    Scaffold { paddingValues ->
        HorizontalPager(
            state = pagerState,
            pageSpacing = 32.dp,
            contentPadding =
                PaddingValues(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr) + 32.dp,
                    end = paddingValues.calculateRightPadding(LayoutDirection.Ltr) + 32.dp,
                ),
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                0 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CameraAnimation()

                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = "Create Montages Easily",
                                style =
                                    MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = flexFontRounded()
                                    ),
                                textAlign = TextAlign.Center,
                            )

                            Text(
                                text = "Get reminded to take a picture everyday.",
                                textAlign = TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
                            ) {
                                Text(text = "Next")
                            }
                        }
                    }
                }

                1 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AutoAnimation()

                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = "Convenient and Automatic",
                                style =
                                    MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = flexFontRounded()
                                    ),
                                textAlign = TextAlign.Center,
                            )

                            Text(
                                text = "Don't even think about storage or editing.",
                                textAlign = TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = { scope.launch { pagerState.animateScrollToPage(2) } }
                            ) {
                                Text(text = "Next")
                            }
                        }
                    }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            PrivateAnimation()

                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = "Permissionless and Private",
                                style =
                                    MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = flexFontRounded()
                                    ),
                                textAlign = TextAlign.Center,
                            )

                            Text(
                                text =
                                    "No invasive permissions, Everything is processed on device!",
                                textAlign = TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = {
                                    onAction(OnboardingAction.OnOnboardingDone)
                                    onNavigateBack()
                                }
                            ) {
                                Text(text = "Done")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    MomentoTheme(theme = Theme(appTheme = AppTheme.DARK)) {
        Onboarding(state = OnboardingState(), onAction = {}, onNavigateBack = {})
    }
}
