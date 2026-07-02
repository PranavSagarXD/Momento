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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.MontageConfig
import com.sagar.momento.core.enums.DateStyle
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.VideoQuality
import com.sagar.momento.core.toFormatStyle
import com.sagar.momento.presentation.project.ProjectAction
import com.sagar.momento.presentation.project.ProjectState
import com.sagar.momento.presentation.shared.ColorPickerDialog
import com.sagar.momento.presentation.shared.SettingSlider
import com.sagar.momento.presentation.shared.detachedItemShape
import com.sagar.momento.presentation.shared.endItemShape
import com.sagar.momento.presentation.shared.flexFontRounded
import com.sagar.momento.presentation.shared.leadingItemShape
import com.sagar.momento.presentation.shared.middleItemShape
import com.sagar.momento.presentation.toDisplayString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MontageEditSheet(
    state: ProjectState,
    onAction: (ProjectAction) -> Unit,
    onDismissRequest: () -> Unit,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier,
    sheetState: SheetState =
        rememberBottomSheetState(
            initialValue = SheetValue.Hidden,
            enabledValues = setOf(SheetValue.Expanded, SheetValue.Hidden),
        ),
) {
    var showColorPicker by remember { mutableStateOf(false) }

    if (showColorPicker) {
        ColorPickerDialog(
            initialColor = state.montageConfig.backgroundColor,
            onSelect = { color ->
                onAction(
                    ProjectAction.OnEditMontageConfig(
                        config = state.montageConfig.copy(backgroundColor = color)
                    )
                )
            },
            onDismiss = { showColorPicker = false },
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetGesturesEnabled = false,
        sheetState = sheetState,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.edit_montage),
                style =
                    MaterialTheme.typography.headlineMedium.copy(fontFamily = flexFontRounded()),
                modifier = Modifier.weight(1f),
            )

            IconButton(onClick = { onAction(ProjectAction.OnResetMontagePrefs) }) {
                Icon(painter = painterResource(R.drawable.sync), contentDescription = "Reset")
            }

            Button(
                onClick = {
                    onAction(ProjectAction.OnCreateMontage(state.days))
                    onDismissRequest()
                },
                enabled = buttonEnabled,
            ) {
                Text(text = stringResource(R.string.remake))
            }
        }

        LazyColumn(
            modifier =
                modifier
                    .animateContentSize()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .heightIn(max = 400.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // frames per image slider
            item {
                Card(shape = leadingItemShape()) {
                    SettingSlider(
                        title = stringResource(R.string.frames_per_image),
                        value = state.montageConfig.framesPerImage.toFloat(),
                        onValueChange = {
                            onAction(
                                ProjectAction.OnEditMontageConfig(
                                    state.montageConfig.copy(framesPerImage = it.roundToInt())
                                )
                            )
                        },
                        valueRange = 1f..10f,
                        steps = 8,
                        valueToShow = state.montageConfig.framesPerImage.toString(),
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            // fps slider
            item {
                Card(shape = endItemShape()) {
                    SettingSlider(
                        title = stringResource(R.string.frames_per_sec),
                        value = state.montageConfig.framesPerSecond,
                        onValueChange = {
                            onAction(
                                ProjectAction.OnEditMontageConfig(
                                    state.montageConfig.copy(framesPerSecond = it)
                                )
                            )
                        },
                        valueRange = 1f..10f,
                        steps = 8,
                        valueToShow = state.montageConfig.framesPerSecond.roundToInt().toString(),
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // show date/ date style
            item {
                Card(shape = leadingItemShape()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.show_date),
                                style = MaterialTheme.typography.titleLarge,
                            )

                            Switch(
                                checked = state.montageConfig.showDate,
                                onCheckedChange = {
                                    onAction(
                                        ProjectAction.OnEditMontageConfig(
                                            state.montageConfig.copy(showDate = it)
                                        )
                                    )
                                },
                            )
                        }

                        AnimatedVisibility(visible = state.montageConfig.showDate) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                DateStyle.entries.forEach { style ->
                                    ToggleButton(
                                        checked = state.montageConfig.dateStyle == style,
                                        onCheckedChange = {
                                            onAction(
                                                ProjectAction.OnEditMontageConfig(
                                                    state.montageConfig.copy(dateStyle = style)
                                                )
                                            )
                                        },
                                    ) {
                                        Text(
                                            text =
                                                LocalDate.now()
                                                    .format(
                                                        DateTimeFormatter.ofLocalizedDate(
                                                            style.toFormatStyle()
                                                        )
                                                    )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // show day message
            item {
                Card(shape = endItemShape()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.show_message),
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Switch(
                            checked = state.montageConfig.showMessage,
                            onCheckedChange = {
                                onAction(
                                    ProjectAction.OnEditMontageConfig(
                                        state.montageConfig.copy(showMessage = it)
                                    )
                                )
                            },
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // stabilize faces
            item {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Card(
                        shape =
                            if (state.montageConfig.stabilizeFaces) {
                                leadingItemShape()
                            } else {
                                detachedItemShape()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.stabilize_faces),
                                style = MaterialTheme.typography.titleLarge,
                            )

                            Switch(
                                checked = state.montageConfig.stabilizeFaces,
                                onCheckedChange = {
                                    onAction(
                                        ProjectAction.OnEditMontageConfig(
                                            state.montageConfig.copy(stabilizeFaces = it)
                                        )
                                    )
                                },
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = state.montageConfig.stabilizeFaces,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Card(shape = endItemShape()) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(R.string.censor_faces),
                                    style = MaterialTheme.typography.titleLarge,
                                )

                                Switch(
                                    checked = state.montageConfig.censorFaces,
                                    enabled = state.montageConfig.stabilizeFaces,
                                    onCheckedChange = {
                                        onAction(
                                            ProjectAction.OnEditMontageConfig(
                                                state.montageConfig.copy(censorFaces = it)
                                            )
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // video quality
            item {
                Card(shape = leadingItemShape()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.video_quality),
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Spacer(modifier = Modifier.size(44.dp))
                    }

                    FlowRow(
                        modifier =
                            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        VideoQuality.entries.forEach { quality ->
                            ToggleButton(
                                checked = quality == state.montageConfig.videoQuality,
                                onCheckedChange = {
                                    onAction(
                                        ProjectAction.OnEditMontageConfig(
                                            state.montageConfig.copy(videoQuality = quality)
                                        )
                                    )
                                },
                            ) {
                                Text(text = quality.name)
                            }
                        }
                    }
                }
            }

            // text font
            item {
                Card(shape = middleItemShape()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.video_font),
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Spacer(modifier = Modifier.size(44.dp))
                    }

                    FlowRow(
                        modifier =
                            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Fonts.entries.forEach { font ->
                            ToggleButton(
                                checked = font == state.montageConfig.font,
                                onCheckedChange = {
                                    onAction(
                                        ProjectAction.OnEditMontageConfig(
                                            state.montageConfig.copy(font = font)
                                        )
                                    )
                                },
                            ) {
                                Text(text = font.toDisplayString())
                            }
                        }
                    }
                }
            }

            // show watermark
            item {
                Card(shape = middleItemShape()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.show_watermark),
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Switch(
                            checked = state.montageConfig.waterMark,
                            onCheckedChange = {
                                onAction(
                                    ProjectAction.OnEditMontageConfig(
                                        state.montageConfig.copy(waterMark = it)
                                    )
                                )
                            },
                        )
                    }
                }
            }

            // set background color
            item {
                Card(shape = endItemShape()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.background_color),
                            style = MaterialTheme.typography.titleLarge,
                        )

                        IconButton(
                            onClick = { showColorPicker = true },
                            colors =
                                IconButtonDefaults.iconButtonColors(
                                    containerColor = state.montageConfig.backgroundColor,
                                    contentColor =
                                        contentColorFor(state.montageConfig.backgroundColor),
                                ),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.edit),
                                contentDescription = "Pick color",
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun Preview() {
    MontageEditSheet(
        state =
            ProjectState(
                days =
                    listOf(
                        Day(
                            id = 1,
                            projectId = 1,
                            image = "",
                            comment = "",
                            date = 1,
                            isFavorite = false,
                        )
                    ),
                montageConfig = MontageConfig(stabilizeFaces = true),
            ),
        onAction = {},
        buttonEnabled = false,
        onDismissRequest = {},
        sheetState = rememberBottomSheetState(initialValue = SheetValue.Expanded),
    )
}
