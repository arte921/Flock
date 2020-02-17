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

    var tx: Double = 0.0
    var ty: Double = 0.0
    var tangle: Double = 0.0
    var tspeed: Double = 0.0

    init {
        this.x = random() * maxX
        this.y = random() * maxY
        this.angle = random() * 2 * PI
        this.speed = 500*random()
        this.acceleration = 0.0
        this.viewRadius = 50.0
        this.dspeed = 0.0
        this.dangle = this.angle

        this.tx = 0.0
        this.ty = 0.0
        this.tangle = 0.0
        this.tspeed = 0.0
    }

    fun getDistance(x: Double,y: Double): Double {
        var d = listOf(
            sqrt((x-this.x).pow(2) + (y-this.y).pow(2)),
            sqrt((maxx-x-this.x).pow(2) + (y-this.y).pow(2)),
            sqrt((x-this.x).pow(2) + (maxy-y-this.y).pow(2)),
            sqrt((maxx-x-this.x).pow(2) + (maxy-y-this.y).pow(2))).min()
        return if(d !== null){d}else{sqrt((x-this.x).pow(2) + (y-this.y).pow(2))}
    }

    fun apply(){
        this.x = this.tx
        this.y = this.ty
        this.angle = this.tangle
        this.speed = this.tspeed
    }

    fun log(){
        Log.i("boid.log",this.x.toString() + "," +  this.x.toString() + ", Angle: " +  this.angle.toString() + ", Speed: " +  this.speed.toString())
    }

    fun reset(){
        this.dspeed = 0.0
        this.dangle = 0.0
    }
}