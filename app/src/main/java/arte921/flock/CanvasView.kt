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


class CanvasView(context: Context): View(context) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val skyColor = ResourcesCompat.getColor(resources,R.color.sky,null)
    private val birdColor = ResourcesCompat.getColor(resources,R.color.bird,null)
    private val sw = 12f
    private var go: Long = 0
    private var deltaT: Long = 0
    private var boids = MutableList(20) { boid() }
    private lateinit var currentBoid: boid
    private var nearbyBoids = mutableListOf<boid>()
    private var adjustedAngle: Double = 0.0

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

        boids.forEach {
            currentBoid = it
            nearbyBoids.clear()

            boids.forEach {
                if(currentBoid.getDistance(it.x,it.y) < currentBoid.viewRadius && it !== currentBoid){
                    nearbyBoids.add(it)
                }
            }

            if(nearbyBoids.size > 0){
                nearbyBoids.forEach {
                    currentBoid.dspeed += it.speed
                    adjustedAngle = it.angle - currentBoid.angle
                    if(adjustedAngle<0) adjustedAngle = 2 * PI - adjustedAngle

                    if(it.angle > PI){
                        currentBoid.dangle += it.angle - 2 * PI
                    }else{
                        currentBoid.dangle += it.angle
                    }
                }

                currentBoid.speed = currentBoid.dspeed / nearbyBoids.size
                if(currentBoid.dangle < 0){
                    currentBoid.angle = 2 * PI - currentBoid.dangle
                }else{
                    currentBoid.angle = currentBoid.dangle
                }
            }

            currentBoid.x = currentBoid.x + deltaT * currentBoid * cos()



            currentBoid.reset()

            currentBoid.log()

            canvas.drawPoint(currentBoid.x.toFloat(),currentBoid.y.toFloat(),paint)

        }



        deltaT = Calendar.getInstance().timeInMillis - go
        go = Calendar.getInstance().timeInMillis
        //invalidate()
    }
}
