package arte921.flock

import android.util.Log
import java.lang.Math.random
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

class boid {
    var x: Double
    var y: Double
    var angle: Double
    var speed: Double
    var acceleration: Double
    var viewRadius: Double
    var dspeed: Double
    var dangle: Double

    init {
        this.x = random() * 300.0
        this.y = random() * 300.0
        this.angle = random() * 2 * PI
        this.speed = random() * 10
        this.acceleration = 0.0
        this.viewRadius = 10.0
        this.dspeed = 0.0
        this.dangle = this.angle
    }

    fun getDistance(x: Double,y: Double): Double {
        return sqrt((x-this.x).pow(2) + (y-this.y).pow(2))
    }

    fun log(){
        Log.i("boid.log",this.x.toString() + "," +  this.x.toString() + ", Angle: " +  this.angle.toString() + ", Speed: " +  this.speed.toString())
    }

    fun reset(){
        this.dspeed = 0.0
        this.dangle = 0.0
    }
}