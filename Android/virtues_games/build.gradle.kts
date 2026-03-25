import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.dokka.jetbrains)
}

val isJitPack = System.getenv("JITPACK") == "true"
        || System.getenv("jitpack") == "true"
        || System.getenv("CI") == "true"
        || System.getenv("ci") == "true"

tasks.dokkaHtml.configure {
    outputDirectory.set(rootProject.layout.projectDirectory.dir("DOCS/GAMES/HTML"))
}

tasks.dokkaGfm.configure {
    outputDirectory.set(rootProject.layout.projectDirectory.dir("DOCS/GAMES/MARKDOWN"))
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(rootProject.layout.projectDirectory.dir("DOCS/GAMES/JAVADOC"))
}

mavenPublishing {
    coordinates("ag.virtues.dimens", "virtues-games", "1.0.0")

    configure(
        AndroidSingleVariantLibrary(
            publishJavadocJar = true,
            sourcesJar = true
        )
    )

    pom {
        name.set("AppDimens Games: C++ Native Game Development Support")
        description.set(
            "A specialized AppDimens module designed for Android game development with C++/NDK support. " +
                    "Provides responsive dimensioning, game-specific utilities, and native performance optimizations " +
                    "for game engines like Cocos2d-x, OpenGL ES, and Vulkan. Includes game UI scaling, " +
                    "viewport management, and cross-platform dimension consistency. " +
                    "(android, kotlin, java, c++, ndk, games, opengl, vulkan, cocos2d-x, responsive, dimensions, " +
                    "game-development, native-performance)"
        )
        url.set("https://github.com/www-virtues-ag/virtues-dimens-dimens")
        inceptionYear.set("2025")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("bodenberg")
                name.set("Jean Bodenberg")
                email.set("jean.bodenberg@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:github.com/www-virtues-ag/virtues-dimens-dimens.git")
            developerConnection.set("scm:git:ssh://github.com/www-virtues-ag/virtues-dimens-dimens.git")
            url.set("https://github.com/www-virtues-ag/virtues-dimens-dimens")
        }
    }
    
if (!isJitPack) {
        signAllPublications()
        publishToMavenCentral()
    }
}

publishing {
    repositories {
        maven {
            name = "SonaType"
            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/")
            credentials {
                username = project.findProperty("mavenCentralUsername") as String?
                password = project.findProperty("mavenCentralPassword") as String?
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/www-virtues-ag/virtues-dimens-dimens")
            credentials {
                username = project.findProperty("gpr.user") as String?
                password = project.findProperty("gpr.key") as String?
            }
        }
    }
}

android {
    namespace = "ag.virtues.dimens.games"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        
        // NDK configuration for C++ support
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        }
        
        externalNativeBuild {
            cmake {
                cppFlags += listOf("-std=c++17", "-frtti", "-fexceptions")
                arguments += listOf("-DANDROID_STL=c++_shared")
            }
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
    
    // CMake configuration for C++ native code
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            // Use CMake 3.22.1 which is available on JitPack
            version = "3.22.1"
        }
    }
    
    // Source sets for native code
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("src/main/jniLibs")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    api(project(":appdimens_library"))
    api(project(":appdimens_dynamic"))
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    // OpenGL ES support for games
    implementation(libs.androidx.graphics.core)
    
    dokkaPlugin(libs.android.documentation.plugin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
