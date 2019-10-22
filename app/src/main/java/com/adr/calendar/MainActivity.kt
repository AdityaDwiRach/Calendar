package com.adr.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var testing



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSave.setOnClickListener{

            addData()
        }
    }

    private fun addData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
