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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sagar.momento.R
import com.sagar.momento.presentation.settings.ui.component.LicenseBottomSheet
import com.sagar.momento.presentation.shared.detachedItemShape
import com.sagar.momento.presentation.shared.endItemShape
import com.sagar.momento.presentation.shared.flexFontEmphasis
import com.sagar.momento.presentation.shared.flexFontRounded
import com.sagar.momento.presentation.shared.leadingItemShape
import com.sagar.momento.presentation.shared.listItemColors

@Composable
fun About(versionName: String, onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uriHandler = LocalUriHandler.current

    var showLicenseBottomSheet by remember { mutableStateOf(false) }

    if (showLicenseBottomSheet) {
        LicenseBottomSheet(onDismissRequest = { showLicenseBottomSheet = false })
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumFlexibleTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.about), fontFamily = flexFontEmphasis())
                },
                navigationIcon = {
                    FilledTonalIconButton(onClick = onNavigateBack) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding =
                PaddingValues(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 60.dp,
                    start = padding.calculateLeftPadding(LocalLayoutDirection.current) + 16.dp,
                    end = padding.calculateRightPadding(LocalLayoutDirection.current) + 16.dp,
                ),
        ) {
            aboutApp(versionName = versionName, uriHandler = uriHandler)
            otherProjects(uriHandler)
            engagementLinks(uriHandler)
            item {
                ListItem(
                    colors = listItemColors(),
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.license),
                            contentDescription = null,
                        )
                    },
                    headlineContent = { Text(text = "License") },
                    supportingContent = { Text(text = "MIT License") },
                    modifier =
                        Modifier.clip(detachedItemShape()).clickable {
                            showLicenseBottomSheet = true
                        },
                )
            }
        }
    }
}

private fun LazyListScope.engagementLinks(uriHandler: UriHandler) {
    item {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            ListItem(
                colors = listItemColors(),
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.buymeacoffee),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                trailingContent = {
                    Icon(painter = painterResource(R.drawable.open_link), contentDescription = null)
                },
                headlineContent = { Text(text = stringResource(R.string.bmc)) },
                supportingContent = { Text(text = stringResource(R.string.bmc_desc)) },
                modifier =
                    Modifier.clip(detachedItemShape()).clickable {
                        uriHandler.openUri("https://dub.sh/donatesagar")
                    },
            )
        }
    }
}

private fun LazyListScope.otherProjects(uriHandler: UriHandler) {
    item {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            ListItem(
                colors = listItemColors(),
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.landscape),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                trailingContent = {
                    Icon(painter = painterResource(R.drawable.open_link), contentDescription = null)
                },
                headlineContent = { Text(text = "Other Projects") },
                supportingContent = { Text(text = "Check out my other projects") },
                modifier =
                    Modifier.clip(detachedItemShape()).clickable {
                        uriHandler.openUri("https://rexd.space/#projects")
                    },
            )
        }
    }
}

private fun LazyListScope.aboutApp(versionName: String, uriHandler: UriHandler) {
    item {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            // App card
            Card(shape = leadingItemShape()) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier =
                            Modifier.size(64.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialShapes.Cookie12Sided.toShape(),
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(48.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Momento",
                            style =
                                MaterialTheme.typography.headlineSmall.copy(
                                    fontFamily = flexFontRounded()
                                ),
                        )
                        Text(
                            text = versionName,
                            style =
                                MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.primary
                                ),
                        )
                    }

                    Row {
                        FilledTonalIconButton(
                            onClick = { uriHandler.openUri("https://discord.com/invite/tUZ8E7wdd2") }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.discord),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        FilledTonalIconButton(
                            onClick = { uriHandler.openUri("https://dub.sh/donatesagar") }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.favorite),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            }

            // dev card
            Card(shape = endItemShape()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier =
                                Modifier.size(64.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = MaterialTheme.colorScheme.tertiaryContainer,
                                        shape = CircleShape,
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.dev_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(64.dp).clip(CircleShape),
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Pranav Sagar",
                                style =
                                    MaterialTheme.typography.headlineSmall.copy(
                                        fontFamily = flexFontRounded()
                                    ),
                            )
                            Text(
                                text = "Developer",
                                style =
                                    MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                            )
                        }
                    }

                    FlowRow(
                        modifier = Modifier.padding(start = 80.dp, top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf(
                                "https://github.com/PranavSagarXD" to R.drawable.github,
                                "https://rexd.space/" to R.drawable.language,
                                "mailto:contact@rexd.space" to R.drawable.email,
                            )
                            .forEach { pair ->
                                FilledTonalIconButton(
                                    onClick = { uriHandler.openUri(pair.first) },
                                    modifier =
                                        Modifier.size(
                                            IconButtonDefaults.smallContainerSize(
                                                widthOption =
                                                    IconButtonDefaults.IconButtonWidthOption.Wide
                                            )
                                        ),
                                ) {
                                    Icon(
                                        painter = painterResource(pair.second),
                                        contentDescription = null,
                                        modifier = Modifier.size(IconButtonDefaults.smallIconSize),
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}
