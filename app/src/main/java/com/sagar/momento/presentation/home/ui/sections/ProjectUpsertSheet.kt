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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sagar.momento.R
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.data_classes.Theme
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.PaletteStyle
import com.sagar.momento.presentation.shared.MomentoBottomSheet
import com.sagar.momento.presentation.shared.MomentoTheme
import com.sagar.momento.presentation.shared.flexFontEmphasis

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProjectUpsertSheet(
    project: Project,
    onUpsertProject: (Project) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    edit: Boolean = false,
) {
    var newProject by remember { mutableStateOf(project) }

    MomentoBottomSheet(
        modifier = modifier.imePadding(),
        padding = 0.dp,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Box(
                modifier =
                    Modifier.size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialShapes.Pill.toShape(),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(if (edit) R.drawable.edit else R.drawable.add),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            Text(
                text =
                    stringResource(if (edit) R.string.edit_project else R.string.start_new_project),
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = flexFontEmphasis()),
            )

            OutlinedTextField(
                value = newProject.title,
                onValueChange = { newProject = newProject.copy(title = it) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                isError = newProject.title.length >= 20,
                placeholder = { Text(text = stringResource(R.string.title)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = newProject.description,
                onValueChange = { newProject = newProject.copy(description = it) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                isError = newProject.description.length >= 100,
                placeholder = { Text(text = stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    onUpsertProject(newProject)
                    onDismissRequest()
                },
                enabled =
                    newProject.title.length < 20 &&
                        newProject.description.length <= 100 &&
                        newProject.title.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(if (edit) R.string.edit else R.string.add_new_project))
            }

            OutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    MomentoTheme(
        theme =
            Theme(
                seedColor = Color.Yellow,
                appTheme = AppTheme.DARK,
                font = Fonts.FIGTREE,
                paletteStyle = PaletteStyle.FIDELITY,
            )
    ) {
        ProjectUpsertSheet(
            project = Project(title = "", description = ""),
            onUpsertProject = {},
            onDismissRequest = {},
        )
    }
}
