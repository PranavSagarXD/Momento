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
package com.sagar.momento.data.database

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.json.Json
import com.sagar.momento.core.data_classes.AlarmData
import com.sagar.momento.core.data_classes.FaceData

object Converters {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @ColumnTypeConverter
    fun fromAlarm(alarm: AlarmData?): String? {
        return alarm?.let { json.encodeToString(AlarmData.serializer(), it) }
    }

    @ColumnTypeConverter
    fun toAlarm(data: String?): AlarmData? {
        return data?.let { json.decodeFromString(AlarmData.serializer(), it) }
    }

    @ColumnTypeConverter
    fun toFaceData(data: String?): FaceData? {
        return data?.let { json.decodeFromString(FaceData.serializer(), it) }
    }

    @ColumnTypeConverter
    fun fromFaceData(faceData: FaceData?): String? {
        return faceData?.let { json.encodeToString(FaceData.serializer(), it) }
    }
}
