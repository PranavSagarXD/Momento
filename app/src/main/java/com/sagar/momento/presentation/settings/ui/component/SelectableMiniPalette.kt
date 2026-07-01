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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.materialkolor.palettes.TonalPalette
import com.sagar.momento.R

// yeeted from
// https://github.com/SkyD666/PodAura/blob/master/app/src/main/java/com/skyd/anivu/ui/screen/settings/appearance/AppearanceScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableMiniPalette(
    modifier: Modifier = Modifier,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    contentDescription: () -> String,
    accents: List<TonalPalette>,
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        TooltipBox(
            modifier = modifier,
            positionProvider =
                TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.End
                ),
            tooltip = { PlainTooltip { Text(contentDescription()) } },
            state = rememberTooltipState(),
        ) {
            Surface(
                modifier =
                    Modifier.clickable(enabled = enabled, onClick = onClick)
                        .padding(12.dp)
                        .size(50.dp),
                shape = CircleShape,
                color = Color(accents[0].tone(60)),
            ) {
                Box {
                    Surface(
                        modifier = Modifier.size(50.dp).offset((-25).dp, 25.dp),
                        color = Color(accents[1].tone(85)),
                    ) {}
                    Surface(
                        modifier = Modifier.size(50.dp).offset(25.dp, 25.dp),
                        color = Color(accents[2].tone(75)),
                    ) {}
                    val animationSpec = spring<Float>(stiffness = Spring.StiffnessMedium)
                    AnimatedVisibility(
                        visible = selected,
                        enter = scaleIn(animationSpec) + fadeIn(animationSpec),
                        exit = scaleOut(animationSpec) + fadeOut(animationSpec),
                    ) {
                        Box(
                            modifier =
                                Modifier.padding(10.dp)
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.check_border),
                                contentDescription = "Checked",
                                modifier = Modifier.padding(8.dp).size(16.dp),
                                tint = MaterialTheme.colorScheme.surface,
                            )
                        }
                    }
                }
            }
        }
    }
}
