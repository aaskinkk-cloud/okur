plugins { id("com.android.application") }
android {
    namespace = "app.okur.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "app.okur.android"
        minSdk = 26
        targetSdk = 34
        versionCode = (System.getenv("GITHUB_RUN_NUMBER") ?: "1").toInt()
        versionName = "1." + (System.getenv("GITHUB_RUN_NUMBER") ?: "0")
    }
    signingConfigs {
        create("okur") {
            storeFile = file("../okur.jks"); storePassword = "okurokur"; keyAlias = "okur"; keyPassword = "okurokur"
        }
    }
    buildTypes {
        getByName("release") { isMinifyEnabled = false; signingConfig = signingConfigs.getByName("okur") }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
