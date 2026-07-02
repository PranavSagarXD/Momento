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
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.koin.compiler)
}

val appName = "Momento"
val appVersionCode = 2001
val appVersionName = project.findProperty("appVersionName") as? String ?: "2.1"
val appNameSpace = "com.sagar.momento"

val gitHash = execute("git", "rev-parse", "HEAD").take(7)

android {
    namespace = appNameSpace
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appNameSpace
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("release.keystore")
            storePassword = "momento123"
            keyAlias = "momento"
            keyPassword = "momento123"
        }
    }

    buildTypes {
        release {
            resValue("string", "app_name", appName)
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }

        create("beta") {
            applicationIdSuffix = ".beta"
            versionNameSuffix = "-beta$gitHash"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    packaging {
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
        jniLibs.keepDebugSymbols.add("**/*.so")
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    splits {
        abi {
            isEnable = false
            isUniversalApk = true
        }
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            output.outputFileName.set("Momento-${appVersionName}-PranavSagarXD.apk")
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-sensitive-resolution")
        optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
        optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
    }
}

dependencies {
    implementation(project(":common:core"))
    implementation(project(":common:montage"))
    implementation(project(":common:facedetection"))

    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.calendar)
    implementation(libs.filekit.core)
    implementation(libs.filekit.dialogs.compose)
    implementation(libs.landscapist.coil)
    implementation(libs.materialKolor)
    implementation(libs.colorpicker.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
    implementation(libs.koin.annotations)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.kotlinx.datetime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.truth)
}

room3 { schemaDirectory("$projectDir/schemas") }

fun execute(vararg command: String): String = try {
    providers.exec { commandLine(*command) }.standardOutput.asText.get().trim()
} catch (_: Exception) {
    "unknown"
}

val generateChangelogJson by
    tasks.registering {
        description = "Generating Changelog for app"
        val inputFile = rootProject.file("CHANGELOG.md")
        val outputDir = file("$projectDir/src/main/assets/")
        val outputFile = File(outputDir, "changelog.json")

        inputs.file(inputFile)
        outputs.file(outputFile)

        doLast {
            if (!outputDir.exists()) outputDir.mkdirs()

            val lines = inputFile.readLines()

            val map = mutableMapOf<String, MutableList<String>>()
            var currentVersion: String? = null

            for (line in lines) {
                when {
                    line.startsWith("## ") -> {
                        currentVersion = line.removePrefix("## ").trim()
                        map[currentVersion] = mutableListOf()
                    }

                    line.startsWith("- ") && currentVersion != null -> {
                        map[currentVersion]?.add(line.removePrefix("- ").trim())
                    }
                }
            }

            val allowedEntries = map.entries.take(10)
            val json = buildString {
                append("[\n")

                allowedEntries.forEachIndexed { index, entry ->
                    append("  {\n")
                    append("    \"version\": \"${entry.key}\",\n")
                    append("    \"changes\": [\n")

                    entry.value.forEachIndexed { i, item ->
                        val escaped = item
                            .replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n")
                            .replace("\r", "\\r")
                            .replace("\t", "\\t")
                        append("      \"$escaped\"")
                        if (i != entry.value.lastIndex) append(",")
                        append("\n")
                    }

                    append("    ]\n")
                    append("  }")

                    if (index != allowedEntries.lastIndex) append(",")
                    append("\n")
                }

                append("]")
            }

            outputFile.writeText(json)
        }
    }

tasks.named("preBuild") { dependsOn(generateChangelogJson) }
