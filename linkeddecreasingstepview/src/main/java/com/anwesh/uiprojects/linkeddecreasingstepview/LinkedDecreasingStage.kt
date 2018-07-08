package com.anwesh.uiprojects.linkeddecreasingstepview

/**
 * Created by anweshmishra on 08/07/18.
 */

import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.view.View
import android.view.MotionEvent

val NODES : Int = 5

class LinkedDecreasingStage (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                
            }
        }
        return true
    }

}