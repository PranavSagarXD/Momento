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
package com.sagar.momento.presentation.project

import android.content.Context
import com.sagar.momento.core.data_classes.AlarmData
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.MontageConfig
import com.sagar.momento.core.data_classes.PlayerAction
import com.sagar.momento.core.data_classes.Project

sealed interface ProjectAction {
    data class OnInitializeExoPlayer(val context: Context) : ProjectAction

    data class OnPlayerAction(val playerAction: PlayerAction) : ProjectAction

    data class OnUpdateReminder(val alarmData: AlarmData? = null) : ProjectAction

    data object OnUpdateDays : ProjectAction

    data class OnUpdateProject(val project: Project) : ProjectAction

    data class OnDeleteProject(val project: Project) : ProjectAction

    data class OnUpsertDay(val day: Day, val isNewImage: Boolean = false) : ProjectAction

    data class OnDeleteDay(val day: Day) : ProjectAction

    data class OnCreateMontage(val days: List<Day>) : ProjectAction

    data object OnClearMontageState : ProjectAction

    data class OnEditMontageConfig(val config: MontageConfig) : ProjectAction

    data object OnResetMontagePrefs : ProjectAction

    data object OnStartFaceScan : ProjectAction

    data object OnResetScanState : ProjectAction
}
