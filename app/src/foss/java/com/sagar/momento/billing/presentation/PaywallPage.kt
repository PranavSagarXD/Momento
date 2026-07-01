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
package com.sagar.momento.billing.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sagar.momento.R
import com.sagar.momento.presentation.shared.flexFontRounded

@Composable
fun PaywallPage(isPlusUser: Boolean, onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Scaffold { paddingValues ->
        Box(
            modifier = modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier.size(100.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialShapes.VerySunny.toShape(),
                            ),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.favorite),
                        contentDescription = "Support",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Support Momento",
                        style =
                            MaterialTheme.typography.titleLarge.copy(
                                textAlign = TextAlign.Center,
                                fontFamily = flexFontRounded(),
                            ),
                    )

                    Text(
                        text = stringResource(R.string.foss_desc),
                        style =
                            MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                    )
                }

                FilledTonalButton(
                    onClick = { uriHandler.openUri("https://dub.sh/donatesagar") },
                    modifier = Modifier.height(ButtonDefaults.MediumContainerHeight),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.buymeacoffee),
                        contentDescription = "Buy me a coffee",
                        modifier = Modifier.size(ButtonDefaults.MediumIconSize),
                    )

                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))

                    Text(
                        text = stringResource(R.string.bmc),
                        style = ButtonDefaults.textStyleFor(ButtonDefaults.MediumContainerHeight),
                    )
                }
            }
        }
    }
}
