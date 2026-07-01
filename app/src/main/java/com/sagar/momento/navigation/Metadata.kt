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
package com.sagar.momento.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay

fun verticalTransitionMetadata(durationMillis: Int = 500): Map<String, Any> = metadata {
    put(NavDisplay.TransitionKey) {
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis),
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    }
    put(NavDisplay.PopTransitionKey) {
        EnterTransition.None togetherWith
            slideOutVertically(targetOffsetY = { it }, animationSpec = tween(durationMillis))
    }
    put(NavDisplay.PredictivePopTransitionKey) {
        EnterTransition.None togetherWith
            slideOutVertically(targetOffsetY = { it }, animationSpec = tween(durationMillis))
    }
}

fun horizontalTransitionMetadata(durationMillis: Int = 500): Map<String, Any> = metadata {
    put(NavDisplay.TransitionKey) {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis),
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    }
    put(NavDisplay.PopTransitionKey) {
        EnterTransition.None togetherWith
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(durationMillis))
    }
    put(NavDisplay.PredictivePopTransitionKey) {
        EnterTransition.None togetherWith
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(durationMillis))
    }
}

fun fadeTransitionMetadata(durationMillis: Int = 500): Map<String, Any> = metadata {
    put(NavDisplay.TransitionKey) {
        fadeIn(animationSpec = tween(durationMillis)) togetherWith
            ExitTransition.KeepUntilTransitionsFinished
    }
    put(NavDisplay.PopTransitionKey) {
        EnterTransition.None togetherWith fadeOut(animationSpec = tween(durationMillis))
    }
    put(NavDisplay.PredictivePopTransitionKey) {
        EnterTransition.None togetherWith fadeOut(animationSpec = tween(durationMillis))
    }
}
