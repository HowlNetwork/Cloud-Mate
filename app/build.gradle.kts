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
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
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
    implementation("androidx.compose.material:material-icons-extended:1.7.5")

    // Thêm thư viện Material Design 2
    implementation(libs.androidx.material)

    // Hilt Dagger
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)

    implementation(libs.vision.internal.vkp)

    // Navigation
    implementation(libs.androidx.navigation.compose.v273)

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

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)

    // Google play's location library
    implementation (libs.play.services.location)

    // provides Android runtime permissions support for Jetpack Compose
    implementation(libs.accompanist.permissions)

    // To use the Palette class from the AndroidX Palette library
    implementation(libs.androidx.palette.ktx)

    testImplementation(libs.junit)

    // Thư viện Mockito để tạo đối tượng giả
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlin.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}