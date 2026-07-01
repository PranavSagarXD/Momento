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
import com.diffplug.gradle.spotless.SpotlessExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.koin.compiler) apply false
}

allprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
    configure<SpotlessExtension> {
        kotlin {
            ktfmt(libs.versions.ktfmt.get()).kotlinlangStyle()
            target("src/**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt")
            licenseHeaderFile(rootProject.file("spotless/copyright.txt"))
        }
        kotlinGradle {
            ktfmt(libs.versions.ktfmt.get()).kotlinlangStyle()
            target("*.kts")
            targetExclude("${layout.buildDirectory}/**/*.kts")
            licenseHeaderFile(rootProject.file("spotless/copyright.txt"), "(^(?![\\/ ]\\*).*$)")
            toggleOffOn()
        }
        format("xml") {
            target("src/**/*.xml")
            targetExclude("**/build/", ".idea/")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
