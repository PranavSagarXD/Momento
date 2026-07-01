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
package com.sagar.momento.presentation.home.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import java.time.LocalDate
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.data_classes.ProjectListData
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.presentation.shared.MomentoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListItem(
    projectListData: ProjectListData,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = shape,
        onClick = onClick,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (projectListData.last10Days.isNotEmpty()) {
                val carouselState = rememberCarouselState { projectListData.last10Days.size }

                HorizontalMultiBrowseCarousel(
                    state = carouselState,
                    modifier = Modifier.height(200.dp),
                    preferredItemWidth = 200.dp,
                    itemSpacing = 8.dp,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
                ) {
                    val currentDay = projectListData.last10Days[it]

                    CoilImage(
                        imageModel = { currentDay.image },
                        failure = {
                            Box(
                                modifier = Modifier.matchParentSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.warning),
                                    contentDescription = "Warning",
                                )
                            }
                        },
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        previewPlaceholder = painterResource(R.drawable.ic_launcher_foreground),
                        modifier = Modifier.fillMaxSize().maskClip(RoundedCornerShape(16.dp)),
                    )
                }
            }

            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = projectListData.project.title,
                        style =
                            MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )

                    if (projectListData.project.description.isNotBlank()) {
                        Text(
                            text = projectListData.project.description,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(R.drawable.arrow_forward),
                    contentDescription = "Navigate to project",
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MomentoTheme(theme = Theme(appTheme = AppTheme.DARK)) {
        ProjectListItem(
            projectListData =
                ProjectListData(
                    project =
                        Project(
                            id = 1,
                            title = "Project 1",
                            description = "Description for project 1",
                        ),
                    last10Days =
                        (0..10).map { it1 ->
                            Day(
                                id = it1.toLong(),
                                projectId = it1.toLong(),
                                image = "",
                                comment = "",
                                date = LocalDate.now().toEpochDay(),
                                isFavorite = false,
                            )
                        },
                ),
            onClick = {},
            shape = RoundedCornerShape(4.dp),
        )
    }
}
