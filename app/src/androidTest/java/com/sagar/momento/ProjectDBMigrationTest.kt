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
package com.sagar.momento

import androidx.room3.Room
import androidx.room3.testing.MigrationTestHelper
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import java.io.File
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.sagar.momento.data.database.ProjectDatabase

@RunWith(AndroidJUnit4::class)
class ProjectDBMigrationTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val dbFile: File = targetContext.getDatabasePath("projects_test.db")

    @get:Rule
    val helper =
        MigrationTestHelper(
            instrumentation = InstrumentationRegistry.getInstrumentation(),
            databaseClass = ProjectDatabase::class,
            driver = AndroidSQLiteDriver(),
            file = dbFile,
        )

    @After
    fun tearDown() {
        if (dbFile.exists()) dbFile.delete()
    }

    @Test
    fun migration1to2_containsCorrectData() = runBlocking {
        // Ensure the database directory exists
        dbFile.parentFile?.mkdirs()

        helper.createDatabase(1).apply {
            // Insert a project into the old version 1 database
            execSQL(
                "INSERT INTO projects_table (id, title, description, alarm) VALUES (1, 'First Project', 'A description for the first project.', '10:00')"
            )

            // Insert a 'day' for that project
            execSQL(
                "INSERT INTO days_table (projectId, image, comment, date, isFavorite) VALUES (1, '/path/to/image.jpg', 'A comment for the day.', 1672531200, 1)"
            )

            close()
        }

        // Run the migration to version 2 and validate the schema.
        val connection = helper.runMigrationsAndValidate(2)

        // Validate the data after migration
        connection.prepare("SELECT * FROM projects_table WHERE id = 1").apply {
            assertThat(step()).isTrue()
            assertThat(getText(1)).isEqualTo("First Project")
            assertThat(getText(2)).isEqualTo("A description for the first project.")
            assertThat(getText(3)).isEqualTo("10:00")
            close()
        }

        connection.prepare("SELECT * FROM days_table WHERE projectId = 1").apply {
            assertThat(step()).isTrue()
            assertThat(getText(2)).isEqualTo("/path/to/image.jpg")
            assertThat(getText(3)).isEqualTo("A comment for the day.")
            assertThat(getLong(4)).isEqualTo(1672531200L)
            assertThat(getLong(5)).isEqualTo(1L)
            close()
        }

        connection.close()
    }

    @Test
    fun testAllMigrations() = runBlocking {
        // Ensure the database directory exists
        dbFile.parentFile?.mkdirs()

        // Create schema 1 and close it.
        helper.createDatabase(1).close()

        // Open the database with the latest version to run all migrations.
        Room.databaseBuilder(targetContext, ProjectDatabase::class.java, dbFile.absolutePath)
            .setDriver(AndroidSQLiteDriver())
            .build()
            .close()
    }
}
