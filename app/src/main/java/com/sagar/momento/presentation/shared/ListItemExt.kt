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
package com.sagar.momento.presentation.shared

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private const val connectedCornerRadius = 4
private const val endCornerRadius = 16

@Composable
fun listItemColors(): ListItemColors {
    return ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
}

fun leadingItemShape(): Shape =
    RoundedCornerShape(
        topStart = endCornerRadius.dp,
        topEnd = endCornerRadius.dp,
        bottomEnd = connectedCornerRadius.dp,
        bottomStart = connectedCornerRadius.dp,
    )

fun middleItemShape(): Shape =
    RoundedCornerShape(
        topStart = connectedCornerRadius.dp,
        topEnd = connectedCornerRadius.dp,
        bottomStart = connectedCornerRadius.dp,
        bottomEnd = connectedCornerRadius.dp,
    )

fun endItemShape(): Shape =
    RoundedCornerShape(
        topStart = connectedCornerRadius.dp,
        topEnd = connectedCornerRadius.dp,
        bottomEnd = endCornerRadius.dp,
        bottomStart = endCornerRadius.dp,
    )

fun detachedItemShape(): Shape = RoundedCornerShape(endCornerRadius.dp)
