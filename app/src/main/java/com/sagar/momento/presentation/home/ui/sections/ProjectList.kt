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
package com.sagar.momento.presentation.home.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.data_classes.ProjectListData
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.PaletteStyle
import com.sagar.momento.presentation.home.HomeAction
import com.sagar.momento.presentation.home.HomeState
import com.sagar.momento.presentation.home.ui.component.Empty
import com.sagar.momento.presentation.home.ui.component.ProjectListItem
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.flexFontEmphasis
import com.sagar.momento.presentation.shared.flexFontRounded

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProjectList(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProject: () -> Unit,
    onNavigateToNewProject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.app_name), fontFamily = flexFontEmphasis())
                },
                subtitle = {
                    Text(
                        text = "${state.projects.size} " + stringResource(R.string.projects),
                        fontFamily = flexFontRounded(),
                    )
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToSettings,
                        shapes =
                            IconButtonShapes(
                                shape = CircleShape,
                                pressedShape = RoundedCornerShape(10.dp),
                            ),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.settings),
                            contentDescription = "Settings",
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            MediumFloatingActionButton(onClick = onNavigateToNewProject) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "Add New Project",
                    modifier = Modifier.size(FloatingActionButtonDefaults.MediumIconSize),
                )
            }
        },
    ) { padding ->
        if (state.projects.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                itemsIndexed(state.projects, key = { _, it -> it.project.id }) {
                    index,
                    projectListData ->
                    val shape =
                        when {
                            state.projects.size == 1 -> RoundedCornerShape(32.dp)
                            index == 0 ->
                                RoundedCornerShape(
                                    topStart = 32.dp,
                                    topEnd = 32.dp,
                                    bottomStart = 4.dp,
                                    bottomEnd = 4.dp,
                                )
                            index == state.projects.size - 1 ->
                                RoundedCornerShape(
                                    topStart = 4.dp,
                                    topEnd = 4.dp,
                                    bottomStart = 32.dp,
                                    bottomEnd = 32.dp,
                                )
                            else -> RoundedCornerShape(4.dp)
                        }

                    ProjectListItem(
                        projectListData = projectListData,
                        shape = shape,
                        onClick = {
                            onAction(HomeAction.OnChangeProject(projectListData.project))
                            onNavigateToProject()
                        },
                    )
                }
            }
        } else {
            Empty()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var state by remember {
        mutableStateOf(
            HomeState(
                projects =
                    (0..10).map {
                        ProjectListData(
                            project =
                                Project(
                                    id = it.toLong(),
                                    title = "Project $it",
                                    description = "Description for project $it",
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
                        )
                    }
            )
        )
    }

    MomentoTheme(
        theme =
            Theme(
                seedColor = Color.Yellow,
                appTheme = AppTheme.DARK,
                font = Fonts.FIGTREE,
                paletteStyle = PaletteStyle.FIDELITY,
            )
    ) {
        ProjectList(
            state = state,
            onAction = {},
            onNavigateToSettings = {},
            onNavigateToProject = {},
            onNavigateToNewProject = {},
        )
    }
}
