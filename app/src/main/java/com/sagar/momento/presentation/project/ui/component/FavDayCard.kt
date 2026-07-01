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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.data.getPlaceholder
import com.sagar.momento.presentation.shared.MomentoTheme

@Composable
fun FavDayCard(
    day: Day,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomStart) {
            CoilImage(
                imageModel = { day.image },
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                failure = {
                    Icon(
                        painter = painterResource(R.drawable.warning),
                        contentDescription = "Placeholder",
                        modifier = Modifier.align(Alignment.Center).size(70.dp),
                    )
                },
                previewPlaceholder = getPlaceholder(),
                modifier = Modifier.clip(shape).height(150.dp).fillMaxWidth(),
            )

            Box(
                modifier =
                    Modifier.matchParentSize()
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    0.7f to
                                        MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 0.4f
                                        ),
                                    0.9f to
                                        MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 0.8f
                                        ),
                                    1f to MaterialTheme.colorScheme.primaryContainer,
                                )
                        )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text =
                            LocalDate.ofEpochDay(day.date)
                                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    if (!day.comment.isNullOrBlank()) {
                        Text(text = day.comment!!)
                    }
                }

                Icon(painter = painterResource(R.drawable.arrow_forward), contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MomentoTheme {
        FavDayCard(
            day =
                Day(
                    id = 1,
                    projectId = 1,
                    image = "",
                    comment = "A day",
                    date = 1,
                    isFavorite = false,
                ),
            onClick = {},
        )
    }
}
