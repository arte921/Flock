package arte921.flock

import android.util.Log
import java.lang.Math.random
import kotlin.math.*

class boid(maxX: Double, maxY: Double) {
    var x: Double = random() * maxX
    var y: Double = random() * maxY
    var angle: Double = random() * 2 * PI
    var speed: Double = 100.0
    var viewRadius: Double = 200.0
    var dspeed: Double = 0.0
    var dangle: Double = 0.0
    var maxx: Double = maxX
    var maxy: Double = maxY
    var tx: Double = 0.0
    var ty: Double = 0.0
    var tangle: Double = 0.0
    var tspeed: Double = 0.0
    var navx: Double = 0.0
    var navy: Double = 0.0
    var dnavx: Double = 0.0
    var dnavy: Double = 0.0
    var tanglec: Double = 0.0
    var danglex: Double = 0.0
    var dangley: Double = 0.0
    var repulsionamount: Int = 0
    var alignmentamount: Int = 0
    var attractionamount: Int = 0


    fun getRawDistance(x: Double, y: Double): Double {
        return sqrt((x-this.x).pow(2) + (y-this.y).pow(2))
    }

    fun getDistance(x: Double, y: Double): Double {

        var d = listOf(
            sqrt((x-this.x).pow(2) + (y-this.y).pow(2)),
            sqrt((maxx-x-this.x).pow(2) + (y-this.y).pow(2)),
            sqrt((x-this.x).pow(2) + (maxy-y-this.y).pow(2)),
            sqrt((maxx-x-this.x).pow(2) + (maxy-y-this.y).pow(2))).min()
        return if(d !== null){d}else{viewRadius + 1}
    }


    fun log(){
        Log.i("boid.log",this.x.toString() + "," +  this.x.toString() + ", Angle: " +  this.angle.toString() + ", Speed: " +  this.speed.toString())
    }

    fun apply(){
        this.x = this.tx
        this.y = this.ty
        this.angle = this.tangle
        this.speed = this.tspeed
    }

    fun reset(){
        this.dspeed = 0.0
        this.dangle = 0.0
        this.navx = 0.0
        this.navy = 0.0
        this.dnavx = 0.0
        this.dnavy = 0.0
        this.tanglec = 0.0
        this.danglex = 0.0
        this.dangley = 0.0
        this.repulsionamount = 0
        this.alignmentamount = 0
        this.attractionamount = 0
    }
}