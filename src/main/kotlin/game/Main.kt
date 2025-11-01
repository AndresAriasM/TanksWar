package game

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(800, 600)
    config.setTitle("Tank Wars")
    config.setForegroundFPS(60)
    config.useVsync(true)
    
    // En Mac, GLFW debe ejecutarse en el thread principal
    Lwjgl3Application(GameApplication(), config)
}