plugins {
    commons.application
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "com.virogu.calculator"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdkVersion
        versionCode = Versions.buildVersionCode
        versionName = Versions.buildVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            //isMinifyEnabled = true
            //isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            //applicationIdSuffix = ".debug"
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        //useIR = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeVersion
    }

//    lint {
//        baseline = file("lint-baseline.xml")
//    }

}

dependencies {
    application()
    composeCore()
    lifecycle()
    // https://mvnrepository.com/artifact/net.objecthunter/exp4j
    implementation("net.objecthunter:exp4j:0.4.8")
    test()
}
