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

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sagar.momento.R
import com.sagar.momento.presentation.MomentoPreviewWrapper
import com.sagar.momento.presentation.shared.MomentoBottomSheet
import com.sagar.momento.presentation.shared.endItemShape
import com.sagar.momento.presentation.shared.leadingItemShape

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageSourcePicker(
    modifier: Modifier = Modifier,
    onOpenCamera: () -> Unit,
    onOpenGallery: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val cameraPermissionState =
        rememberPermissionState(Manifest.permission.CAMERA) { granted ->
            if (granted) {
                onOpenCamera()
            }
            onDismissRequest()
        }

    MomentoBottomSheet(modifier = modifier, onDismissRequest = onDismissRequest, padding = 16.dp) {
        ImageSourcePickerContent(
            onOpenGallery = {
                onOpenGallery()
                onDismissRequest()
            },
            onOpenCamera = {
                if (cameraPermissionState.status.isGranted) {
                    onOpenCamera()
                    onDismissRequest()
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            },
        )
    }
}

@Composable
private fun ImageSourcePickerContent(
    modifier: Modifier = Modifier,
    onOpenGallery: () -> Unit,
    onOpenCamera: () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        ListItem(
            modifier = Modifier.clip(leadingItemShape()).clickable { onOpenGallery() },
            headlineContent = { Text(text = stringResource(R.string.select_from_gallery)) },
            leadingContent = {
                Icon(painter = painterResource(R.drawable.image), contentDescription = null)
            },
            trailingContent = {
                Icon(painter = painterResource(R.drawable.arrow_forward), contentDescription = null)
            },
        )
        ListItem(
            modifier = Modifier.clip(endItemShape()).clickable { onOpenCamera() },
            headlineContent = { Text(text = stringResource(R.string.open_camera)) },
            leadingContent = {
                Icon(painter = painterResource(R.drawable.camera), contentDescription = null)
            },
            trailingContent = {
                Icon(painter = painterResource(R.drawable.arrow_forward), contentDescription = null)
            },
        )
    }
}

@PreviewWrapper(MomentoPreviewWrapper::class)
@Preview
@Composable
private fun Preview() {
    ImageSourcePickerContent(onOpenGallery = {}, onOpenCamera = {})
}
