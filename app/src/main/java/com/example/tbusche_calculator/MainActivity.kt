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
            R.id.button1 -> displayNumber += "1"
            R.id.button2 -> displayNumber += "2"
            R.id.button3 -> displayNumber += "3"
            R.id.button4 -> displayNumber += "4"
            R.id.button5 -> displayNumber += "5"
            R.id.button6 -> displayNumber += "6"
            R.id.button7 -> displayNumber += "7"
            R.id.button8 -> displayNumber += "8"
            R.id.button9 -> displayNumber += "9"
            R.id.button0 -> displayNumber += "0"
            R.id.decimal_button -> {
                if (displayNumber.contains(".")) { /* do nothing */ }
                else
                    displayNumber += "."
            }
        }
        calcDisplay.setText(displayNumber)
    }
}