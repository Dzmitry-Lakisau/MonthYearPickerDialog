plugins {
	id("com.android.application")
	kotlin("android")
}

android {
	compileSdk = 31

	defaultConfig {
		applicationId = "com.example.sample"
		minSdk = 23
		targetSdk = 31
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
	implementation(project(":library"))

	implementation("androidx.appcompat:appcompat:1.4.0-rc01")
	implementation("androidx.constraintlayout:constraintlayout:2.1.1")
	implementation("com.google.android.material:material:1.5.0-alpha05")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.3")
}