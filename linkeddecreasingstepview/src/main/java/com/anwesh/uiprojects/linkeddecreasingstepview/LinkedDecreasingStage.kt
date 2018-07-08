package com.anwesh.uiprojects.linkeddecreasingstepview

/**
 * Created by anweshmishra on 08/07/18.
 */

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.MotionEvent

val NODES : Int = 5

class LinkedDecreasingStage (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    var onCompletionListener : CompletionListener? = null

    fun addOnCompletionListener(onComplete : (Int) -> Unit) {
        onCompletionListener = CompletionListener(onComplete)
    }

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(prevScale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class DSNode(var i : Int, val state : State = State()) {

        var next : DSNode? = null

        var prev : DSNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < NODES - 1) {
                next = DSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val hGap : Float = 0.9f * h / NODES
            canvas.save()
            canvas.translate(w/2, 0.05f * h + i * hGap + hGap * state.scale)
            canvas.drawLine(-w/8, 0f, w/8, 0f, paint)
            canvas.restore()
            next?.draw(canvas, paint)
        }

        fun update(stopcb : (Int, Float) -> Unit) {
            state.update {
                stopcb(i, it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : DSNode{
            var curr : DSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class LinkedDecreasingStep(var i : Int) {

        private var curr : DSNode = DSNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            paint.color = Color.parseColor("#00897B")
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeWidth = Math.min(canvas.width, canvas.height).toFloat() / 60
            curr.draw(canvas, paint)
        }

        fun update(stopcb : (Int, Float) -> Unit) {
            curr.update {j, scale ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                stopcb(j, scale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }
    }

    data class Renderer(var view : LinkedDecreasingStage) {

        private var animator : Animator = Animator(view)

        private var lds : LinkedDecreasingStep = LinkedDecreasingStep(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#BDBDBD"))
            lds.draw(canvas, paint)
            animator.animate {
                lds.update {j, scale ->
                    animator.stop()
                    when (scale) {
                        1f -> view.onCompletionListener?.onComplete?.invoke(j)
                    }
                }
            }
        }

        fun handleTap() {
            lds.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity)  : LinkedDecreasingStage {
            val view : LinkedDecreasingStage = LinkedDecreasingStage(activity)
            activity.setContentView(view)
            return view
        }
    }

    data class CompletionListener(var onComplete : (Int) -> Unit)
}