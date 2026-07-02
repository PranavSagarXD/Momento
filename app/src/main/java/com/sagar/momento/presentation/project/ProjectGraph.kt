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
package com.sagar.momento.presentation.project

import androidx.camera.core.CameraSelector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.navigation.fadeTransitionMetadata
import com.sagar.momento.navigation.horizontalTransitionMetadata
import com.sagar.momento.navigation.verticalTransitionMetadata
import com.sagar.momento.presentation.project.ui.sections.Camera
import com.sagar.momento.presentation.project.ui.sections.CameraViewModel
import com.sagar.momento.presentation.project.ui.sections.DayInfo
import com.sagar.momento.presentation.project.ui.sections.ProjectCalendar
import com.sagar.momento.presentation.project.ui.sections.ProjectDetails
import com.sagar.momento.presentation.project.ui.sections.ProjectMontageView
import com.sagar.momento.presentation.shared.MomentoTheme

@Serializable data object ProjectDetails : NavKey

@Serializable data object ProjectCalendarView : NavKey

@Serializable data object ProjectMontageView : NavKey

@Serializable data class DayInfoView(val selectedDate: Long) : NavKey

@Serializable data class Camera(val selectedDate: Long) : NavKey

@Composable
fun ProjectGraph(
    state: ProjectState,
    exoPlayer: ExoPlayer?,
    onAction: (ProjectAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(ProjectDetails)

    LaunchedEffect(state.project) { onAction(ProjectAction.OnUpdateDays) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider =
            entryProvider {
                entry<ProjectDetails> {
                    ProjectDetails(
                        state = state,
                        onAction = onAction,
                        onNavigateBack = onNavigateBack,
                        onNavigateToCalendar = { backStack.add(ProjectCalendarView) },
                        onNavigateToMontage = { backStack.add(ProjectMontageView) },
                        onNavigateToDayInfo = { backStack.add(DayInfoView(it)) },
                    )
                }

                entry<ProjectCalendarView>(metadata = verticalTransitionMetadata()) {
                    ProjectCalendar(
                        state = state,
                        onNavigateBack = { if (backStack.size != 1) backStack.removeLastOrNull() },
                        onNavigateToDayInfo = { backStack.add(DayInfoView(it)) },
                    )
                }

                entry<DayInfoView>(metadata = verticalTransitionMetadata()) {
                    DayInfo(
                        selectedDate = it.selectedDate,
                        state = state,
                        onAction = onAction,
                        onNavigateBack = { if (backStack.size != 1) backStack.removeLastOrNull() },
                        onNavigateToCamera = { backStack.add(Camera(it.selectedDate)) },
                    )
                }

                entry<ProjectMontageView>(metadata = horizontalTransitionMetadata()) {
                    ProjectMontageView(
                        state = state,
                        exoPlayer = exoPlayer,
                        onAction = onAction,
                        onNavigateBack = { if (backStack.size != 1) backStack.removeLastOrNull() },
                    )
                }

                entry<Camera>(metadata = fadeTransitionMetadata()) { entry ->
                    val cameraViewModel = viewModel { CameraViewModel() }
                    val surfaceRequest by
                        cameraViewModel.surfaceRequest.collectAsStateWithLifecycle()
                    val showGuides by cameraViewModel.showGuides.collectAsStateWithLifecycle()
                    val cameraSelector: CameraSelector by
                        cameraViewModel.cameraSelector.collectAsStateWithLifecycle()
                    val context = LocalContext.current
                    val lifecycleOwner = LocalLifecycleOwner.current

                    LaunchedEffect(lifecycleOwner) {
                        cameraViewModel.bindToCamera(context.applicationContext, lifecycleOwner)
                    }

                    Camera(
                        surfaceRequest = surfaceRequest,
                        showGuides = showGuides,
                        cameraSelector = cameraSelector,
                        onToggleCamera = cameraViewModel::toggleCamera,
                        onToggleGuides = cameraViewModel::toggleGuides,
                        onTakePhoto = {
                            cameraViewModel.takePhoto(
                                context = context,
                                onPhotoCaptured = { photoFile ->
                                    val projectId = state.project?.id ?: return@takePhoto
                                    val day = state.days.find { it.date == entry.selectedDate }
                                    onAction(
                                        ProjectAction.OnUpsertDay(
                                            day =
                                                day?.copy(image = photoFile.absolutePath)
                                                    ?: Day(
                                                        projectId = projectId,
                                                        image = photoFile.absolutePath,
                                                        comment = "",
                                                        date = entry.selectedDate,
                                                        isFavorite = false,
                                                    ),
                                            isNewImage = true,
                                        )
                                    )
                                    if (backStack.size != 1) backStack.removeLastOrNull()
                                },
                            )
                        },
                        onNavigateBack = { if (backStack.size != 1) backStack.removeLastOrNull() },
                    )
                }
            },
    )
}

@Preview
@Composable
private fun Preview() {
    var state by remember {
        mutableStateOf(
            ProjectState(
                project =
                    Project(id = 1, title = "Sample Project", description = "A sample project")
            )
        )
    }

    MomentoTheme(theme = Theme(appTheme = AppTheme.DARK)) {
        ProjectGraph(
            state = state,
            onAction = {},
            onNavigateBack = {},
            exoPlayer = null,
        )
    }
}
