package game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.entities.*
import game.managers.*
import game.utils.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class GameScreen(camera: OrthographicCamera, batch: SpriteBatch) : BaseScreen(camera, batch) {
    override val screenType = ScreenType.GAME
    
    private lateinit var playerTank: PlayerTank
    private val allyTanks = mutableListOf<AllyTank>()
    private var allySpawned = false
    private lateinit var enemyManager: EnemyManager
    private lateinit var mapManager: MapManager
    private val bullets = mutableListOf<Bullet>()
    private val powerUps = mutableListOf<PowerUp>()
    private val damageIndicators = mutableListOf<DamageIndicator>()
    private val airplanes = mutableListOf<Airplane>()
    
    private var gameState: GameState = GameState.PLAYING
    private var currentLevel: Int = 1
    private var score: Int = 0
    private var lives: Int = Constants.MAX_LIVES
    private var timeElapsed: Float = 0f
    private var levelTimeLimit: Float? = null
    
    private var gameover = false
    private var levelComplete = false
    private var backToMenu = false
    
    private var lastLivesCount = Constants.MAX_LIVES
    private var livesLostMessage = ""
    private var livesLostTimer = 0f
    
    private var powerUpSpawnTimer = 0f
    private val powerUpSpawnInterval = 15f
    private var airplaneSpawnTimer = 0f
    private val airplaneSpawnInterval = 20f
    
    // Pause menu
    private var pausedMenuSelection = 0
    private val pausedMenuOptions = listOf("Continuar", "Menú Principal")
    
    data class DamageIndicator(
        var x: Float,
        var y: Float,
        var damage: Int,
        var time: Float = 0.5f
    )
    
    override fun show() {
        super.show()
        SoundManager.playGameMusic()
        backToMenu = false
        initializeGame(resetLives = true)
    }
    
    private fun initializeGame(resetLives: Boolean = false) {
        currentLevel = DataManager.getCurrentLevel()
        score = 0
        if (resetLives) lives = Constants.MAX_LIVES
        timeElapsed = 0f
        gameover = false
        levelComplete = false
        allySpawned = false
        powerUpSpawnTimer = 0f
        pausedMenuSelection = 0
        
        val levelConfig = LevelManager.levelConfigs[currentLevel]
        requireNotNull(levelConfig) { "Nivel $currentLevel no encontrado" }
        
        mapManager = MapManager()
        mapManager.loadMap(arrayOf())  // Mapa vacío
        
        // Generar obstáculos aleatorios (más en niveles altos)
        val obstacleCount = 8 + (currentLevel * 3)
        mapManager.generateRandomObstacles(obstacleCount)
        
        playerTank = PlayerTank(
            x = Constants.SCREEN_WIDTH / 2,
            y = Constants.SCREEN_HEIGHT / 2
        )
        
        enemyManager = EnemyManager(maxEnemies = 12)
        // Aumentar frecuencia de disparo según el nivel
        enemyManager.setShootFrequency(0.02f + (currentLevel * 0.01f))
        enemyManager.initialize(levelConfig.enemyCount)
        
        bullets.clear()
        powerUps.clear()
        airplanes.clear()
        damageIndicators.clear()
        allyTanks.clear()
        powerUpSpawnTimer = 0f
        airplaneSpawnTimer = 0f
        val powerupCount = 3 + (currentLevel / 2)
        val allPowerupTypes = listOf(
            PowerUpType.RAPID_FIRE,
            PowerUpType.SHIELD,
            PowerUpType.SPEED,
            PowerUpType.HEALTH,
            PowerUpType.ARMOR,
            PowerUpType.BLAST,
            PowerUpType.ALLY_SPAWN
        )
        
        repeat(powerupCount) {
            val x = kotlin.random.Random.nextFloat() * (Constants.SCREEN_WIDTH - 100f) + 50f
            val y = kotlin.random.Random.nextFloat() * (Constants.SCREEN_HEIGHT - 100f) + 50f
            val type = allPowerupTypes[kotlin.random.Random.nextInt(allPowerupTypes.size)]
            powerUps.add(PowerUp(x, y, type))
        }
        
        levelTimeLimit = levelConfig.timeLimit
        gameState = GameState.PLAYING
        
        println("🎮 Nivel $currentLevel inicializado")
        println("📊 Enemigos a derrotar: ${levelConfig.enemyCount}")
        println("🗺️ Obstáculos generados: $obstacleCount")
        println("⚡ Power-ups generados: $powerupCount")
        SoundManager.playSound(SoundManager.SoundType.LEVEL_COMPLETE)
    }
    
    override fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (gameState == GameState.PAUSED) {
                gameState = GameState.PLAYING
                pausedMenuSelection = 0
            } else {
                gameState = GameState.PAUSED
                pausedMenuSelection = 0
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (gameState == GameState.PAUSED) {
                gameState = GameState.PLAYING
                pausedMenuSelection = 0
            } else {
                gameState = GameState.PAUSED
                pausedMenuSelection = 0
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            val bullet = playerTank.shoot()
            bullet?.let {
                bullets.add(it)
                SoundManager.playSound(SoundManager.SoundType.SHOOT)
            }
        }
        
        // Controles del menú de pausa
        if (gameState == GameState.PAUSED) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                pausedMenuSelection = (pausedMenuSelection - 1).coerceAtLeast(0)
                SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                pausedMenuSelection = (pausedMenuSelection + 1).coerceAtMost(pausedMenuOptions.size - 1)
                SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                when (pausedMenuSelection) {
                    0 -> {
                        gameState = GameState.PLAYING
                        pausedMenuSelection = 0
                        SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
                    }
                    1 -> {
                        backToMenu = true
                        SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
                    }
                }
            }
        }
    }
    
    override fun update(deltaTime: Float) {
        handleInput()
        
        if (gameState != GameState.PLAYING) return
        
        timeElapsed += deltaTime
        
        // Spawnar aviones aleatorios
        airplaneSpawnTimer += deltaTime
        if (airplaneSpawnTimer >= airplaneSpawnInterval) {
            val type = if (Random.nextFloat() > 0.5f) AirplaneType.BOMBER else AirplaneType.OBSTACLE_DROP
            airplanes.add(Airplane(x = -50f, y = 100f + Random.nextFloat() * 300f, type = type))
            airplaneSpawnTimer = 0f
            println("✈️ Avión ${type.name} ha aparecido")
        }
        
        // Spawnar aliado aleatorio en el nivel
        if (!allySpawned && Random.nextFloat() > 0.8f) {
            allyTanks.add(AllyTank(
                x = Random.nextFloat() * (Constants.SCREEN_WIDTH - 100f) + 50f,
                y = Random.nextFloat() * (Constants.SCREEN_HEIGHT - 100f) + 50f
            ))
            allySpawned = true
            println("🛡️ ¡Aliado ha llegado!")
        }
        powerUpSpawnTimer += deltaTime
        if (powerUpSpawnTimer >= powerUpSpawnInterval && Random.nextFloat() > 0.5f) {
            val x = Random.nextFloat() * (Constants.SCREEN_WIDTH - 100f) + 50f
            val y = Random.nextFloat() * (Constants.SCREEN_HEIGHT - 100f) + 50f
            val allPowerupTypes = listOf(
                PowerUpType.RAPID_FIRE, PowerUpType.SHIELD, PowerUpType.SPEED,
                PowerUpType.HEALTH, PowerUpType.ARMOR, PowerUpType.BLAST, PowerUpType.ALLY_SPAWN
            )
            val type = allPowerupTypes[Random.nextInt(allPowerupTypes.size)]
            powerUps.add(PowerUp(x, y, type))
            powerUpSpawnTimer = 0f
        }
        
        playerTank.update(deltaTime)
        enemyManager.update(deltaTime)
        
        // Actualizar aliados
        for (ally in allyTanks) {
            if (ally.isAlive) {
                ally.update(deltaTime)
                ally.shoot()?.let { bullets.add(it) }
            }
        }
        allyTanks.removeAll { !it.isAlive }
        
        // Actualizar y procesar aviones
        airplanes.forEach { plane ->
            plane.update(deltaTime)
            
            if (plane.shouldShoot()) {
                plane.resetShootTimer()
                when (plane.type) {
                    AirplaneType.BOMBER -> {
                        val (dropX, dropY) = plane.getDropPosition()
                        bullets.add(Bullet(dropX, dropY, 270f, 20, false))
                    }
                    AirplaneType.OBSTACLE_DROP -> {
                        val (dropX, dropY) = plane.getDropPosition()
                        mapManager.getObstacles().let { obstacles ->
                            if (obstacles.size < 30) {
                                obstacles.add(Obstacle(
                                    x = dropX,
                                    y = dropY,
                                    isDestructible = true,
                                    health = 20
                                ))
                            }
                        }
                    }
                }
            }
        }
        airplanes.removeAll { !it.isActive }
        
        for (enemy in enemyManager.getEnemies()) {
            if (enemy.isAlive && Random.nextFloat() < 0.025f + (currentLevel * 0.005f)) {
                enemy.shoot()?.let { bullets.add(it) }
                SoundManager.playSound(SoundManager.SoundType.UI_CLICK)
            }
        }
        
        bullets.forEach { it.update(deltaTime) }
        bullets.removeAll { !it.isActive }
        
        powerUps.forEach { it.update(deltaTime) }
        powerUps.removeAll { !it.isActive }
        
        damageIndicators.forEach { it.time -= deltaTime }
        damageIndicators.removeAll { it.time <= 0 }
        
        if (livesLostTimer > 0) {
            livesLostTimer -= deltaTime
        }
        
        checkBulletTankCollisions()
        checkBulletObstacleCollisions()
        checkPowerUpCollisions()
        checkPlayerObstacleCollisions()
        checkTankCollisions()
        
        levelTimeLimit?.let {
            if (timeElapsed > it) {
                lives--
                if (lives <= 0) {
                    gameState = GameState.GAME_OVER
                    gameover = true
                } else {
                    initializeGame()
                }
            }
        }
        
        if (enemyManager.isLevelComplete()) {
            levelComplete = true
            gameState = GameState.LEVEL_COMPLETE
            SoundManager.playSound(SoundManager.SoundType.LEVEL_COMPLETE)
            
            if (currentLevel < Constants.TOTAL_LEVELS) {
                DataManager.setLevelUnlocked(currentLevel + 1)
            }
            
            if (currentLevel == Constants.TOTAL_LEVELS) {
                DataManager.unlockSecretLevel()
            }
        }
        
        if (!playerTank.isAlive) {
            lives--
            if (lives > 0) {
                livesLostMessage = "¡PERDISTE UNA VIDA! Vidas restantes: $lives"
                livesLostTimer = 2f
                println("💀 ¡Perdiste una vida! Te quedan $lives")
            }
            if (lives <= 0) {
                gameState = GameState.GAME_OVER
                gameover = true
            } else {
                initializeGame()
            }
        }
    }
    
    private fun checkBulletTankCollisions() {
        bullets.removeAll { bullet ->
            var hit = false
            
            if (bullet.isPlayerBullet) {
                for (enemy in enemyManager.getEnemies()) {
                    if (enemy.isAlive &&
                        CollisionSystem.checkCircleCollision(
                            bullet.x, bullet.y, Constants.BULLET_SIZE / 2,
                            enemy.x, enemy.y, Constants.TANK_SIZE / 2
                        )) {
                        enemy.takeDamage(bullet.damage)
                        damageIndicators.add(DamageIndicator(enemy.x, enemy.y, bullet.damage))
                        if (!enemy.isAlive) {
                            score += enemy.getScoreValue()
                            DataManager.addPlayerStat("enemies_killed", 1)
                            SoundManager.playSound(SoundManager.SoundType.EXPLOSION)
                        }
                        hit = true
                    }
                }
            } else {
                if (playerTank.isAlive &&
                    CollisionSystem.checkCircleCollision(
                        bullet.x, bullet.y, Constants.BULLET_SIZE / 2,
                        playerTank.x, playerTank.y, Constants.TANK_SIZE / 2
                    )) {
                    playerTank.takeDamage(bullet.damage)
                    damageIndicators.add(DamageIndicator(playerTank.x, playerTank.y, bullet.damage))
                    SoundManager.playSound(SoundManager.SoundType.DAMAGE)
                    hit = true
                }
                
                for (ally in allyTanks) {
                    if (ally.isAlive &&
                        CollisionSystem.checkCircleCollision(
                            bullet.x, bullet.y, Constants.BULLET_SIZE / 2,
                            ally.x, ally.y, Constants.TANK_SIZE / 2
                        )) {
                        ally.takeDamage(bullet.damage)
                        damageIndicators.add(DamageIndicator(ally.x, ally.y, bullet.damage))
                        hit = true
                    }
                }
            }
            
            hit
        }
    }
    
    private fun checkBulletObstacleCollisions() {
        bullets.removeAll { bullet ->
            if (mapManager.getObstacles().any { obstacle ->
                    obstacle.isActive &&
                    bullet.x >= obstacle.x && bullet.x <= obstacle.x + obstacle.width &&
                    bullet.y >= obstacle.y && bullet.y <= obstacle.y + obstacle.height
                }) {
                mapManager.damageObstacle(bullet.x, bullet.y, bullet.damage)
                true
            } else {
                false
            }
        }
    }
    
    private fun checkPowerUpCollisions() {
        powerUps.removeAll { powerUp ->
            if (!powerUp.isActive) return@removeAll false
            
            if (CollisionSystem.checkAABBCollision(
                    playerTank.x - Constants.TANK_SIZE / 2,
                    playerTank.y - Constants.TANK_SIZE / 2,
                    Constants.TANK_SIZE,
                    Constants.TANK_SIZE,
                    powerUp.x - powerUp.size / 2,
                    powerUp.y - powerUp.size / 2,
                    powerUp.size,
                    powerUp.size
                )) {
                
                applyPowerUp(powerUp.type)
                SoundManager.playSound(SoundManager.SoundType.POWERUP)
                DataManager.addPlayerStat("powerups_collected", 1)
                println("⚡ Power-up recogido: ${powerUp.getDescription()}")
                true
            } else {
                false
            }
        }
    }
    
    private fun checkPlayerObstacleCollisions() {
        if (mapManager.checkCollision(playerTank.x, playerTank.y, Constants.TANK_SIZE)) {
            playerTank.x -= playerTank.speed * Gdx.graphics.deltaTime
            playerTank.y -= playerTank.speed * Gdx.graphics.deltaTime
        }
    }
    
    private fun checkTankCollisions() {
        for (enemy in enemyManager.getEnemies()) {
            if (enemy.isAlive && playerTank.isAlive) {
                val dx = playerTank.x - enemy.x
                val dy = playerTank.y - enemy.y
                val distance = kotlin.math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                
                if (distance < Constants.TANK_SIZE) {
                    playerTank.takeDamage(20)
                    enemy.takeDamage(20)
                    damageIndicators.add(DamageIndicator(playerTank.x, playerTank.y, 20))
                    SoundManager.playSound(SoundManager.SoundType.DAMAGE)  
                }
            }
        }
    }
    
    private fun applyPowerUp(type: PowerUpType) {
        when (type) {
            PowerUpType.RAPID_FIRE -> playerTank.rapidFireMultiplier = 2f
            PowerUpType.SHIELD -> playerTank.activateShield()
            PowerUpType.SPEED -> playerTank.speedMultiplier = 1.5f
            PowerUpType.HEALTH -> playerTank.heal(30)
            PowerUpType.ARMOR -> playerTank.damageMultiplier = 1.5f
            PowerUpType.BLAST -> {
                val angles = listOf(0f, 72f, 144f, 216f, 288f)
                angles.forEach { angle ->
                    val radians = Math.toRadians(angle.toDouble())
                    val cannonLength = Constants.TANK_SIZE * 0.8f
                    val bulletX = playerTank.x + (cos(radians) * cannonLength).toFloat()
                    val bulletY = playerTank.y + (sin(radians) * cannonLength).toFloat()
                    bullets.add(Bullet(bulletX, bulletY, angle, Constants.BULLET_DAMAGE, true))
                }
                SoundManager.playSound(SoundManager.SoundType.SHOOT)
            }
            PowerUpType.ALLY_SPAWN -> {
                allyTanks.add(AllyTank(
                    x = playerTank.x + 50f,
                    y = playerTank.y + 50f
                ))
                allyTanks.last().health = 30
                println("🛡️ ¡Nuevo aliado ha sido invocado!")
            }
        }
    }
    
    override fun render() {
        drawBackground(
            Constants.COLOR_BACKGROUND_R,
            Constants.COLOR_BACKGROUND_G,
            Constants.COLOR_BACKGROUND_B
        )
        
        mapManager.getObstacles().forEach { obstacle ->
            obstacle.render(batch, shapeRenderer)
        }
        
        powerUps.forEach { powerUp ->
            powerUp.render(batch, shapeRenderer)
        }
        
        if (playerTank.isAlive) {
            playerTank.render(batch, shapeRenderer)
        }
        
        for (ally in allyTanks) {
            if (ally.isAlive) {
                ally.render(batch, shapeRenderer)
            }
        }
        
        for (enemy in enemyManager.getEnemies()) {
            if (enemy.isAlive) {
                enemy.render(batch, shapeRenderer)
            }
        }
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        if (playerTank.isAlive) {
            val barWidth = 40f
            val barHeight = 4f
            val barX = playerTank.x - barWidth / 2
            val barY = playerTank.y + Constants.TANK_SIZE / 2 + 8f
            
            shapeRenderer.color.set(1f, 0f, 0f, 1f)
            shapeRenderer.rect(barX, barY, barWidth, barHeight)
            shapeRenderer.color.set(0f, 1f, 0f, 1f)
            shapeRenderer.rect(barX, barY, (barWidth * playerTank.health) / playerTank.maxHealth, barHeight)
        }
        
        for (ally in allyTanks) {
            if (ally.isAlive) {
                val barWidth = 40f
                val barHeight = 4f
                val barX = ally.x - barWidth / 2
                val barY = ally.y + Constants.TANK_SIZE / 2 + 8f
                
                shapeRenderer.color.set(1f, 0f, 0f, 1f)
                shapeRenderer.rect(barX, barY, barWidth, barHeight)
                shapeRenderer.color.set(0f, 1f, 0f, 1f)
                shapeRenderer.rect(barX, barY, (barWidth * ally.health) / ally.maxHealth, barHeight)
            }
        }
        
        for (enemy in enemyManager.getEnemies()) {
            if (enemy.isAlive) {
                val barWidth = 40f
                val barHeight = 4f
                val barX = enemy.x - barWidth / 2
                val barY = enemy.y + Constants.TANK_SIZE / 2 + 8f
                
                shapeRenderer.color.set(1f, 0f, 0f, 1f)
                shapeRenderer.rect(barX, barY, barWidth, barHeight)
                shapeRenderer.color.set(0f, 1f, 0f, 1f)
                shapeRenderer.rect(barX, barY, (barWidth * enemy.health) / enemy.maxHealth, barHeight)
            }
        }
        
        shapeRenderer.end()
        
        bullets.forEach { bullet ->
            bullet.render(batch, shapeRenderer)
        }
        
        airplanes.forEach { plane ->
            plane.render(shapeRenderer)
        }
        
        damageIndicators.forEach { indicator ->
            drawDamageIndicator(indicator)
        }
        
        drawHUD()
        
        when (gameState) {
            GameState.PAUSED -> drawPausedMenu()
            GameState.LEVEL_COMPLETE -> drawLevelComplete()
            GameState.GAME_OVER -> drawGameOver()
            else -> {}
        }
    }
    
    private fun drawHUD() {
        batch.begin()
        font.data.setScale(1f)
        font.color.set(0.2f, 0.8f, 0.2f, 1f)
        
        font.draw(batch, "Puntuación: $score", 20f, Constants.SCREEN_HEIGHT - 20f)
        font.draw(batch, "Vidas: $lives", 20f, Constants.SCREEN_HEIGHT - 50f)
        font.draw(batch, "Nivel: $currentLevel", Constants.SCREEN_WIDTH - 150f, Constants.SCREEN_HEIGHT - 20f)
        
        val minutes = (timeElapsed / 60).toInt()
        val seconds = (timeElapsed % 60).toInt()
        font.draw(batch, "Tiempo: ${minutes}:${String.format("%02d", seconds)}", Constants.SCREEN_WIDTH - 150f, Constants.SCREEN_HEIGHT - 50f)
        
        font.draw(batch, "Enemigos: ${enemyManager.getAliveEnemiesCount()}", Constants.SCREEN_WIDTH / 2 - 100f, Constants.SCREEN_HEIGHT - 20f)
        
        if (livesLostTimer > 0) {
            font.color.set(1f, 0f, 0f, livesLostTimer / 2f)
            font.data.setScale(2f)
            val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, livesLostMessage)
            font.draw(batch, livesLostMessage, 
                      Constants.SCREEN_WIDTH / 2 - layout.width / 2, 
                      Constants.SCREEN_HEIGHT / 2 + 100f)
            font.data.setScale(1f)
            font.color.set(0.2f, 0.8f, 0.2f, 1f)
        }
        
        batch.end()
    }
    
    private fun drawPausedMenu() {
        // Fondo oscuro semi-transparente
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color.set(0f, 0f, 0f, 0.7f)
        shapeRenderer.rect(0f, 0f, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT)
        shapeRenderer.end()
        
        // Título
        drawCenteredText(
            "PAUSADO",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2 + 80f,
            3f
        )
        
        // Opciones del menú
        val optionStartY = Constants.SCREEN_HEIGHT / 2
        val optionSpacing = 60f
        
        pausedMenuOptions.forEachIndexed { index, option ->
            val isSelected = index == pausedMenuSelection
            val y = optionStartY - (index * optionSpacing)
            
            // Fondo de opción seleccionada
            if (isSelected) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                shapeRenderer.color.set(0.2f, 0.8f, 0.2f, 1f)
                shapeRenderer.rect(
                    Constants.SCREEN_WIDTH / 2 - 120f,
                    y - 25f,
                    240f,
                    50f
                )
                shapeRenderer.end()
            }
            
            // Texto de opción
            val prefix = if (isSelected) "► " else "  "
            batch.begin()
            font.data.setScale(1.5f)
            
            if (isSelected) {
                font.color.set(0f, 0f, 0f, 1f) // Negro para texto sobre fondo verde
            } else {
                font.color.set(1f, 1f, 1f, 1f) // Blanco
            }
            
            val text = "$prefix$option"
            val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, text)
            font.draw(batch, text, Constants.SCREEN_WIDTH / 2 - layout.width / 2, y + layout.height / 2)
            batch.end()
        }
        
        // Controles
        drawCenteredText(
            "Use ARRIBA/ABAJO para navegar, ENTER para seleccionar, ESC para continuar",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2 - 120f,
            0.9f
        )
    }
    
    private fun drawLevelComplete() {
        drawCenteredText(
            "¡NIVEL COMPLETADO!",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2 + 50f,
            2.5f
        )
        drawCenteredText(
            "Puntuación: $score",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2,
            1.5f
        )
        drawCenteredText(
            "Presiona ENTER para continuar",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2 - 50f,
            1f
        )
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (currentLevel < Constants.TOTAL_LEVELS) {
                currentLevel++
                DataManager.setCurrentLevel(currentLevel)
                initializeGame()
            } else {
                backToMenu = true
            }
        }
    }
    
    private fun drawGameOver() {
        drawCenteredText(
            "GAME OVER",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2 + 50f,
            3f
        )
        drawCenteredText(
            "Puntuación Final: $score",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2,
            1.5f
        )
        drawCenteredText(
            "Presiona ESC para volver al menú",
            Constants.SCREEN_WIDTH / 2,
            Constants.SCREEN_HEIGHT / 2 - 50f,
            1f
        )
        
        DataManager.setTotalScore(score)
        DataManager.setHighScore(score)
    }
    
    private fun drawDamageIndicator(indicator: DamageIndicator) {
        val alpha = indicator.time / 0.5f
        font.data.setScale(1.5f)
        batch.begin()
        font.color.set(1f, 0f, 0f, alpha)
        
        val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(font, "-${indicator.damage}")
        font.draw(batch, "-${indicator.damage}", 
                  indicator.x - layout.width / 2, 
                  indicator.y + (0.5f - indicator.time) * 30f)
        
        batch.end()
    }
    
    fun isBackToMenu(): Boolean = backToMenu
    
    override fun dispose() {
        super.dispose()
        if (::mapManager.isInitialized) mapManager.dispose()
        if (::enemyManager.isInitialized) enemyManager.dispose()
        bullets.clear()
        powerUps.clear()
        damageIndicators.clear()
        airplanes.clear()
        allyTanks.clear()
    }
}
