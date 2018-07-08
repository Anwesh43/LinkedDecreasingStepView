package com.anwesh.uiprojects.kotlinlinkeddecreasingstepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.anwesh.uiprojects.linkeddecreasingstepview.LinkedDecreasingStage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view : LinkedDecreasingStage =  LinkedDecreasingStage.create(this)
        fullScreen()
        view.addOnCompletionListener {
            Toast.makeText(this, "animation ${it+1} completed", Toast.LENGTH_SHORT).show()
        }
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}