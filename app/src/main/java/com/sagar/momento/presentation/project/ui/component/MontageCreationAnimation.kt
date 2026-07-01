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

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.flexFontRounded

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MontageCreationAnimation(progress: Float, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "transfer")

    val travelProgress by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = EaseInOut),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "travel",
        )

    // 🔥 Pulse triggers
    val sourcePulse by
        animateFloatAsState(
            targetValue = if (travelProgress < 0.2f) 1.15f else 1f,
            animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
            label = "sourcePulse",
        )

    val targetPulse by
        animateFloatAsState(
            targetValue = if (travelProgress > 0.8f) 1.15f else 1f,
            animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
            label = "targetPulse",
        )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 32.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val startX = -120f
            val endX = 120f
            // 🚀 Moving element using lerp
            val xOffset = lerp(startX, endX, travelProgress)

            val alpha =
                when {
                    travelProgress < 0.1f -> lerp(0f, 1f, travelProgress / 0.1f)
                    travelProgress > 0.9f -> lerp(1f, 0f, (travelProgress - 0.9f) / 0.1f)
                    else -> 1f
                }

            val scale =
                when {
                    travelProgress < 0.2f -> lerp(0.6f, 1f, travelProgress / 0.2f)
                    travelProgress > 0.8f -> lerp(1f, 0.6f, (travelProgress - 0.8f) / 0.2f)
                    else -> 1f
                }

            Box(
                modifier =
                    Modifier.offset(x = xOffset.dp)
                        .graphicsLayer {
                            this.scaleX = scale
                            this.scaleY = scale
                            this.alpha = alpha
                        }
                        .size(36.dp)
                        .clip(MaterialShapes.Pill.toShape())
                        .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.image),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }

            // 🧱 Containers (Row now)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                // ⬅️ SOURCE
                Box(
                    modifier =
                        Modifier.scale(sourcePulse)
                            .size(100.dp)
                            .clip(MaterialShapes.Cookie12Sided.toShape())
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.photo_library),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                    )
                }

                // ➡️ TARGET
                Box(
                    modifier =
                        Modifier.scale(targetPulse)
                            .size(120.dp)
                            .clip(MaterialShapes.SoftBurst.toShape())
                            .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.play),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                    )
                }
            }
        }

        LinearWavyProgressIndicator(
            progress = { progress },
            modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
        )

        Text(
            text = stringResource(R.string.processing_images),
            style = MaterialTheme.typography.titleLarge.copy(fontFamily = flexFontRounded()),
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            while (progress <= 1) {
                delay(100)
                progress += 0.01f
            }
            delay(1000)
            progress = 0f
            delay(500)
        }
    }

    MomentoTheme(theme = Theme(appTheme = AppTheme.DARK)) {
        Surface(modifier = Modifier.fillMaxSize()) { MontageCreationAnimation(progress = progress) }
    }
}
