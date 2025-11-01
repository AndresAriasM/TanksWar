package game.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class PlayerTank(x: Float = 100f, y: Float = 100f) : Tank(x, y) {
    var bulletsFired: Int = 0
    
    override fun update(deltaTime: Float) {
        handleInput()
        updateShootCooldown(deltaTime)
    }
    
    override fun render(batch: SpriteBatch, shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        drawTank(shapeRenderer, 0.2f, 0.8f, 0.2f) // Verde para el jugador
        shapeRenderer.end()
        
        // Dibujar barra de salud
        drawHealthBar(shapeRenderer)
    }
    
    private fun handleInput() {
        // Movimiento
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            angle = 90f
            moveForward(0.016f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            angle = 270f
            moveForward(0.016f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle = 180f
            moveForward(0.016f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle = 0f
            moveForward(0.016f)
        }
    }
    
    fun shoot(): Bullet? {
        if (canShoot()) {
            resetShootCooldown()
            bulletsFired++
            
            // Crear bala desde la posición del cañón
            val radians = Math.toRadians(angle.toDouble())
            val cannonLength = size * 0.8f
            val bulletX = x + (kotlin.math.cos(radians) * cannonLength).toFloat()
            val bulletY = y + (kotlin.math.sin(radians) * cannonLength).toFloat()
            
            return Bullet(
                x = bulletX,
                y = bulletY,
                angle = angle,
                damage = (5 * damageMultiplier).toInt(),
                isPlayerBullet = true
            )
        }
        return null
    }
    
    private fun drawHealthBar(shapeRenderer: ShapeRenderer) {
        val barWidth = size
        val barHeight = 4f
        val barX = x - barWidth / 2
        val barY = y + size / 2 + 10f
        
        // Fondo rojo
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color.set(1f, 0f, 0f, 1f)
        shapeRenderer.rect(barX, barY, barWidth, barHeight)
        
        // Salud verde
        shapeRenderer.color.set(0f, 1f, 0f, 1f)
        shapeRenderer.rect(barX, barY, barWidth * (health.toFloat() / maxHealth), barHeight)
        shapeRenderer.end()
    }
}