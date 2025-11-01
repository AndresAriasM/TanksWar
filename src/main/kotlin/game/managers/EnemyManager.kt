package game.managers

import game.entities.Bullet
import game.entities.EnemyTank
import game.utils.EnemyType
import kotlin.random.Random

class EnemyManager(var maxEnemies: Int = 5) {
    private val enemies = mutableListOf<EnemyTank>()
    private var spawnTimer: Float = 0f
    private val spawnInterval: Float = 3f
    private var totalEnemiesSpawned: Int = 0
    private var enemiesToSpawn: Int = 0
    private var shootFrequency: Float = 0.02f // Probabilidad de disparo por frame
    
    fun setShootFrequency(frequency: Float) {
        shootFrequency = frequency
    }
    
    fun initialize(enemyCount: Int) {
        enemiesToSpawn = enemyCount
        totalEnemiesSpawned = 0
        enemies.clear()
        spawnTimer = 0f
        println("👾 EnemyManager inicializado. Enemigos a spawnear: $enemyCount")
    }
    
    fun update(deltaTime: Float) {
        spawnTimer += deltaTime
        
        // Spawnear enemigos gradualmente
        if (spawnTimer >= spawnInterval && totalEnemiesSpawned < enemiesToSpawn) {
            spawnEnemy()
            spawnTimer = 0f
        }
        
        // Actualizar enemigos vivos
        enemies.forEach { 
            if (it.isAlive) {
                it.update(deltaTime)
            }
        }
        
        // Remover enemigos muertos
        enemies.removeAll { !it.isAlive }
    }
    
    private fun spawnEnemy() {
        if (enemies.size >= maxEnemies || totalEnemiesSpawned >= enemiesToSpawn) return
        
        // Posiciones de spawn en las esquinas
        val spawnPositions = listOf(
            Pair(50f, 550f),
            Pair(750f, 550f),
            Pair(50f, 50f),
            Pair(750f, 50f)
        )
        
        val spawnPos = spawnPositions[Random.nextInt(spawnPositions.size)]
        
        // Seleccionar tipo de enemigo según progresión
        val enemyType = selectEnemyType(totalEnemiesSpawned)
        
        val enemy = EnemyTank(spawnPos.first, spawnPos.second, enemyType)
        enemies.add(enemy)
        totalEnemiesSpawned++
        
        println("👾 Enemigo spawneado: $enemyType (Total: $totalEnemiesSpawned)")
    }
    
    private fun selectEnemyType(spawnedCount: Int): EnemyType {
        return when {
            spawnedCount < 2 -> EnemyType.NORMAL
            spawnedCount < 4 -> EnemyType.FAST
            spawnedCount < 6 -> EnemyType.TANK
            else -> EnemyType.SMART
        }
    }
    
    fun getEnemies(): List<EnemyTank> = enemies.toList()
    
    fun getAliveEnemiesCount(): Int = enemies.count { it.isAlive }
    
    fun removeEnemy(enemy: EnemyTank) {
        enemies.remove(enemy)
        println("💥 Enemigo eliminado. Quedan: ${enemies.size}")
    }
    
    fun getAllBullets(): List<Bullet> {
        val bullets = mutableListOf<Bullet>()
        enemies.forEach { enemy ->
            enemy.shoot()?.let { bullets.add(it) }
        }
        return bullets
    }
    
    fun isLevelComplete(): Boolean {
        return totalEnemiesSpawned >= enemiesToSpawn && enemies.isEmpty()
    }
    
    fun dispose() {
        enemies.clear()
    }
}