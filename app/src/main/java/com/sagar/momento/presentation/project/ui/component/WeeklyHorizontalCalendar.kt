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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.skydoves.landscapist.coil3.CoilImage
import java.time.LocalDate
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.data.getPlaceholder
import com.sagar.momento.presentation.shared.MomentoTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeeklyHorizontalCalendar(
    onNavigateToCalendar: () -> Unit,
    weekState: WeekCalendarState,
    days: List<Day>,
    showGuide: Boolean,
    onNavigateToDayInfo: (Long) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
) {
    Card(modifier = modifier.fillMaxWidth(), shape = shape, onClick = onNavigateToCalendar) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.calendar),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = "Calendar",
            )
        }

        val today = LocalDate.now()
        WeekCalendar(
            state = weekState,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp),
            dayContent = { weekDay ->
                val possibleDay = weekDay.date <= today
                val day = days.find { it.date == weekDay.date.toEpochDay() }

                Box(
                    modifier = Modifier.height(100.dp).fillMaxWidth().padding(2.dp),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    day?.let { validDay ->
                        val donePrevious =
                            days.any { it.date == weekDay.date.minusDays(1).toEpochDay() }
                        val doneAfter =
                            days.any { it.date == weekDay.date.plusDays(1).toEpochDay() }
                        val dayShape =
                            when {
                                donePrevious && doneAfter -> RoundedCornerShape(4.dp)

                                donePrevious ->
                                    RoundedCornerShape(
                                        topEnd = 20.dp,
                                        bottomEnd = 20.dp,
                                        bottomStart = 4.dp,
                                        topStart = 4.dp,
                                    )

                                doneAfter ->
                                    RoundedCornerShape(
                                        topStart = 20.dp,
                                        bottomStart = 20.dp,
                                        topEnd = 4.dp,
                                        bottomEnd = 4.dp,
                                    )

                                else -> RoundedCornerShape(20.dp)
                            }

                        CoilImage(
                            imageModel = { validDay.image },
                            previewPlaceholder = getPlaceholder(),
                            failure = {
                                Box(
                                    modifier =
                                        Modifier.matchParentSize()
                                            .border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.error,
                                                shape = dayShape,
                                            )
                                )
                            },
                            modifier = Modifier.matchParentSize().clip(dayShape),
                        )

                        Box(
                            modifier =
                                Modifier.matchParentSize()
                                    .background(
                                        brush =
                                            Brush.verticalGradient(
                                                0f to Color.Transparent,
                                                0.7f to MaterialTheme.colorScheme.background,
                                                1f to MaterialTheme.colorScheme.background,
                                            ),
                                        shape = dayShape,
                                    )
                                    .clip(dayShape)
                                    .clickable(enabled = possibleDay) {
                                        onNavigateToDayInfo(weekDay.date.toEpochDay())
                                    }
                        )
                    }
                        ?: Box(
                            modifier =
                                Modifier.matchParentSize()
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceContainer,
                                        shape = RoundedCornerShape(20.dp),
                                    )
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable(enabled = possibleDay) {
                                        onNavigateToDayInfo(weekDay.date.toEpochDay())
                                    }
                        )

                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = weekDay.date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color =
                                if (possibleDay) {
                                    MaterialTheme.colorScheme.onBackground
                                } else {
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                },
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = weekDay.date.dayOfWeek.toString().take(3),
                            color =
                                if (possibleDay) {
                                    MaterialTheme.colorScheme.onBackground
                                } else {
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                },
                            style = MaterialTheme.typography.labelSmall,
                        )

                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            },
        )

        if (showGuide) {
            Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                Text(
                    text = stringResource(R.string.guide_text),
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MomentoTheme(theme = Theme(appTheme = AppTheme.LIGHT)) {
        WeeklyHorizontalCalendar(
            onNavigateToCalendar = {},
            weekState = rememberWeekCalendarState(),
            days =
                (0..2).map {
                    Day(
                        id = it.toLong(),
                        projectId = 1,
                        image = "",
                        comment = it.toString(),
                        date = LocalDate.now().minusDays(it.toLong()).toEpochDay(),
                        isFavorite = true,
                    )
                },
            showGuide = false,
            onNavigateToDayInfo = {},
        )
    }
}
