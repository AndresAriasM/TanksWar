plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "com.tankgame"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // LibGDX 1.11.0
    implementation("com.badlogicgames.gdx:gdx:1.11.0")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.11.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.11.0:natives-desktop")
    
    // Kotlin
    implementation(kotlin("stdlib"))
    
    // Testing
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("game.MainKt")
}

// Para Mac: GLFW debe ejecutarse en el thread principal
tasks.withType<JavaExec> {
    if (System.getProperty("os.name").contains("Mac")) {
        jvmArgs("-XstartOnFirstThread")
    }
}

sourceSets {
    main {
        kotlin.srcDir("src/main/kotlin")
        resources.srcDir("src/resources")
    }
}