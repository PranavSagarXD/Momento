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
package com.sagar.momento.presentation.settings.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sagar.momento.BuildConfig
import com.sagar.momento.R
import com.sagar.momento.presentation.shared.endItemShape
import com.sagar.momento.presentation.shared.flexFontRounded
import com.sagar.momento.presentation.shared.leadingItemShape

@Composable
fun AboutApp(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column {
                Text(
                    text = "Momento",
                    style =
                        MaterialTheme.typography.headlineMedium.copy(fontFamily = flexFontRounded()),
                )
                Text(
                    text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row {
                IconButton(onClick = { uriHandler.openUri("https://discord.com/invite/tUZ8E7wdd2") }) {
                    Icon(
                        painter = painterResource(R.drawable.discord),
                        contentDescription = "Discord",
                        modifier = Modifier.size(24.dp),
                    )
                }

                IconButton(onClick = { uriHandler.openUri("https://github.com/PranavSagarXD/Momento") }) {
                    Icon(
                        painter = painterResource(R.drawable.github),
                        contentDescription = "Github",
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                modifier =
                    Modifier.background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = leadingItemShape(),
                        )
                        .clip(leadingItemShape())
                        .clickable { uriHandler.openUri("https://dub.sh/donatesagar") }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.buymeacoffee),
                        contentDescription = "Buy me a coffee",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.bmc),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = flexFontRounded(),
                    )
                }
            }
        }
    }
}
