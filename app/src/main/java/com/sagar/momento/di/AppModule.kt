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
package com.sagar.momento.di

import android.content.Context
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import com.sagar.momento.facedetection.FaceDetectorImpl
import com.sagar.momento.core.interfaces.FaceDetector
import com.sagar.momento.core.interfaces.MontageMaker
import com.sagar.momento.core.interfaces.SettingsPrefs
import com.sagar.momento.data.database.DaysDao
import com.sagar.momento.data.database.MontageOptionsDao
import com.sagar.momento.data.database.ProjectDBFactory
import com.sagar.momento.data.database.ProjectDao
import com.sagar.momento.data.database.ProjectDatabase
import com.sagar.momento.data.datastore.DatastoreFactory
import com.sagar.momento.montage.MontageMakerImpl

@Module
@ComponentScan("com.sagar.momento")
class AppModule {
    @Single
    fun provideAppDb(dbFactory: ProjectDBFactory): ProjectDatabase = dbFactory.create().build()

    @Single fun getProjectDao(database: ProjectDatabase): ProjectDao = database.projectDao

    @Single fun getDaysDao(database: ProjectDatabase): DaysDao = database.daysDao

    @Single
    fun getMontageOptionsDao(database: ProjectDatabase): MontageOptionsDao =
        database.montageOptionsDao

    @Single
    fun getSettingsPrefs(datastoreFactory: DatastoreFactory): SettingsPrefs =
        datastoreFactory.settingsPreferences()

    @Single fun getMontageMaker(context: Context): MontageMaker = MontageMakerImpl(context)

    @Single fun getFaceDetector(context: Context): FaceDetector = FaceDetectorImpl(context)
}
