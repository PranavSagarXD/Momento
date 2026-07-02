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
package com.sagar.momento.presentation.settings.ui.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.detachedItemShape
import com.sagar.momento.presentation.shared.endItemShape
import com.sagar.momento.presentation.shared.flexFontEmphasis
import com.sagar.momento.presentation.shared.leadingItemShape
import com.sagar.momento.presentation.shared.listItemColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Root(
    onNavigateBack: () -> Unit,
    onNavigateToLookAndFeel: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToChangelog: () -> Unit,
    onNavigateToAppInfo: () -> Unit,
    currentVersion: String,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumFlexibleTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.settings), fontFamily = flexFontEmphasis())
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Navigate Back",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding =
                PaddingValues(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 60.dp,
                    start =
                        paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                ListItem(
                    colors = listItemColors(),
                    headlineContent = { Text(text = stringResource(R.string.look_and_feel)) },
                    supportingContent = {
                        Text(text = stringResource(R.string.look_and_feel_info))
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.palette),
                            contentDescription = "Navigate",
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.arrow_forward),
                            contentDescription = "Go",
                        )
                    },
                    modifier =
                        Modifier.clip(detachedItemShape()).clickable { onNavigateToLookAndFeel() },
                )
            }

            item {
                ListItem(
                    colors = listItemColors(),
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.star),
                            contentDescription = "Onboarding",
                        )
                    },
                    headlineContent = { Text(text = stringResource(R.string.onboarding)) },
                    supportingContent = { Text(text = stringResource(R.string.onboarding_desc)) },
                    trailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.arrow_forward),
                            contentDescription = "Go",
                        )
                    },
                    modifier =
                        Modifier.clip(detachedItemShape()).clickable { onNavigateToOnboarding() },
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    ListItem(
                        colors = listItemColors(),
                        leadingContent = {
                            Icon(
                                painter = painterResource(R.drawable.info),
                                contentDescription = null,
                            )
                        },
                        supportingContent = { Text(text = "Momento $currentVersion") },
                        trailingContent = {
                            Icon(
                                painter = painterResource(R.drawable.arrow_forward),
                                contentDescription = "Navigate",
                            )
                        },
                        headlineContent = { Text(text = stringResource(R.string.about)) },
                        modifier =
                            Modifier.clip(leadingItemShape()).clickable { onNavigateToAppInfo() },
                    )

                    ListItem(
                        colors = listItemColors(),
                        headlineContent = { Text(text = stringResource(R.string.changelog)) },
                        leadingContent = {
                            Icon(
                                painter = painterResource(R.drawable.logs),
                                contentDescription = "Changelog",
                            )
                        },
                        trailingContent = {
                            Icon(
                                painter = painterResource(R.drawable.arrow_forward),
                                contentDescription = "Go",
                            )
                        },
                        modifier =
                            Modifier.clip(endItemShape()).clickable { onNavigateToChangelog() },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MomentoTheme(theme = Theme(appTheme = AppTheme.DARK)) {
        Root(
            onNavigateBack = {},
            onNavigateToLookAndFeel = {},
            onNavigateToChangelog = {},
            onNavigateToOnboarding = {},
            onNavigateToAppInfo = {},
            currentVersion = "1.0.0",
        )
    }
}
