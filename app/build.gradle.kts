plugins {
    alias(libs.plugins.android.application)

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.gussanxz.organizze"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gussanxz.organizze"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Dependencias Firebase
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))


    // Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)//Analytics firebase
    implementation(libs.firebase.auth)//Auth firebase
    implementation(libs.firebase.firestore)//Firestore firebase. Detabase
    implementation(libs.firebase.storage)//Storeage firebase. Armazenamento de imagem
    //implementation(libs.organizze)
    implementation("com.github.gussanxz:organizze:-919c01f66e-1")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}