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
package com.sagar.momento.core

import java.time.format.FormatStyle
import com.sagar.momento.core.enums.DateStyle
import com.sagar.momento.core.enums.Fonts
import com.sagar.momento.core.enums.VideoQuality

fun DateStyle.toFormatStyle(): FormatStyle {
    return when (this) {
        FULL -> FULL
        LONG -> LONG
        MEDIUM -> MEDIUM
        SHORT -> SHORT
    }
}

fun Fonts.toFontRes(): Int? {
    return when (this) {
        INTER -> R.font.inter
        POPPINS -> R.font.poppins
        MANROPE -> R.font.manrope
        MONTSERRAT -> R.font.montserrat
        FIGTREE -> R.font.figtree
        QUICKSAND -> R.font.quicksand
        GOOGLE_SANS -> R.font.google_sans_flex
        SYSTEM_DEFAULT -> null
    }
}

fun VideoQuality.toDimensions(): Pair<Int, Int> {
    return when (this) {
        SMALL -> Pair(768, 1024)
        HD -> Pair(1080, 1440)
    }
}
