package game.managers

import game.entities.Obstacle
import game.utils.Constants

class MapManager {
    private val obstacles = mutableListOf<Obstacle>()
    private var currentMapConfig: Array<IntArray>? = null
    
    fun loadMap(mapLayout: Array<IntArray>) {
        obstacles.clear()
        currentMapConfig = mapLayout
        
        for (row in mapLayout.indices) {
            for (col in mapLayout[row].indices) {
                if (mapLayout[row][col] != 0) {
                    val x = col * Constants.OBSTACLE_SIZE
                    val y = (mapLayout.size - 1 - row) * Constants.OBSTACLE_SIZE
                    
                    val isDestructible = mapLayout[row][col] == 2
                    obstacles.add(
                        Obstacle(
                            x = x.toFloat(),
                            y = y.toFloat(),
                            isDestructible = isDestructible,
                            health = if (isDestructible) 20 else Int.MAX_VALUE
                        )
                    )
                }
            }
        }
        
        println("🗺️ Mapa cargado. Obstáculos: ${obstacles.size}")
    }
    
    fun update(deltaTime: Float) {
        obstacles.forEach { it.update(deltaTime) }
        obstacles.removeAll { !it.isActive }
    }
    
    fun getObstacles(): MutableList<Obstacle> = obstacles
    
    fun checkCollision(x: Float, y: Float, size: Float): Boolean {
        return obstacles.any { obstacle ->
            x < obstacle.x + obstacle.width &&
            x + size > obstacle.x &&
            y < obstacle.y + obstacle.height &&
            y + size > obstacle.y
        }
    }
    
    fun damageObstacle(bulletX: Float, bulletY: Float, damage: Int) {
        obstacles.forEach { obstacle ->
            if (bulletX >= obstacle.x && bulletX <= obstacle.x + obstacle.width &&
                bulletY >= obstacle.y && bulletY <= obstacle.y + obstacle.height) {
                obstacle.takeDamage(damage)
            }
        }
    }
    
    fun generateRandomObstacles(count: Int) {
        val margin = 80f
        val maxX = Constants.SCREEN_WIDTH - margin
        val maxY = Constants.SCREEN_HEIGHT - margin
        
        repeat(count) {
            val x = kotlin.random.Random.nextFloat() * (maxX - margin) + margin
            val y = kotlin.random.Random.nextFloat() * (maxY - margin) + margin
            val isDestructible = kotlin.random.Random.nextFloat() > 0.4f
            
            obstacles.add(
                Obstacle(
                    x = x,
                    y = y,
                    isDestructible = isDestructible,
                    health = if (isDestructible) 20 else Int.MAX_VALUE
                )
            )
        }
        println("🗺️ ${count} obstáculos aleatorios generados")
    }
    
    fun dispose() {
        obstacles.clear()
    }
}