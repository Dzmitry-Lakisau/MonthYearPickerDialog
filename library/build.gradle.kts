plugins {
    id("com.android.library")
    id("org.jetbrains.dokka") version "1.6.0"
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

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    outputDirectory.set(rootDir.resolve("docs/${outputDirectory.get().absolutePath.split("\\").last()}"))
}

apply {
    from("../publishing/maven.gradle")
}