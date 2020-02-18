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

        return((2* PI + atan(angletotaly/angletotalx)) % (2 * PI))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)

        boids.forEach {currentBoid ->
            nearbyBoids.clear()

            boids.forEach {
                if(currentBoid.getRawDistance(it.x,it.y) < currentBoid.viewRadius && it !== currentBoid){
                    nearbyBoids.add(it)
                }
            }

            if(nearbyBoids.size > 0){
                nearbyBoids.forEach {
                    currentdistance = currentBoid.getDistance(it.x,it.y)
                    currentBoid.dspeed += it.speed

                    if(currentdistance > 100){
                        currentBoid.navx += it.x
                        currentBoid.navy += it.y
                        currentBoid.attractionamount++
                    }else if(currentdistance < 30){
                        currentBoid.navx += 2 * currentBoid.x - it.x
                        currentBoid.navy += 2 * currentBoid.y - it.y
                        currentBoid.repulsionamount++
                    }else{
                        currentBoid.danglex += cos(it.angle)
                        currentBoid.dangley += sin(it.angle)
                        currentBoid.alignmentamount++
                    }

                }

                navxangle = atan((currentBoid.navy/(currentBoid.attractionamount+currentBoid.repulsionamount)-currentBoid.y) / (currentBoid.navx/(currentBoid.attractionamount+currentBoid.repulsionamount)-currentBoid.x))
                alignangle = atan(currentBoid.danglex/currentBoid.dangley)


                currentBoid.tangle =  atan(((sin(navxangle) + sin(alignangle)) * 2/3 )/((cos(navxangle) + cos(alignangle)) * 1/3 ))

                currentBoid.tspeed = currentBoid.dspeed / nearbyBoids.size
                currentBoid.initdeltas()
            }

            currentBoid.tx = currentBoid.x + deltaT / 1000 * currentBoid.speed * cos(currentBoid.angle)
            currentBoid.ty = currentBoid.y + deltaT / 1000 * currentBoid.speed * sin(currentBoid.angle)

            if(currentBoid.x > maxX) currentBoid.tx = 0.0
            if(currentBoid.x < 0) currentBoid.tx = maxX
            if(currentBoid.y > maxY) currentBoid.ty = 0.0
            if(currentBoid.y < 0) currentBoid.ty = maxY


            currentBoid.reset()
            currentBoid.apply()
            currentBoid.log()

            canvas.drawPoint((currentBoid.x % maxX).toFloat(),(maxY - currentBoid.y % maxY).toFloat(),paint)
            canvas.drawPoint((10.0 + random()*10.0).toFloat(), 10F,paint)

        }



        deltaT = Calendar.getInstance().timeInMillis - go
        go = Calendar.getInstance().timeInMillis.toDouble()
        invalidate()
    }
}
