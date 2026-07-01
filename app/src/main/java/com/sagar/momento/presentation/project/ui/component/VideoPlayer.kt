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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.PlayerAction
import com.sagar.momento.core.enums.VideoAction

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    onPlayerAction: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPlaying by remember { mutableStateOf(true) }
    var showControls by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0L) }
    var position by remember { mutableLongStateOf(0L) }

    // Update position periodically
    LaunchedEffect(exoPlayer) {
        delay(300L)
        onPlayerAction(PlayerAction(action = VideoAction.PLAY))
        while (isActive) {
            duration = exoPlayer.duration.coerceAtLeast(0L)
            position = exoPlayer.currentPosition.coerceAtLeast(0L)
            delay(500L)
        }
    }

    // Auto-hide controls
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(3000L)
            showControls = false
        }
    }

    Box(modifier = modifier.clickable { showControls = true }) {
        PlayerSurface(
            player = exoPlayer,
            modifier = Modifier.fillMaxSize(),
            surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
        )

        AnimatedVisibility(visible = showControls, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilledIconButton(
                    onClick = {
                        if (exoPlayer.isPlaying) {
                            onPlayerAction(PlayerAction(action = VideoAction.PAUSE))
                            isPlaying = false
                        } else {
                            onPlayerAction(PlayerAction(action = VideoAction.PLAY))
                            isPlaying = true
                        }
                        showControls = true
                    }
                ) {
                    Icon(
                        painter =
                            painterResource(if (isPlaying) R.drawable.pause else R.drawable.play),
                        contentDescription = null,
                    )
                }

                Slider(
                    value = if (duration > 0) position / duration.toFloat() else 0f,
                    onValueChange = { sliderValue ->
                        val newPosition = (sliderValue * duration).toLong()
                        onPlayerAction(PlayerAction(action = VideoAction.SEEK, data = newPosition))
                        position = newPosition
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
