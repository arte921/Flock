package arte921.flock

import kotlin.math.PI

class boid {
    var x: Float
    var y: Float
    var angle: Float
    var speed: Float
    var acceleration: Float
    var viewRadius: Float

    var dspeed: Float
    var dangle: Float


    init {
        this.x = 300.0F
        this.y = 300.0F
        this.angle = PI.toFloat()
        this.speed = 10.0F
        this.acceleration = 0.0F
        this.viewRadius = 10.0F
        this.dspeed = this.speed
        this.dangle = this.angle
    }

    fun getDistance(x,y){
        return
    }
}