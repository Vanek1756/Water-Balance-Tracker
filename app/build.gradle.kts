import com.android.build.api.dsl.ApplicationBuildFeatures

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.babiichuk.waterbalancetracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.babiichuk.waterbalancetracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release"){
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
    buildFeatures(
        Action<ApplicationBuildFeatures> {
            viewBinding = true
        }
    )
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation components
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Dagger Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // Firebase
    implementation(libs.firebase.auth)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Joda-time
//    implementation(libs.joda.time)

    // Adapter Delegates
    implementation(libs.adapterDelgates)

    // Kotpref
    implementation(libs.kotpref)
    implementation(libs.kotpref.initializer)

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

}