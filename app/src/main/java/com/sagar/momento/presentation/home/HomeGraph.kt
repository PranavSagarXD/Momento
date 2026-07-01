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
package com.sagar.momento.presentation.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.data_classes.ProjectListData
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.PaletteStyle
import com.sagar.momento.presentation.home.ui.sections.ProjectList
import com.sagar.momento.presentation.home.ui.sections.ProjectUpsertSheet
import com.sagar.momento.presentation.shared.MomentoTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeGraph(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProject: () -> Unit,
    isPlusUser: Boolean,
    onNavigateToPaywall: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showProjectUpsertSheet by remember { mutableStateOf(false) }

    ProjectList(
        modifier = modifier,
        state = state,
        onAction = onAction,
        onNavigateToProject = onNavigateToProject,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToNewProject = {
            if (state.projects.size <= 3 || isPlusUser) {
                showProjectUpsertSheet = true
            } else {
                onNavigateToPaywall()
            }
        },
    )

    if (showProjectUpsertSheet) {
        ProjectUpsertSheet(
            onUpsertProject = {
                onAction(HomeAction.OnAddProject(title = it.title, description = it.description))
            },
            onDismissRequest = { showProjectUpsertSheet = false },
            project = Project(title = "", description = ""),
        )
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
                            last10Days = emptyList(),
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
        HomeGraph(
            state = state,
            onAction = {},
            onNavigateToSettings = {},
            onNavigateToProject = {},
            isPlusUser = true,
            onNavigateToPaywall = {},
        )
    }
}
