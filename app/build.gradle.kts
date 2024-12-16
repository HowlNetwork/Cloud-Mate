plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android") // Plugin Hilt
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.cloudmate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cloudmate"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.cloudmate.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    sourceSets {
        getByName("debug").manifest.srcFile("src/debug/AndroidManifest.xml")
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/NOTICE.txt"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Thêm thư viện Material Icons
    implementation(libs.androidx.material.icons.extended)

    // Thêm thư viện Material Design 2
    implementation(libs.androidx.material)

    // Hilt Dagger
    implementation(libs.hilt.android)
    implementation(libs.vision.internal.vkp)
    implementation(libs.androidx.hilt.common)
    kapt(libs.hilt.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose.v273)
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Env
    implementation(libs.dotenv.kotlin)

    //Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.kotlinx.coroutines.play.services)

    // To use Kotlin coroutines with lifecycle-aware components
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx.v251)

    //Retrofit
    implementation (libs.retrofit)

    //GSON converter
    implementation (libs.converter.gson)

    // Coil
    implementation(libs.coil.compose)

    // OkHttp
    implementation(libs.okhttp)

    //WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)

    // Google play's location library
    implementation (libs.play.services.location)

    // provides Android runtime permissions support for Jetpack Compose
    implementation(libs.accompanist.permissions)

    testImplementation(libs.junit)

    // Thư viện Mockito để tạo đối tượng giả
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlin.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation (libs.androidx.room.testing)
    testImplementation(libs.mockk)
    testImplementation (libs.hilt.android.testing)

    kaptTest(libs.hilt.compiler)

    testAnnotationProcessor(libs.hilt.compiler)

    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)

    kaptAndroidTest(libs.hilt.compiler)

    androidTestAnnotationProcessor(libs.hilt.compiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}