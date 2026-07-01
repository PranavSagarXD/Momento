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
package com.sagar.momento.montage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toRectF
import androidx.core.net.toUri
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.MontageConfig
import com.sagar.momento.core.data_classes.isValid
import com.sagar.momento.core.data_classes.toRect
import com.sagar.momento.core.getBitmapWithRotation
import com.sagar.momento.core.interfaces.MontageMaker
import com.sagar.momento.core.interfaces.MontageState
import com.sagar.momento.core.toDimensions
import com.sagar.momento.core.toFontRes
import com.sagar.momento.core.toFormatStyle

class MontageMakerImpl(private val context: Context) : MontageMaker {

    override suspend fun createMontageFlow(
        days: List<Day>,
        config: MontageConfig,
    ): Flow<MontageState> = flow {
        val file = File(context.cacheDir, "temp.mp4")
        val muxer = Muxer(context, file)
        muxer.setMuxerConfig(muxer.getMuxerConfig().update(config))

        val sortedDays = days.sortedBy { it.date }
        val total = sortedDays.size

        val textPaint =
            Paint().apply {
                color = Color.WHITE
                alpha = 255
                isAntiAlias = true
                style = Paint.Style.FILL
                setShadowLayer(4f, 2f, 2f, Color.BLACK)
                textSize = config.videoQuality.toDimensions().first * 0.04f
                config.font.toFontRes()?.let { typeface = ResourcesCompat.getFont(context, it) }
            }

        val censorPaint =
            Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = config.backgroundColor.toArgb()
            }

        var processedCount = 0
        val bitmapFlow = flow {
            sortedDays.forEach { day ->
                processDay(
                        day = day,
                        config = config,
                        textPaint = textPaint,
                        censorPaint = censorPaint,
                    )
                    ?.let { bitmap -> emit(bitmap) }
            }
        }

        val flowForMuxer =
            bitmapFlow.onEach {
                processedCount++
                emit(MontageState.ProcessingImages(processedCount.toFloat() / total))
            }

        when (val result = muxer.mux(flowForMuxer)) {
            is MuxingResult.MuxingError -> {
                emit(MontageState.Error(result.message, result.exception))
            }

            is MuxingResult.MuxingSuccess -> {
                emit(MontageState.Success(result.file, config))
            }
        }
    }

    private fun processDay(
        day: Day,
        config: MontageConfig,
        textPaint: Paint,
        censorPaint: Paint,
    ): Bitmap? {
        val dimensions = config.videoQuality.toDimensions()

        return try {
            val uri = day.image.toUri()
            val originalBitmap = uri.getBitmapWithRotation(context)

            if (originalBitmap != null) {
                val canvasBitmap = createBitmap(dimensions.first, dimensions.second)
                try {
                    val canvas = Canvas(canvasBitmap)
                    canvas.drawColor(config.backgroundColor.toArgb())

                    val matrix = Matrix()

                    if (config.stabilizeFaces && day.faceData.isValid()) {
                        val faceBox = day.faceData?.toRect()!!
                        val targetFaceHeight = dimensions.second * 0.3f
                        val scale = targetFaceHeight / faceBox.height().toFloat()

                        val faceCenterX = faceBox.centerX().toFloat()
                        val faceCenterY = faceBox.centerY().toFloat()

                        val targetCenterX = dimensions.first / 2f
                        val targetCenterY = dimensions.second / 2f

                        // move face center to (0,0)
                        // --- Build transform ---
                        matrix.postTranslate(-faceCenterX, -faceCenterY)
                        matrix.postScale(scale, scale) // scale to target size
                        matrix.postRotate(-day.faceData!!.headAngle) // straighten roll
                        matrix.postTranslate(targetCenterX, targetCenterY) // center face
                    } else {
                        // No stabilization, just fit into canvas
                        val scale =
                            minOf(
                                dimensions.first.toFloat() / originalBitmap.width,
                                dimensions.second.toFloat() / originalBitmap.height,
                            )
                        val dx = (dimensions.first - originalBitmap.width * scale) / 2f
                        val dy = (dimensions.second - originalBitmap.height * scale) / 2f
                        matrix.postScale(scale, scale)
                        matrix.postTranslate(dx, dy)
                    }

                    canvas.drawBitmap(originalBitmap, matrix, null)

                    // --- Watermark, date, message ---
                    if (config.waterMark) {
                        val watermark = "PranavSagarXD/Momento"
                        val paddingX = dimensions.first * 0.05f
                        val paddingY =
                            dimensions.second * 0.05f + textPaint.descent() + textPaint.textSize
                        canvas.drawText(watermark, paddingX, paddingY, textPaint)
                    }
                    if (config.showDate) {
                        val date =
                            LocalDate.ofEpochDay(day.date)
                                .format(
                                    DateTimeFormatter.ofLocalizedDate(
                                        config.dateStyle.toFormatStyle()
                                    )
                                )
                        val paddingX = dimensions.first * 0.05f
                        val paddingY =
                            dimensions.second - dimensions.second * 0.05f - textPaint.descent()
                        canvas.drawText(date, paddingX, paddingY, textPaint)
                    }
                    if (config.showMessage && !day.comment.isNullOrBlank()) {
                        val message = day.comment!!
                        val paddingX = dimensions.first * 0.05f
                        val paddingY =
                            dimensions.second - dimensions.second * 0.12f - textPaint.descent()
                        canvas.drawText(message, paddingX, paddingY, textPaint)
                    }

                    // censor face
                    if (config.censorFaces && config.stabilizeFaces && day.faceData.isValid()) {
                        val faceBox = day.faceData?.toRect()!!
                        val faceRectF = faceBox.toRectF()
                        matrix.mapRect(faceRectF)

                        canvas.drawRect(faceRectF, censorPaint)
                    }

                    canvasBitmap
                } finally {
                    if (!originalBitmap.isRecycled) originalBitmap.recycle()
                }
            } else null
        } catch (e: Exception) {
            Log.e("MontageMaker", "Error processing day: ", e)
            null
        }
    }
}
