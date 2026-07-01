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
package com.sagar.momento.facedetection

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import kotlin.math.atan2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.sagar.momento.core.data_classes.FaceData
import com.sagar.momento.core.getBitmapWithRotation
import com.sagar.momento.core.interfaces.FaceDetector

class FaceDetectorImpl(private val context: Context) : FaceDetector {

    companion object {
        const val TAG = "FaceDetector"
        private const val MP_FACE_LANDMARKER_TASK = "face_landmarker.task"
        const val DEFAULT_FACE_DETECTION_CONFIDENCE = 0.5F
        const val DEFAULT_FACE_TRACKING_CONFIDENCE = 0.5F
        const val DEFAULT_FACE_PRESENCE_CONFIDENCE = 0.5F
        const val DEFAULT_NUM_FACES = 1
    }

    private val faceLandmarker: FaceLandmarker by lazy {
        val baseOptionsBuilder = BaseOptions.builder().setModelAssetPath(MP_FACE_LANDMARKER_TASK)
        val baseOptions = baseOptionsBuilder.build()
        val optionsBuilder =
            FaceLandmarker.FaceLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinFaceDetectionConfidence(DEFAULT_FACE_DETECTION_CONFIDENCE)
                .setMinTrackingConfidence(DEFAULT_FACE_TRACKING_CONFIDENCE)
                .setMinFacePresenceConfidence(DEFAULT_FACE_PRESENCE_CONFIDENCE)
                .setNumFaces(DEFAULT_NUM_FACES)
                .setRunningMode(RunningMode.IMAGE)
        val options = optionsBuilder.build()

        FaceLandmarker.createFromOptions(context, options)
    }

    override suspend fun getFaceDataFromUri(uri: Uri): FaceData =
        withContext(Dispatchers.Default) {
            val imageBitmap = uri.getBitmapWithRotation(context)
            if (imageBitmap == null) {
                Log.e(TAG, "Could not decode image from Uri")
                return@withContext FaceData()
            }

            val mpImage = BitmapImageBuilder(imageBitmap).build()
            if (mpImage == null) {
                Log.e(TAG, "Could not process image bitmap")
                return@withContext FaceData()
            }

            val width = imageBitmap.width
            val height = imageBitmap.height

            val landmarks =
                try {
                    val result = faceLandmarker.detect(mpImage)
                    result.faceLandmarks().firstOrNull()
                } catch (e: Exception) {
                    Log.e(TAG, "Face Detection Failed", e)
                    null
                } finally {
                    imageBitmap.recycle()
                }
            if (landmarks == null) {
                Log.e(TAG, "Could not extract landmarks")
                return@withContext FaceData()
            }

            try {
                var minX = 1.0f
                var minY = 1.0f
                var maxX = 0.0f
                var maxY = 0.0f

                for (landmark in landmarks) {
                    minX = minOf(minX, landmark.x())
                    minY = minOf(minY, landmark.y())
                    maxX = maxOf(maxX, landmark.x())
                    maxY = maxOf(maxY, landmark.y())
                }

                val left = (minX * width).toInt()
                val top = (minY * height).toInt()
                val right = (maxX * width).toInt()
                val bottom = (maxY * height).toInt()

                val leftEye = landmarks[33]
                val rightEye = landmarks[263]

                val headAngle =
                    Math.toDegrees(
                            atan2(
                                ((rightEye.y() - leftEye.y()) * height).toDouble(),
                                ((rightEye.x() - leftEye.x()) * width).toDouble(),
                            )
                        )
                        .toFloat()

                FaceData(
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom,
                    headAngle = headAngle,
                )
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected Error while extracting face", e)
                return@withContext FaceData()
            }
        }
}
