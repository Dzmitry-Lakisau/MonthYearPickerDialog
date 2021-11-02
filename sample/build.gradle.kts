plugins {
	id("com.android.application")
	kotlin("android")
}

android {
	compileSdk = Config.compileSdkVersion

	defaultConfig {
		applicationId = Config.applicationId
		minSdk = Config.minSdkVersion
		targetSdk = Config.targetSdkVersion

		versionCode = Config.versionCode
		versionName = Config.versionName

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

	implementation(Dependencies.appCompat)
	implementation(Dependencies.constraintLayout)
	implementation(Dependencies.material)

	testImplementation(Dependencies.Test.junit)
	androidTestImplementation(Dependencies.Test.androidxExtJunit)
}