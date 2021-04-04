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
    const val coordinatorLayout = "1.1.0"
    const val ktx = "1.3.2"
    const val material = "1.2.0"
    const val navigation = "2.3.0"
    const val room = "2.2.6"
    const val moshi = "1.11.0"
    const val retrofit = "2.9.0"
    const val coroutines = "1.4.2"
    const val lifecycle = "2.3.0"
    const val coil = "1.1.1"
    const val swipeToRefresh = "1.1.0"

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
    const val kotlinSafeArgs = "androidx.navigation.safeargs.kotlin"
}

object Android {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.ktx}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coordinatorLayout =
        "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinatorLayout}"
    const val swipeToRefresh =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeToRefresh}"
}

object Coroutines {
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
}

object Dependencies {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val ktLint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktLint}"
    const val materialDesign = "com.google.android.material:material:${Versions.material}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"
}

object Lifecycle {
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
}

object Navigation {
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val safeArgs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}

object Moshi {
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val codeGen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiRetrofitConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
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
