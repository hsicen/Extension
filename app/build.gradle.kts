plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = DepVersion.compileSdk

    defaultConfig {
        applicationId = "com.hsicen.extension"
        minSdk = DepVersion.minSdk
        targetSdk = DepVersion.targetSdk
        versionCode = DepVersion.versionCode
        versionName = DepVersion.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(Deps.fileMap))
    implementation(Deps.kotlinStb)
    implementation(Deps.appCompat)
    implementation(Deps.coreKtx)
    implementation(Deps.constrainLayout)
    implementation(project(":extension"))
}
