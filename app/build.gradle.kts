plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.studymate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.studymate"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}

// ✅ Kotlin DSL: dùng val
val room_version = "2.8.3"

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.navigation:navigation-fragment:2.9.6")
    implementation("androidx.navigation:navigation-ui:2.9.6")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.4")
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.4")


    // Room dependencies
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:2.8.3")

    // Nếu muốn dùng LiveData với Room
    implementation("androidx.room:room-ktx:$room_version")
}
