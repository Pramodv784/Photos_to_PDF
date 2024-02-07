plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tasakiapps.photostopdf"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tasakiapps.photostopdf"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
      viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation( "com.intuit.sdp:sdp-android:1.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.itextpdf:itextpdf:5.5.13.3")
    implementation ("com.dmitryborodin:pdfview-android:1.1.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.1.0-alpha01")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")

}