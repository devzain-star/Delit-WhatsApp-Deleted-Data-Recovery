plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.firebase.firebase.perf)
}

android {
    namespace = "com.recover.deleted.messages.chat.recovery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.recover.deleted.messages.chat.recovery"
        minSdk = 23
        targetSdk = 35
        versionCode = 17
        versionName = "1.1.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Splash API
    implementation (libs.androidx.core.splashscreen)

    //ViewPager
    implementation (libs.androidx.viewpager2)

    //sdp
    implementation (libs.sdp.android)

    //common io
    implementation(files("libs/commons-io-2.4.jar"))

    //glide
    implementation (libs.glide)
    implementation (libs.picasso)

    //AutoUpdate
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    //GSON
    implementation (libs.gson)

    //WorkManager
    implementation (libs.androidx.work.runtime.ktx)

}