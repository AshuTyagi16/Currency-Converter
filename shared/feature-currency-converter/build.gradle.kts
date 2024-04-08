@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.sqldelight)
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
            baseName = "feature-currency-converter"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            // Shared Core Base Module
            implementation(project(":shared:core-base"))

            // Shared Core Network Module
            api(project(":shared:core-network"))

            // Shared Core Logger Module
            implementation(project(":shared:core-logger"))

            // Store5
            implementation(libs.bundles.store)

            // Koin
            implementation(libs.koin)

            // SqlDelight
            implementation(libs.bundles.sqldelight.common)

        }

        commonTest.dependencies {
            // Shared Core Preferences
            implementation(project(":shared:core-preferences"))

            // Test Bundle
            implementation(libs.bundles.shared.commonTest)
        }

        androidMain.dependencies {
            // SqlDelight
            implementation(libs.bundles.sqldelight.android)

            // Koin Android
            implementation(libs.koin.android)
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.bundles.shared.androidTest)
            }
        }

        iosMain.dependencies {
            // SqlDelight
            implementation(libs.bundles.sqldelight.native)
        }
    }
}

val modulePackageName = "com.multicurrency.app.feature_currency_converter.shared"

android {
    namespace = modulePackageName
    compileSdk = libs.versions.compileSdkVersion.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdkVersion.get().toInt()
    }
}

sqldelight {
    databases {
        create("MultiCurrencyDatabase") {
            packageName.set(modulePackageName)
        }
    }
}

task("testClasses").doLast {
    println("This is a dummy testClasses task")
}
