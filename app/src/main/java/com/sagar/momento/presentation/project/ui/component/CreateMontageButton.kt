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
package com.sagar.momento.presentation.project.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes.Companion.VerySunny
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sagar.momento.R
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.flexFontRounded

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun CreateMontageButton(
    daysSize: Int,
    onNavigateToMontage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayProgress by animateFloatAsState(targetValue = daysSize.toFloat() / 5f)

    val canCreateMontage = daysSize >= 5

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")

    val rotation by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "infinite rotation",
        )

    Card(
        onClick = { if (canCreateMontage) onNavigateToMontage() },
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
    ) {
        Box {
            Box(
                modifier =
                    Modifier.matchParentSize()
                        .drawWithContent {
                            val width = size.width * dayProgress

                            clipRect(right = width) { this@drawWithContent.drawContent() }
                        }
                        .background(MaterialTheme.colorScheme.secondaryContainer)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp),
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.montage),
                        style =
                            MaterialTheme.typography.titleLarge.copy(fontFamily = flexFontRounded()),
                    )

                    val daysLeft = 5 - daysSize
                    Text(
                        text =
                            if (!canCreateMontage) {
                                pluralStringResource(
                                    id = R.plurals.add_more_days,
                                    count = daysLeft,
                                    formatArgs = arrayOf(daysLeft),
                                )
                            } else {
                                "$daysSize ${stringResource(R.string.days)}"
                            },
                        style =
                            MaterialTheme.typography.labelLarge.copy(fontFamily = flexFontRounded()),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                    Box(
                        modifier =
                            Modifier.size(50.dp)
                                .then(
                                    if (canCreateMontage) {
                                        Modifier.graphicsLayer { rotationZ = rotation }
                                    } else Modifier
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = VerySunny.toShape(),
                                )
                    )

                    Icon(
                        painter = painterResource(R.drawable.arrow_forward),
                        contentDescription = "Create Montage",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MomentoTheme { CreateMontageButton(daysSize = 7, onNavigateToMontage = {}) }
}
