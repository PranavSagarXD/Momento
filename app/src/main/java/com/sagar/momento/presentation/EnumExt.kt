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
package com.sagar.momento.presentation

import com.sagar.momento.R
import com.sagar.momento.core.enums.AppTheme
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.PaletteStyle

fun AppTheme.toStringRes(): Int {
    return when (this) {
        LIGHT -> R.string.theme_light
        DARK -> R.string.theme_dark
        SYSTEM -> R.string.theme_system
    }
}

fun Fonts.toDisplayString(): String {
    return when (this) {
        INTER -> "Inter"
        POPPINS -> "Poppins"
        MANROPE -> "Manrope"
        MONTSERRAT -> "Montserrat"
        FIGTREE -> "Figtree"
        QUICKSAND -> "Quicksand"
        GOOGLE_SANS -> "Google Sans"
        SYSTEM_DEFAULT -> "System Default"
    }
}

fun PaletteStyle.toMPaletteStyle(): com.materialkolor.PaletteStyle {
    return when (this) {
        TONALSPOT -> TonalSpot
        NEUTRAL -> Neutral
        VIBRANT -> Vibrant
        EXPRESSIVE -> Expressive
        RAINBOW -> Rainbow
        FRUITSALAD -> FruitSalad
        MONOCHROME -> Monochrome
        FIDELITY -> Fidelity
        CONTENT -> Content
    }
}
