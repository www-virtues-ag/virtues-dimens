import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka.jetbrains)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootProject.layout.projectDirectory.dir("DOCS/ALL/HTML"))
}

tasks.dokkaGfm.configure {
    outputDirectory.set(rootProject.layout.projectDirectory.dir("DOCS/ALL/MARKDOWN"))
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(rootProject.layout.projectDirectory.dir("DOCS/ALL/JAVADOC"))
}

android {
    namespace = "ag.virtues.dimens.all"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.get()
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)

    api(project(":virtues_dynamic"))
    api(project(":virtues_ssps"))
    api(project(":virtues_sdps"))
    api(project(":virtues_library"))

    dokkaPlugin(libs.android.documentation.plugin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
