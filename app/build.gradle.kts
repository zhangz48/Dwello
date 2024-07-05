plugins {
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt") // Apply Kotlin Kapt plugin
}

val activityVersion by extra("1.9.0")
val appCompatVersion by extra("1.7.0")
val constraintLayoutVersion by extra("2.1.4")
val coreTestingVersion by extra("2.2.0")
val coroutinesVersion by extra("1.7.3")
val lifecycleVersion by extra("2.8.2")
val materialVersion by extra("1.6.8")
val roomVersion by extra("2.6.1")
val junitVersion by extra("4.13.2")
val espressoVersion by extra("3.4.0")
val androidxJunitVersion by extra("1.1.5")

android {
    namespace = "com.example.dwello"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dwello"
        minSdk = 34
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Add Jetpack Navigation Compose dependency
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Room dependencies
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$rootProject.lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$rootProject.lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$rootProject.lifecycleVersion")

    // Kotlin coroutines
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$rootProject.coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$rootProject.coroutines")

    // UI
    implementation("androidx.constraintlayout:constraintlayout:$rootProject.constraintLayoutVersion")
    implementation("com.google.android.material:material:$rootProject.materialVersion")

    // Testing
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.arch.core:core-testing:$coreTestingVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    // Other
    implementation("androidx.compose.material:material-icons-extended:$materialVersion")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}