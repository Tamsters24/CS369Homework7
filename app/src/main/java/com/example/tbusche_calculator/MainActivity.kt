package com.example.tbusche_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun concatNumber(view: View) {
        val calcDisplay = findViewById<EditText>(R.id.calculator_display)
        var displayNumber = calcDisplay.text.toString()
        val numberbtn = view as Button
        when (numberbtn.id) {
            R.id.button7 -> displayNumber += "7"
            R.id.button8 -> displayNumber += "8"
            R.id.button9 -> displayNumber += "9"
        }
        calcDisplay.setText(displayNumber)
    }
}