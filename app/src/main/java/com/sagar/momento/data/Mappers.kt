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
package com.sagar.momento.data

import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.MontageOptions
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.data.database.DayEntity
import com.sagar.momento.data.database.MontageOptionsEntity
import com.sagar.momento.data.database.ProjectEntity

fun ProjectEntity.toProject(): Project {
    return Project(id = id, title = title, description = description, alarm = alarm)
}

fun Project.toEntity(): ProjectEntity {
    return ProjectEntity(id = id, title = title, description = description, alarm = alarm)
}

fun DayEntity.toDay(): Day {
    return Day(
        id = id,
        projectId = projectId,
        date = date,
        image = image,
        comment = comment,
        isFavorite = isFavorite,
        faceData = faceData,
    )
}

fun Day.toDayEntity(): DayEntity {
    return DayEntity(
        id = id,
        projectId = projectId,
        date = date,
        image = image,
        comment = comment,
        isFavorite = isFavorite,
        faceData = faceData,
    )
}

fun MontageOptionsEntity.toMontageOptions(): MontageOptions {
    return MontageOptions(
        projectId = projectId,
        framesPerImage = framesPerImage,
        framesPerSecond = framesPerSecond,
        videoQuality = videoQuality,
        backgroundColor = backgroundColor,
        waterMark = waterMark,
        showDate = showDate,
        showMessage = showMessage,
        font = font,
        dateStyle = dateStyle,
        stabilizeFaces = stabilizeFaces,
        censorFaces = censorFaces,
    )
}

fun MontageOptions.toMontageOptionsEntity(): MontageOptionsEntity {
    return MontageOptionsEntity(
        projectId = projectId,
        framesPerImage = framesPerImage,
        framesPerSecond = framesPerSecond,
        videoQuality = videoQuality,
        backgroundColor = backgroundColor,
        waterMark = waterMark,
        showDate = showDate,
        showMessage = showMessage,
        font = font,
        dateStyle = dateStyle,
        stabilizeFaces = stabilizeFaces,
        censorFaces = censorFaces,
    )
}
