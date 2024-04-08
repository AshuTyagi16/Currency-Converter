@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.ktlint)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTargetVersion.get()
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core-base"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Share Core Preference Module
            api(project(":shared:core-preferences"))

            // Kotlinx-Serialization
            api(libs.kotlin.serialization)

            // Kotlin Datetime
            api(libs.kotlin.datetime)

            // Coroutine
            implementation(libs.coroutines.core)

            // Kotlin Bignum
            implementation(libs.kotlin.bignum)

            // Koin DI
            implementation(libs.koin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            // ViewModel
            api(libs.androidx.lifecycle.viewmodel)
        }
    }
}

android {
    namespace = "com.multicurrency.app.core_base.shared"
    compileSdk = libs.versions.compileSdkVersion.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdkVersion.get().toInt()
    }
}
