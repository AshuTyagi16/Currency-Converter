import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    id(libs.plugins.build.konfig.get().toString())
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
            baseName = "core-network"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Stdlib
            implementation(libs.kotlin.stdlib)

            // coroutine
            implementation(libs.coroutines.core)

            // Ktor
            api(libs.bundles.ktor.common)

            // Koin
            implementation(libs.koin)

            // Store5
            implementation(libs.bundles.store)

            // Core-Logger Module
            implementation(project(":shared:core-logger"))
        }

        androidMain.dependencies {
            // Ktor (OkHttp Engine)
            implementation(libs.bundles.ktor.android)
        }

        iosMain.dependencies {
            // Ktor (Darwin Engine)
            implementation(libs.bundles.ktor.ios)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

fun readTokenProperties(): Map<String, String> {
    val items = HashMap<String, String>()

    val fl = rootProject.file("token.properties")

    if (fl.exists()) {
        fl.forEachLine {
            items[it.split("=")[0]] = it.split("=")[1]
        }
    }

    return items
}

val tokenProperties = readTokenProperties()

val modulePackageName = "com.multicurrency.app.core_network.shared"

buildkonfig {
    packageName = modulePackageName
    exposeObjectWithName = "CoreNetworkBuildKonfig"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "openexchangerates.org")
        buildConfigField(FieldSpec.Type.STRING, "APP_ID", tokenProperties["app_id"])
    }
}

android {
    namespace = modulePackageName
    compileSdk = libs.versions.compileSdkVersion.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdkVersion.get().toInt()
    }
}

task("testClasses").doLast {
    println("This is a dummy testClasses task")
}