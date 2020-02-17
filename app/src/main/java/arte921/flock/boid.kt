package arte921.flock

import kotlin.math.PI

class boid {
    var x: Float
    var y: Float
    var angle: Float
    var speed: Float
    var acceleration: Float

    init {
        this.x = 300.0F
        this.y = 300.0F
        this.angle = PI.toFloat()
        this.speed = 10.0F
        this.acceleration = 0.0F
    }

    
}