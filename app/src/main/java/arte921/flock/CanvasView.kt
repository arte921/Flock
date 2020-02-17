package arte921.flock

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.lang.Math.PI
import java.lang.Math.random
import java.util.*
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
    private var boids = MutableList(20) { boid(maxX, maxY) }


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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)

        boids.forEach {currentBoid ->
            nearbyBoids.clear()

            boids.forEach {
                if(currentBoid.getDistance(it.x,it.y) < currentBoid.viewRadius && it !== currentBoid){
                    nearbyBoids.add(it)
                }
            }

            if(nearbyBoids.size > 0){
                nearbyBoids.forEach {
                    currentBoid.dspeed += it.speed
                    /*
                    adjustedAngle = it.angle - currentBoid.angle
                    if(adjustedAngle<0) adjustedAngle += 2 * PI
*/
                    currentBoid.dangle += it.angle
                }
                currentBoid.tspeed = currentBoid.dspeed / nearbyBoids.size
                currentBoid.tangle = currentBoid.dangle / nearbyBoids.size % PI
/*
                if(currentBoid.dangle < 0){
                    currentBoid.angle = 2 * PI - currentBoid.dangle
                }else{
                    currentBoid.angle = currentBoid.dangle
                }*/
            }

            currentBoid.x = currentBoid.x + deltaT / 1000 * currentBoid.speed * cos(currentBoid.angle)
            currentBoid.y = currentBoid.y + deltaT / 1000 * currentBoid.speed * sin(currentBoid.angle)

            if(currentBoid.x > maxX) currentBoid.x = 0.0
            if(currentBoid.x < 0) currentBoid.x = maxX
            if(currentBoid.y > maxY) currentBoid.y = 0.0
            if(currentBoid.y < 0) currentBoid.y = maxY


            currentBoid.reset()

            currentBoid.log()

            canvas.drawPoint(currentBoid.x.toFloat(),currentBoid.y.toFloat(),paint)
            canvas.drawPoint(10.0F, 10F,paint)

        }



        deltaT = Calendar.getInstance().timeInMillis - go
        go = Calendar.getInstance().timeInMillis.toDouble()
        invalidate()
    }
}
