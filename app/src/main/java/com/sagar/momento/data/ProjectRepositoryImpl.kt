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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import com.sagar.momento.core.data_classes.Day
import com.sagar.momento.core.data_classes.MontageOptions
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.data_classes.ProjectListData
import com.sagar.momento.core.interfaces.ProjectRepository
import com.sagar.momento.data.database.DaysDao
import com.sagar.momento.data.database.MontageOptionsDao
import com.sagar.momento.data.database.ProjectDao

@Single(binds = [ProjectRepository::class])
class ProjectRepositoryImpl(
    private val projectDao: ProjectDao,
    private val daysDao: DaysDao,
    private val montageOptionsDao: MontageOptionsDao,
) : ProjectRepository {
    override fun getProjectListData(): Flow<List<ProjectListData>> {
        return projectDao.getProjects().map { flow ->
            flow.map { project ->
                ProjectListData(
                    project = project.toProject(),
                    last10Days = daysDao.getLastDaysByProjectId(project.id).map { it.toDay() },
                )
            }
        }
    }

    override suspend fun upsertProject(project: Project) {
        projectDao.upsertProject(project.toEntity())
    }

    override suspend fun deleteProject(project: Project) {
        projectDao.deleteProject(project.toEntity())
    }

    override suspend fun getProjectById(id: Long): Project? {
        return projectDao.getProjectById(id)?.toProject()
    }

    override fun getDays(): Flow<List<Day>> {
        return daysDao.getDays().map { flow -> flow.map { it.toDay() } }
    }

    override suspend fun upsertDay(day: Day) {
        return daysDao.upsertDay(day.toDayEntity())
    }

    override suspend fun deleteDay(day: Day) {
        return daysDao.deleteDay(day.toDayEntity())
    }

    override suspend fun getLastCompletedDay(projectId: Long): Day? {
        return daysDao.getLastDaysByProjectId(projectId).firstOrNull()?.toDay()
    }

    override suspend fun getMontageOptionsByProjectId(projectId: Long): MontageOptions? {
        return montageOptionsDao.getMontageOptionByProjectId(projectId)?.toMontageOptions()
    }

    override suspend fun upsertMontageOptions(montageOptions: MontageOptions) {
        montageOptionsDao.upsertMontageOption(montageOptions.toMontageOptionsEntity())
    }

    override suspend fun deleteMontageOptions(montageOptions: MontageOptions) {
        montageOptionsDao.deleteMontageOption(montageOptions.toMontageOptionsEntity())
    }
}
