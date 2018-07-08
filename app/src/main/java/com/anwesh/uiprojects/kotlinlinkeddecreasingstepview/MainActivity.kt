package com.anwesh.uiprojects.kotlinlinkeddecreasingstepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.linkeddecreasingstepview.LinkedDecreasingStage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LinkedDecreasingStage.create(this)
    }
}
