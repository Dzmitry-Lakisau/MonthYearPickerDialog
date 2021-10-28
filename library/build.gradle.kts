plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = 31

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.3.0-beta01")
    implementation("androidx.appcompat:appcompat:1.4.0-rc01")
    implementation("androidx.core:core-ktx:1.7.0-rc01")
    implementation("com.google.android.material:material:1.5.0-alpha05")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
}