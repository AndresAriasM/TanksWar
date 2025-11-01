package game.utils

import com.badlogic.gdx.math.Rectangle

object CollisionSystem {
    
    /**
     * Detecta colisión AABB (Axis-Aligned Bounding Box)
     * entre dos rectángulos
     */
    fun checkAABBCollision(
        x1: Float, y1: Float, w1: Float, h1: Float,
        x2: Float, y2: Float, w2: Float, h2: Float
    ): Boolean {
        return x1 < x2 + w2 &&
               x1 + w1 > x2 &&
               y1 < y2 + h2 &&
               y1 + h1 > y2
    }
    
    /**
     * Detecta colisión circular (útil para balas)
     */
    fun checkCircleCollision(
        x1: Float, y1: Float, r1: Float,
        x2: Float, y2: Float, r2: Float
    ): Boolean {
        val dx = x1 - x2
        val dy = y1 - y2
        val distance = kotlin.math.sqrt(dx * dx + dy * dy)
        return distance < r1 + r2
    }
    
    /**
     * Detecta si un punto está dentro de un rectángulo
     */
    fun pointInRectangle(
        px: Float, py: Float,
        rx: Float, ry: Float, rw: Float, rh: Float
    ): Boolean {
        return px >= rx && px <= rx + rw &&
               py >= ry && py <= ry + rh
    }
    
    /**
     * Obtiene el rectángulo de colisión
     */
    fun getBounds(x: Float, y: Float, w: Float, h: Float): Rectangle {
        return Rectangle(x, y, w, h)
    }
}