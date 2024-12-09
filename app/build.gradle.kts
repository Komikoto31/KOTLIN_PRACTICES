plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.pr7"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pr7"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {

    implementation("androidx.test.ext:junit-ktx:1.2.1")
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines android:1.6.4")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("com.android.support.test:rules:1.0.2")

    testImplementation ("org.mockito:mockito-core:5.2.0")
    testImplementation ("org.mockito:mockito-android:5.2.0")

    androidTestImplementation("androidx.test.espresso:espressocore:3.5.1")
    androidTestImplementation("androidx.test.espresso:espressocontrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espressointents:3.5.1")
    androidTestImplementation("androidx.test.espresso:espressoaccessibility:3.5.1")
    androidTestImplementation("androidx.test.espresso:espressoweb:3.5.1")
    androidTestImplementation("androidx.test.espresso.idling:idlingconcurrent:3.5.1")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    testImplementation ("io.mockk:mockk:1.12.0")
    testImplementation ("org.robolectric:robolectric:4.9")



    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}