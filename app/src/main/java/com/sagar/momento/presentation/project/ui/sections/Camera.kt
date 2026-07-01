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

import android.app.Activity
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.sagar.momento.R

@Composable
fun Camera(
    surfaceRequest: SurfaceRequest?,
    showGuides: Boolean,
    cameraSelector: CameraSelector,
    onToggleCamera: () -> Unit,
    onToggleGuides: () -> Unit,
    onTakePhoto: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.context as? Activity)?.window ?: return@DisposableEffect onDispose {}
        val controller = WindowCompat.getInsetsController(window, view)

        controller.hide(
            WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
        )
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {
            controller.show(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        surfaceRequest?.let { request ->
            val isFrontCamera = cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
            CameraXViewfinder(
                surfaceRequest = request,
                modifier =
                    Modifier.fillMaxSize().graphicsLayer {
                        if (isFrontCamera) {
                            scaleX = -1f
                        }
                    },
            )
        }

        if (showGuides) {
            CameraGuides()
        }

        FilledTonalIconButton(
            onClick = onNavigateBack,
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
        ) {
            Icon(painter = painterResource(R.drawable.arrow_back), contentDescription = "Back")
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                FilledTonalIconToggleButton(
                    checked = showGuides,
                    onCheckedChange = { onToggleGuides() },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.rounded_grid),
                        contentDescription = "Toggle Grid",
                    )
                }

                FilledTonalIconButton(
                    onClick = onTakePhoto,
                    modifier =
                        Modifier.size(
                            IconButtonDefaults.largeContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Wide
                            )
                        ),
                    shapes = IconButtonDefaults.shapes(),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "Take Photo",
                        modifier = Modifier.size(IconButtonDefaults.largeIconSize),
                    )
                }

                FilledTonalIconButton(onClick = onToggleCamera) {
                    Icon(
                        painter = painterResource(R.drawable.sync),
                        contentDescription = "Switch Camera",
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraGuides() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val color = Color.White.copy(alpha = 0.5f)
        val strokeWidth = 1.dp.toPx()

        // Vertical lines
        drawLine(
            color = color,
            start = Offset(width / 3, 0f),
            end = Offset(width / 3, height),
            strokeWidth = strokeWidth,
        )
        drawLine(
            color = color,
            start = Offset(2 * width / 3, 0f),
            end = Offset(2 * width / 3, height),
            strokeWidth = strokeWidth,
        )

        // Horizontal lines
        drawLine(
            color = color,
            start = Offset(0f, height / 3),
            end = Offset(width, height / 3),
            strokeWidth = strokeWidth,
        )
        drawLine(
            color = color,
            start = Offset(0f, 2 * height / 3),
            end = Offset(width, 2 * height / 3),
            strokeWidth = strokeWidth,
        )
    }
}
