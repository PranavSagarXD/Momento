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
package com.sagar.momento.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import com.sagar.momento.core.interfaces.AlarmScheduler
import com.sagar.momento.core.interfaces.ProjectRepository
import com.sagar.momento.data.AlarmSchedulerImpl
import com.sagar.momento.data.reminderNotification

@Single
class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val pendingResult = goAsync()

        Log.d(TAG, "Received Intent")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (intent != null && intent.action == AlarmSchedulerImpl.Companion.ACTION) {
                    Log.d(TAG, "Processing Notification")

                    val projectRepo = get<ProjectRepository>()
                    val scheduler = get<AlarmScheduler>()

                    val projectId = intent.getLongExtra("project_id", -1)
                    if (projectId < 0L) return@launch

                    val project = projectRepo.getProjectById(projectId) ?: return@launch
                    val lastDay = projectRepo.getLastCompletedDay(projectId)

                    if (lastDay == null || LocalDate.ofEpochDay(lastDay.date) != LocalDate.now()) {
                        reminderNotification(context, project)
                    } else {
                        Log.d(TAG, "Already done")
                    }

                    scheduler.schedule(project)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing intent", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
