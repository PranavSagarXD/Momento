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
package com.sagar.momento.presentation.project.ui.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberShareFileLauncher
import io.github.vinceglb.filekit.write
import java.time.LocalDate
import kotlinx.coroutines.launch
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.PlayerAction
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.core.enums.VideoAction
import com.sagar.momento.core.interfaces.MontageState
import com.sagar.momento.presentation.project.ProjectAction
import com.sagar.momento.presentation.project.ProjectState
import com.sagar.momento.presentation.project.ui.component.MontageCreationAnimation
import com.sagar.momento.presentation.project.ui.component.MontageEditSheet
import com.sagar.momento.presentation.project.ui.component.VideoPlayer
import com.sagar.momento.presentation.shared.MomentoTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProjectMontageView(
    exoPlayer: ExoPlayer?,
    state: ProjectState,
    onAction: (ProjectAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val lifeCycleOwner = LocalLifecycleOwner.current

    val fileShareLauncher = rememberShareFileLauncher()
    val fileSaverLauncher =
        rememberFileSaverLauncher(dialogSettings = FileKitDialogSettings()) { file ->
            if (file != null) {
                (state.montage as? MontageState.Success)?.let { result ->
                    val platformFile = PlatformFile(result.file)
                    scope.launch { file.write(platformFile) }
                }
            }
        }

    var showEditSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onAction(ProjectAction.OnInitializeExoPlayer(context))
        onAction(ProjectAction.OnCreateMontage(state.days))
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE ->
                    onAction(ProjectAction.OnPlayerAction(PlayerAction(VideoAction.PAUSE)))

                Lifecycle.Event.ON_RESUME ->
                    onAction(ProjectAction.OnPlayerAction(PlayerAction(VideoAction.PLAY)))

                else -> Unit
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
            onAction(ProjectAction.OnClearMontageState)
        }
    }

    Scaffold(modifier = modifier) { padding ->
        Box(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (state.montage) {
                is MontageState.Error -> Text("Error: ${state.montage.message}")

                is MontageState.Success -> {
                    exoPlayer?.let { player ->
                        VideoPlayer(
                            exoPlayer = player,
                            onPlayerAction = { onAction(ProjectAction.OnPlayerAction(it)) },
                            modifier =
                                Modifier.size(330.dp, 440.dp).clip(MaterialTheme.shapes.medium),
                        )
                    }
                }

                is MontageState.ProcessingImages -> {
                    MontageCreationAnimation(state.montage.progress)
                }
            }

            HorizontalFloatingToolbar(
                expanded = true,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            (state.montage as? MontageState.Success)?.let { result ->
                                fileShareLauncher.launch(PlatformFile(result.file))
                            }
                        },
                        containerColor =
                            if (state.montage is MontageState.Success) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.tertiary
                            },
                        contentColor =
                            if (state.montage is MontageState.Success) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onTertiary
                            },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.share),
                            contentDescription = "Share",
                        )
                    }
                },
                modifier = Modifier.padding(bottom = 32.dp).align(Alignment.BottomCenter),
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(painter = painterResource(R.drawable.close), contentDescription = "Close")
                }

                IconButton(onClick = { showEditSheet = true }) {
                    Icon(painter = painterResource(R.drawable.edit), contentDescription = "Edit")
                }

                IconButton(
                    onClick = {
                        fileSaverLauncher.launch(
                            suggestedName = state.project?.title ?: "Untitled",
                            defaultExtension = "mp4",
                        )
                    },
                    enabled = state.montage is MontageState.Success,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.download),
                        contentDescription = "Save to gallery",
                    )
                }
            }
        }

        if (showEditSheet) {
            MontageEditSheet(
                state = state,
                onAction = onAction,
                onDismissRequest = { showEditSheet = false },
                buttonEnabled =
                    state.montageConfig != (state.montage as? MontageState.Success)?.config,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var state by remember {
        mutableStateOf(
            ProjectState(
                project =
                    Project(id = 1, title = "Sample Project", description = "A sample project"),
                days =
                    (0..10).map {
                        Day(
                            id = it.toLong(),
                            projectId = 1,
                            image = "",
                            comment = it.toString(),
                            date = LocalDate.now().minusDays(it.toLong()).toEpochDay(),
                            isFavorite = false,
                        )
                    },
            )
        )
    }

    MomentoTheme(theme = Theme(appTheme = AppTheme.DARK)) {
        ProjectMontageView(
            state = state,
            onAction = {},
            onNavigateBack = {},
            exoPlayer = null,
        )
    }
}
