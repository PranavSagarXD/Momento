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
package com.sagar.momento.presentation.settings.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sagar.momento.presentation.shared.MomentoBottomSheet

// yeeted from nsh07/Tomato
@Composable
fun LicenseBottomSheet(modifier: Modifier = Modifier, onDismissRequest: () -> Unit) {
    val typography = MaterialTheme.typography

    val paragraphs = remember {
        listOf(
            buildAnnotatedString {
                append("Copyright (c) 2026 PranavSagarXD\n\n")
                append(
                    "Permission is hereby granted, free of charge, to any person obtaining a copy " +
                        "of this software and associated documentation files (the \"Software\"), to deal " +
                        "in the Software without restriction, including without limitation the rights " +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell " +
                        "copies of the Software, and to permit persons to whom the Software is " +
                        "furnished to do so, subject to the following conditions:\n\n" +
                        "The above copyright notice and this permission notice shall be included in all " +
                        "copies or substantial portions of the Software.\n\n" +
                        "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR " +
                        "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, " +
                        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE " +
                        "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER " +
                        "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, " +
                        "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE " +
                        "SOFTWARE."
                )
            },
        )
    }

    MomentoBottomSheet(onDismissRequest = onDismissRequest, padding = 0.dp, modifier = modifier) {
        LazyColumn(
            modifier = Modifier.clip(shapes.large).heightIn(max = 500.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            item {
                Text(
                    "MIT License",
                    style = typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp).fillMaxWidth(),
                )
            }
            items(paragraphs) {
                Text(
                    it,
                    style = typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }
    }
}
