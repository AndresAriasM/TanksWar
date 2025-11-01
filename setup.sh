#!/bin/bash

# Script de configuración para proyecto de juego en Kotlin con LibGDX
# Uso: bash setup_kotlin_game.sh <nombre_proyecto>

if [ -z "$1" ]; then
    echo "❌ Por favor proporciona un nombre para el proyecto"
    echo "Uso: bash setup_kotlin_game.sh nombre_proyecto"
    exit 1
fi

PROJECT_NAME=$1
PROJECT_DIR=$(pwd)/$PROJECT_NAME

echo "🎮 Iniciando configuración del proyecto: $PROJECT_NAME"
echo "📁 Directorio: $PROJECT_DIR"
echo ""

# Crear directorio principal
mkdir -p "$PROJECT_DIR"
cd "$PROJECT_DIR"

echo "📂 Creando estructura de carpetas..."

# Estructura de carpetas principal
mkdir -p src/main/kotlin/game
mkdir -p src/main/kotlin/game/screens
mkdir -p src/main/kotlin/game/entities
mkdir -p src/main/kotlin/game/utils
mkdir -p src/main/kotlin/game/maps
mkdir -p src/main/kotlin/game/managers
mkdir -p src/resources
mkdir -p src/resources/assets
mkdir -p src/resources/assets/images
mkdir -p src/resources/assets/sounds
mkdir -p src/resources/assets/fonts
mkdir -p build
mkdir -p lib

echo "✅ Carpetas creadas"
echo ""

# Crear archivo build.gradle.kts
echo "📝 Creando build.gradle.kts..."
cat > build.gradle.kts << 'EOF'
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
    // LibGDX
    implementation("com.badlogicgames.gdx:gdx:1.12.1")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.12.1")
    implementation("com.badlogicgames.gdx:gdx-platform:1.12.1:natives-desktop")
    
    // Kotlin
    implementation(kotlin("stdlib"))
    
    // Testing
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("game.GameApplicationKt")
}

sourceSets {
    main {
        kotlin.srcDir("src/main/kotlin")
        resources.srcDir("src/resources")
    }
}
EOF
echo "✅ build.gradle.kts creado"
echo ""

# Crear archivo settings.gradle.kts
echo "📝 Creando settings.gradle.kts..."
cat > settings.gradle.kts << 'EOF'
rootProject.name = "TankGame"
EOF
echo "✅ settings.gradle.kts creado"
echo ""

# Crear archivo .gitignore
echo "📝 Creando .gitignore..."
cat > .gitignore << 'EOF'
# Gradle
.gradle/
build/
*.gradle

# IDE
.idea/
.vscode/
*.iml
*.iws
*.ipr
.DS_Store

# Kotlin
out/
*.class
*.jar
*.war

# General
*.log
*.swp
*~
.env
EOF
echo "✅ .gitignore creado"
echo ""

# Crear archivo main.kt de inicio
echo "📝 Creando archivo principal..."
cat > src/main/kotlin/game/GameApplication.kt << 'EOF'
package game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import game.screens.GameScreen

class GameApplication : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var camera: OrthographicCamera
    private lateinit var gameScreen: GameScreen

    override fun create() {
        // Inicializar cámara y batch
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800f, 600f)
        batch = SpriteBatch()
        
        // Inicializar pantalla del juego
        gameScreen = GameScreen(camera, batch)
        
        println("🎮 Juego iniciado correctamente")
    }

    override fun render() {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        // Actualizar y renderizar
        gameScreen.update(Gdx.graphics.deltaTime)
        gameScreen.render()
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width.toFloat(), height.toFloat())
    }

    override fun dispose() {
        batch.dispose()
        gameScreen.dispose()
    }
}
EOF
echo "✅ GameApplication.kt creado"
echo ""

# Crear GameScreen
echo "📝 Creando GameScreen..."
cat > src/main/kotlin/game/screens/GameScreen.kt << 'EOF'
package game.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class GameScreen(val camera: OrthographicCamera, val batch: SpriteBatch) {
    
    fun update(deltaTime: Float) {
        // Lógica de actualización
    }
    
    fun render() {
        batch.projectionMatrix = camera.combined
        batch.begin()
        
        // Dibujar elementos del juego aquí
        
        batch.end()
    }
    
    fun dispose() {
        // Limpiar recursos
    }
}
EOF
echo "✅ GameScreen.kt creado"
echo ""

# Crear archivo Tank.kt
echo "📝 Creando entidad Tank..."
cat > src/main/kotlin/game/entities/Tank.kt << 'EOF'
package game.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Tank(
    var x: Float,
    var y: Float,
    var width: Float = 32f,
    var height: Float = 32f,
    var isPlayer: Boolean = true
) {
    var speed = 200f
    var direction = Direction.UP
    var health = 100
    
    fun update(deltaTime: Float) {
        // Actualizar posición basada en dirección
    }
    
    fun render(batch: SpriteBatch) {
        // Dibujar tanque
    }
    
    fun takeDamage(damage: Int) {
        health -= damage
    }
    
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
EOF
echo "✅ Tank.kt creado"
echo ""

# Crear archivo README.md
echo "📝 Creando README.md..."
cat > README.md << 'EOF'
# Tank Game - Kotlin con LibGDX

Un juego de tanques 2D desarrollado en Kotlin usando LibGDX.

## Estructura del Proyecto

- `src/main/kotlin/game/` - Código fuente principal
  - `screens/` - Pantallas del juego (menú, juego, etc)
  - `entities/` - Entidades del juego (Tanques, balas, power-ups)
  - `utils/` - Utilidades y helpers
  - `maps/` - Gestión de mapas/laberintos
  - `managers/` - Gestores de colisiones, enemigos, etc

- `src/resources/assets/` - Recursos del juego
  - `images/` - Sprites y texturas
  - `sounds/` - Efectos de sonido
  - `fonts/` - Fuentes tipográficas

## Requisitos

- JDK 11 o superior
- Gradle
- Kotlin 1.9.20 o superior

## Instalación y Ejecución

```bash
gradle build
gradle run
```

## Desarrollo

Abre el proyecto en VS Code o IntelliJ IDEA y comienza a desarrollar.
EOF
echo "✅ README.md creado"
echo ""

echo "🎉 ¡Estructura del proyecto creada exitosamente!"
echo "📍 Ubicación: $PROJECT_DIR"
echo ""
echo "Próximos pasos:"
echo "1. cd $PROJECT_NAME"
echo "2. Configurar VS Code con las extensiones recomendadas"
echo "3. Ejecutar: gradle build"
echo "4. ¡Comenzar a codificar!"