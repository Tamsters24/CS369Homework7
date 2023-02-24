package com.example.tbusche_calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var storedExpression = ""
    private var operand1: Double = 0.0
    private var operand2: Double = 0.0
    private var operator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun concatNumber(view: View) {                                          /* Comments */
        val calcDisplay = findViewById<EditText>(R.id.calc_display_bottom)  /* Access the bottom display */
        var displayNumber = calcDisplay.text.toString()                     /* Convert the display text to string */
        val numberBtn = view as Button                                      /* Access the button clicked */
        if (displayNumber == "0")                                           /* Initial display shows 0 */
            displayNumber = ""                                              /* Remove initial display for new value */
        when (numberBtn.id) {                                               /* Switch for button clicked */
            R.id.button1 -> displayNumber += "1"                            /* 1 thru 9 is appended to display text */
            R.id.button2 -> displayNumber += "2"
            R.id.button3 -> displayNumber += "3"
            R.id.button4 -> displayNumber += "4"
            R.id.button5 -> displayNumber += "5"
            R.id.button6 -> displayNumber += "6"
            R.id.button7 -> displayNumber += "7"
            R.id.button8 -> displayNumber += "8"
            R.id.button9 -> displayNumber += "9"
            R.id.button0 -> displayNumber += "0"
            R.id.decimal_button -> {                                        /* Only accept one decimal to append */
                if (displayNumber.contains(".")) { /* do nothing */ }
                else if (displayNumber == "")
                    displayNumber = "0."
                else
                    displayNumber += "."
            }
            R.id.pos_neg_button -> {                                        /* Switch between negative and positive */
                displayNumber =
                    if (displayNumber[0] == '-')
                        displayNumber.drop(1)   // Odd. It doesn't work as intended for n = 0
                    else
                        "-$displayNumber"
            }
        }
        calcDisplay.setText(displayNumber)
    }

    fun delete(view: View) {                                                /* Because the delete button is     */
        val calcDisplay = findViewById<EditText>(R.id.calc_display_bottom)  /* not a button but an ImageButton, */
        var displayNumber = calcDisplay.text.toString()                     /* cannot use the previous switch   */
        displayNumber =
            if (displayNumber.length > 1)
                displayNumber.subSequence(0, displayNumber.length - 1) as String
            else
                "0"
        calcDisplay.setText(displayNumber)
    }

    fun clear(view: View) {                                                 /* Resets display to initial display */
        val displayTop = findViewById<EditText>(R.id.calc_display_top)
        val displayBottom = findViewById<EditText>(R.id.calc_display_bottom)
        storedExpression = ""
        displayTop.setText("")
        displayBottom.setText("0")
    }

    fun numberOperation(view: View) {
        /* There is a top display for storing previously entered expressions
         * while the bottom display contains the current entry. When an operation
         * is clicked: 1) store the current value for calculation. 2) store
         * the operand for the final calculation. 3) concatenate the stored
         * expression with values and operator symbols. 4) Update the top display
         * with the stored expression. 5) Refresh the bottom display. */
        val storedDisplay = findViewById<EditText>(R.id.calc_display_top)
        val currentDisplay = findViewById<EditText>(R.id.calc_display_bottom)
        val previousExpression = storedDisplay.text.toString()
        val currentExpression = currentDisplay.text.toString()
        val operatorBtn = view as Button

        if (isNumeric(currentExpression)) {
            /* If there is no operand1 yet, assign it and store it and the
             * operator in the top display. Otherwise, calculate the current
             * operands, and place result and new operator in the stored display
             * (until "equals" is executed) */
            if (previousExpression == "") {
                operand1 = currentExpression.toDouble()
                storedExpression += currentExpression
            } else if (isNumeric(currentExpression)) {
                operand2 = currentExpression.toDouble()
                storedExpression = calculate(operand1, operand2, operator)
                operand1 = storedExpression.toDouble()
            }

            when (operatorBtn.id) {
                R.id.percent_button -> {
                    storedExpression += " %"
                    operator = "%"
                }
                R.id.divide_button -> {
                    storedExpression += " /"
                    operator = "/"
                }
                R.id.multiply_button -> {
                    storedExpression += " *"
                    operator = "*"
                }
                R.id.subtract_button -> {
                    storedExpression += " - "
                    operator = "-"
                }
                R.id.addition_button -> {
                    storedExpression += " + "
                    operator = "+"
                }
            }
            storedDisplay.setText(storedExpression)
            currentDisplay.setText("")
        }
    }

    private fun isNumeric(string: String): Boolean {
        return string.toDoubleOrNull() != null
    }

    private fun calculate(operand1: Double, operand2: Double, operator:String): String {
        var result = ""
        when (operator) {
            "/" -> result = (operand1 / operand2).toString()
            "*" -> result = (operand1 * operand2).toString()
            "-" -> result = (operand1 - operand2).toString()
            "+" -> result = (operand1 + operand2).toString()
        }
        return result
    }
}