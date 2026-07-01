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
package com.sagar.momento.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sagar.momento.R
import com.sagar.momento.app.VersionEntry
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChangelogSheet(
    modifier: Modifier = Modifier,
    currentLog: VersionEntry,
    onDismissRequest: () -> Unit,
) {
    MomentoBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier, padding = 0.dp) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier.size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialShapes.Pill.toShape(),
                        ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.whats_changed),
                    style =
                        MaterialTheme.typography.headlineSmall.copy(fontFamily = flexFontEmphasis()),
                )
                Text(
                    text = currentLog.version,
                    style =
                        MaterialTheme.typography.titleMedium.copy(fontFamily = flexFontRounded()),
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                itemsIndexed(currentLog.changes) { index, change ->
                    val shape =
                        when {
                            currentLog.changes.size == 1 -> detachedItemShape()
                            index == 0 -> leadingItemShape()
                            index == currentLog.changes.size - 1 -> endItemShape()
                            else -> middleItemShape()
                        }

                    ListItem(
                        colors = listItemColors(),
                        modifier = Modifier.clip(shape),
                        headlineContent = { Text(text = change) },
                    )
                }
            }

            Button(onClick = onDismissRequest, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.done))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MomentoTheme(Theme(appTheme = AppTheme.DARK)) {
        ChangelogSheet(
            currentLog =
                VersionEntry(
                    version = "1.0.0",
                    changes =
                        listOf(
                            "Changed X to y",
                            "Added ability to read the epstein files",
                            "removed herobrine",
                            "Got rid of the nuclear fission particle accelerator",
                        ),
                ),
            onDismissRequest = {},
        )
    }
}
