plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.ktLint)
}

android {
    compileSdkVersion(AppConfig.Sdk.compile)

    defaultConfig {
        applicationId = AppConfig.appId
        minSdkVersion(AppConfig.Sdk.min)
        targetSdkVersion(AppConfig.Sdk.target)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.testRunner
    }

    buildFeatures.viewBinding = true

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/gradle/incremental.annotation.processors")
    }
}

dependencies {
    implementation(Dependencies.kotlin)
    implementation(Dependencies.materialDesign)

    implementation(Android.appcompat)
    implementation(Android.constraintLayout)
    implementation(Android.coreKtx)

    implementation(Room.runtime)
    implementation(Room.ktx)
    kapt(Room.compiler)

    implementation(Lifecycle.liveData)

    implementation(Moshi.moshi)
    implementation(Moshi.codeGen)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.moshiRetrofitConverter)

    implementation(Coroutines.core)
    implementation(Coroutines.android)

    implementation(Navigation.navigationFragmentKtx)
    implementation(Navigation.navigationUiKtx)

    testImplementation(Testing.jUnit)
    androidTestImplementation(Testing.extJUnit)
    androidTestImplementation(Testing.espresso)
}

ktlint {
    android.set(true)
    outputColorName.set("RED")
}
