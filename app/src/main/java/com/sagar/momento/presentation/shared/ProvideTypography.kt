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

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.tooling.preview.Preview
import com.sagar.momento.core.R

val TYPOGRAPHY = Typography()

@OptIn(ExperimentalTextApi::class)
@Composable
fun flexFontEmphasis(slant: Float = 0f): FontFamily =
    FontFamily(
        Font(
            resId = R.font.google_sans_flex,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(1000),
                    FontVariation.slant(slant),
                    FontVariation.width(120f),
                ),
        )
    )

@OptIn(ExperimentalTextApi::class)
@Composable
fun flexFontRounded(): FontFamily =
    FontFamily(
        Font(
            resId = R.font.google_sans_flex,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(800),
                    FontVariation.Setting("ROND", 100f),
                ),
        )
    )

@Composable
fun provideTypography(font: Int? = R.font.poppins): Typography {
    val selectedFont = font?.let { FontFamily(Font(font)) } ?: FontFamily.Default

    return Typography(
        displayLarge = TYPOGRAPHY.displayLarge.copy(fontFamily = selectedFont),
        displayMedium = TYPOGRAPHY.displayMedium.copy(fontFamily = selectedFont),
        displaySmall = TYPOGRAPHY.displaySmall.copy(fontFamily = selectedFont),
        headlineLarge = TYPOGRAPHY.headlineLarge.copy(fontFamily = selectedFont),
        headlineMedium = TYPOGRAPHY.headlineMedium.copy(fontFamily = selectedFont),
        headlineSmall = TYPOGRAPHY.headlineSmall.copy(fontFamily = selectedFont),
        titleLarge = TYPOGRAPHY.titleLarge.copy(fontFamily = selectedFont),
        titleMedium = TYPOGRAPHY.titleMedium.copy(fontFamily = selectedFont),
        titleSmall = TYPOGRAPHY.titleSmall.copy(fontFamily = selectedFont),
        bodyLarge = TYPOGRAPHY.bodyLarge.copy(fontFamily = selectedFont),
        bodyMedium = TYPOGRAPHY.bodyMedium.copy(fontFamily = selectedFont),
        bodySmall = TYPOGRAPHY.bodySmall.copy(fontFamily = selectedFont),
        labelLarge = TYPOGRAPHY.labelLarge.copy(fontFamily = selectedFont),
        labelMedium = TYPOGRAPHY.labelMedium.copy(fontFamily = selectedFont),
        labelSmall = TYPOGRAPHY.labelSmall.copy(fontFamily = selectedFont),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun TypographyPreview() {
    val typography = provideTypography()
    Column {
        Text("Flex Font Emphasis", fontFamily = flexFontEmphasis())
        Text("Flex Font Rounded", fontFamily = flexFontRounded())

        Text("Display Large", style = typography.displayLarge)
        Text("Display Medium", style = typography.displayMedium)
        Text("Display Small", style = typography.displaySmall)
        Text("Headline Large", style = typography.headlineLarge)
        Text("Headline Medium", style = typography.headlineMedium)
        Text("Headline Small", style = typography.headlineSmall)
        Text("Title Large", style = typography.titleLarge)
        Text("Title Medium", style = typography.titleMedium)
        Text("Title Small", style = typography.titleSmall)
        Text("Body Large", style = typography.bodyLarge)
        Text("Body Medium", style = typography.bodyMedium)
        Text("Body Small", style = typography.bodySmall)
        Text("Label Large", style = typography.labelLarge)
        Text("Label Medium", style = typography.labelMedium)
        Text("Label Small", style = typography.labelSmall)
    }
}
