plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mycontactlist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mycontactlist"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
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
//    packagingOptions {
//        exclude 'META-INF/androidx.localbroadcastmanager_localbroadcastmanager.version'
//        exclude 'META-INF/android.support.localbroadcastmanager.version'
//    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    //implementation(libs.design)
    implementation(libs.multidex)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.maps.license)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}