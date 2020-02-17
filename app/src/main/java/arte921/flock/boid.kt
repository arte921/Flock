package arte921.flock

import android.util.Log
import java.lang.Math.random
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

class boid(maxX: Double, maxY: Double) {
    var x: Double
    var y: Double
    var angle: Double
    var speed: Double
    var acceleration: Double
    var viewRadius: Double
    var dspeed: Double
    var dangle: Double
    var maxx: Double = maxX
    var maxy: Double = maxY

    init {
        this.x = random() * maxX
        this.y = random() * maxY
        this.angle = random() * 2 * PI
        this.speed = 100.0
        this.acceleration = 0.0
        this.viewRadius = 10000.0
        this.dspeed = 0.0
        this.dangle = this.angle
    }

    fun getDistance(x: Double,y: Double): Double {
        var d = listOf(
            sqrt((x-this.x).pow(2) + (y-this.y).pow(2)),
            sqrt((maxx-x-this.x).pow(2) + (y-this.y).pow(2)),
            sqrt((x-this.x).pow(2) + (maxy-y-this.y).pow(2)),
            sqrt((maxx-x-this.x).pow(2) + (maxy-y-this.y).pow(2))).min()
        return if(d !== null){d}else{sqrt((x-this.x).pow(2) + (y-this.y).pow(2))}
    }

    fun log(){
        Log.i("boid.log",this.x.toString() + "," +  this.x.toString() + ", Angle: " +  this.angle.toString() + ", Speed: " +  this.speed.toString())
    }

    fun reset(){
        this.dspeed = 0.0
        this.dangle = 0.0
    }
}