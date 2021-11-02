plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    sourceSets.getByName("main") {
        manifest.srcFile("src/main/AndroidManifest.xml")
        java.srcDirs("src/main/java")
        resources.srcDirs("src/main/res")
    }
}

dependencies {
    implementation(Dependencies.appCompat)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.material)

    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.androidxExtJunit)
}

apply {
    from("../publishing/maven.gradle")
}