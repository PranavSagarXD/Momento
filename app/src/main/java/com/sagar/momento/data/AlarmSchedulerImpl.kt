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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import org.koin.core.annotation.Single
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.interfaces.AlarmScheduler
import com.sagar.momento.data.receiver.AlarmReceiver

@Single(binds = [AlarmScheduler::class])
class AlarmSchedulerImpl(private val context: Context) : AlarmScheduler {

    companion object {
        private const val TAG = "NotificationAlarmScheduler"
        const val ACTION = "notification_reminder"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(project: Project) {
        cancel(project)
        if (project.alarm == null) return

        val time = LocalTime.ofSecondOfDay(project.alarm!!.time)
        val now = LocalDateTime.now()
        val today = now.toLocalDate()

        val scheduledDate = if (time.isAfter(now.toLocalTime())) today else today.plusDays(1)

        val nextDateTime = LocalDateTime.of(scheduledDate, time).atZone(ZoneId.systemDefault())

        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                action = ACTION
                putExtra("project_id", project.id)
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                project.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextDateTime.toEpochSecond() * 1000,
            pendingIntent,
        )

        Log.d(TAG, "Scheduled alarm for ${project.title} at $nextDateTime)")
    }

    override fun cancel(project: Project) {
        val intent =
            Intent(context, AlarmReceiver::class.java).apply {
                action = ACTION
                putExtra("project_id", project.id)
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                project.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Cancelled alarm for project=${project.id}")
    }

    override fun cancelAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll()
        }
    }
}
