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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.sagar.momento.app.Changelog
import com.sagar.momento.app.VersionEntry
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.detachedItemShape
import com.sagar.momento.presentation.shared.endItemShape
import com.sagar.momento.presentation.shared.flexFontEmphasis
import com.sagar.momento.presentation.shared.flexFontRounded
import com.sagar.momento.presentation.shared.leadingItemShape
import com.sagar.momento.presentation.shared.listItemColors
import com.sagar.momento.presentation.shared.middleItemShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Changelog(modifier: Modifier = Modifier, changelog: Changelog, onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumFlexibleTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.changelog), fontFamily = flexFontEmphasis())
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
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding =
                PaddingValues(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 60.dp,
                    start = padding.calculateLeftPadding(LocalLayoutDirection.current) + 16.dp,
                    end = padding.calculateRightPadding(LocalLayoutDirection.current) + 16.dp,
                ),
        ) {
            changelog.forEach { versionEntry ->
                item {
                    Text(
                        text = versionEntry.version,
                        style =
                            MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = flexFontRounded()
                            ),
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                itemsIndexed(versionEntry.changes) { index, change ->
                    val shape =
                        when {
                            versionEntry.changes.size == 1 -> detachedItemShape()
                            index == 0 -> leadingItemShape()
                            index == versionEntry.changes.size - 1 -> endItemShape()
                            else -> middleItemShape()
                        }

                    ListItem(
                        colors = listItemColors(),
                        modifier = Modifier.clip(shape),
                        headlineContent = { Text(text = change) },
                    )
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Preview
@Composable
private fun ChangelogPreview() {
    MomentoTheme(theme = Theme()) {
        Changelog(
            changelog =
                listOf(
                    VersionEntry(
                        version = "1.0.0",
                        changes = listOf("Initial release", "Added new feature"),
                    ),
                    VersionEntry(
                        version = "1.1.0",
                        changes = listOf("Bug fixes", "Improved performance"),
                    ),
                ),
            onNavigateBack = {},
        )
    }
}
