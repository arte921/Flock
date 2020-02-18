package arte921.flock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.lang.Math.*
import java.util.*
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin


class CanvasView(context: Context): View(context) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val skyColor = ResourcesCompat.getColor(resources,R.color.sky,null)
    private val birdColor = ResourcesCompat.getColor(resources,R.color.bird,null)
    private val sw = 12f
    private var go: Double = Calendar.getInstance().timeInMillis.toDouble()
    private var deltaT: Double = 1.0
    private var maxX: Double = 720.0
    private var maxY: Double = 1280.0
    private var nearbyBoids = mutableListOf<boid>()
    private var boids = MutableList(50) { boid(maxX, maxY) }
    private var angletotalx: Double = 0.0
    private var angletotaly: Double = 0.0
    private var currentdistance: Double = 0.0
    private var navxangle: Double = 0.0
    private var alignangle: Double = 0.0
    private var angles = mutableListOf<Double>()

    private val paint = Paint().apply {
        color = birdColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = sw
    }

    private var path = Path()

    override fun onSizeChanged(width:Int,height:Int,oldWidth:Int,oldHeight:Int){
        super.onSizeChanged(width,height,oldWidth,oldHeight)
        if(::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(skyColor)
    }

    fun avgangles(inputs: List<Double>): Double{
        angletotalx = 0.0
        angletotaly = 0.0
        inputs.forEach {
            angletotalx += cos(it)
            angletotaly += sin(it)
        }

        return((2 * PI + atan(angletotaly/angletotalx)) % (2 * PI))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)

        boids.forEach {currentBoid ->
            nearbyBoids.clear()

            boids.forEach {
                if(currentBoid.getRawDistance(it.x,it.y) < currentBoid.viewRadius &&  it !== currentBoid){
                    nearbyBoids.add(it)
                }
            }

            if(nearbyBoids.size > 0){
                angles.clear()
                nearbyBoids.forEach {
                    currentdistance = currentBoid.getRawDistance(it.x,it.y)
                    currentBoid.dspeed += it.speed

                    if(currentdistance > currentBoid.viewRadius/3){
                        currentBoid.navx += it.x
                        currentBoid.navy += it.y
                        currentBoid.attractionamount++
                    }else if(currentdistance < currentBoid.viewRadius/8){
                        currentBoid.navx += 2 * currentBoid.x - it.x
                        currentBoid.navy += 2 * currentBoid.y - it.y
                        currentBoid.repulsionamount++
                    }else{
                        currentBoid.danglex += cos(it.x)
                        currentBoid.dangley += sin(it.x)
                        currentBoid.alignmentamount++
                    }
                }



                currentBoid.navx = currentBoid.navx/(currentBoid.attractionamount+currentBoid.repulsionamount)
                currentBoid.navy = currentBoid.navy/(currentBoid.attractionamount+currentBoid.repulsionamount)

                navxangle = (2 * PI + atan2((currentBoid.navy-currentBoid.y),currentBoid.navx-currentBoid.x)) % (2 * PI)

                alignangle = (2 * PI + atan2(sin(currentBoid.dangley),cos(currentBoid.danglex))) % (2 * PI)

                currentBoid.tangle = atan2((sin(alignangle) * currentBoid.alignmentamount + sin(navxangle) * (currentBoid.repulsionamount + currentBoid.attractionamount)),((cos(alignangle) * currentBoid.alignmentamount + cos(navxangle) * (currentBoid.repulsionamount + currentBoid.attractionamount))))
                currentBoid.tspeed = currentBoid.dspeed / nearbyBoids.size
            }

            currentBoid.tx = currentBoid.x + deltaT / 1000 * currentBoid.speed * cos(currentBoid.angle)
            currentBoid.ty = currentBoid.y + deltaT / 1000 * currentBoid.speed * sin(currentBoid.angle)
/*
            if(currentBoid.x > maxX) currentBoid.tx = 0.0
            if(currentBoid.x < 0) currentBoid.tx = maxX
            if(currentBoid.y > maxY) currentBoid.ty = 0.0
            if(currentBoid.y < 0) currentBoid.ty = maxY
*/
            if(currentBoid.x > maxX - currentBoid.viewRadius) currentBoid.tangle = PI
            if(currentBoid.x < currentBoid.viewRadius) currentBoid.tangle = 0.0
            if(currentBoid.y > maxY) currentBoid.tangle = 1.5 * PI
            if(currentBoid.y < 0) currentBoid.tangle = 0.5 * PI

        }

        boids.forEach {
            it.apply()
            it.reset()
            it.log()
            canvas.drawPoint((it.x % maxX).toFloat(),(maxY - it.y % maxY).toFloat(),paint)
            canvas.drawPoint((10.0 + random()*10.0).toFloat(), (10.0 + random()*10.0).toFloat(),paint)
        }



        deltaT = Calendar.getInstance().timeInMillis - go
        go = Calendar.getInstance().timeInMillis.toDouble()
        invalidate()
    }
}
