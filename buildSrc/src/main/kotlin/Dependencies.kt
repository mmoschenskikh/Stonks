object AppConfig {
    const val appId = "ru.maxultra.stonks"
    const val versionCode = 1
    const val versionName = "1.0"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"

    object Sdk {
        const val min = 21
        const val compile = 30
        const val target = compile
    }
}

object Versions {
    const val kotlin = "1.4.31"
    const val buildTools = "4.1.0"

    const val ktLint = "9.2.1"

    const val appCompat = "1.2.0"
    const val constraintLayout = "2.0.4"
    const val ktx = "1.3.2"
    const val material = "1.2.0"
    const val room = "2.2.6"

    const val jUnit = "4.13.2"
    const val extJUnit = "1.1.1"
    const val espresso = "3.3.0"
}

object BuildPlugins {
    const val gradle = "com.android.tools.build:gradle:${Versions.buildTools}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val ktLint = "org.jlleitschuh.gradle.ktlint"
}

object Android {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.ktx}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
}

object Dependencies {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val ktLint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktLint}"
    const val materialDesign = "com.google.android.material:material:${Versions.material}"
}

object Room {
    const val compiler = "androidx.room:room-compiler:${Versions.room}"
    const val ktx = "androidx.room:room-ktx:${Versions.room}"
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
}

object Testing {
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val extJUnit = "androidx.test.ext:junit:${Versions.extJUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}
