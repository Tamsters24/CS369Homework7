package com.example.tbusche_calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //private val displayTop: EditText = findViewById(R.id.calc_display_top)          /* Access the top display */
    //private val displayBottom: EditText = findViewById(R.id.calc_display_bottom)    /* Access the bottom display */
    private var storedExpression = ""
    private var operand1: Float = 0F
    private var operand2: Float = 0F
    private var operator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun concatNumber(view: View) {                                          /* Comments */
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        var displayNumber = displayBottom.text.toString()                   /* Convert the display text to string */
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
        displayBottom.setText(displayNumber)
    }

    fun delete(view: View) {                                                /* Because the delete button is     */
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        var displayNumber = displayBottom.text.toString()                   /* not a button but an ImageButton, */
        displayNumber =                                                     /* cannot use the previous switch   */
            if (displayNumber.length > 1)
                displayNumber.subSequence(0, displayNumber.length - 1) as String
            else
                "0"
        displayBottom.setText(displayNumber)
    }

    fun clear(view: View) {                                                 /* Resets display to initial display */
        val displayTop: EditText = findViewById(R.id.calc_display_top)
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        storedExpression = ""
        displayTop.setText("")
        displayBottom.setText("0")
    }

    fun numberOperation(view: View) {
        /* There is a top display for storing previously entered expressions
         * while the bottom display contains the current entry. When an operation
         * is clicked: 1) store the current value subsequent calculation. 2) store
         * the operand for when the calculation is executed. 3) concatenate the
         * stored expression with values and operator symbols. 4) Update the top
         * display with the stored expression. 5) Refresh the bottom display. */
        val displayTop: EditText = findViewById(R.id.calc_display_top)
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        val currentExpression = displayBottom.text.toString()
        val operatorBtn = view as Button

        // prevent operation if a second operand has not been entered
        if (isNumeric(currentExpression)) {
            /* If there is no operand1 yet, assign it and store it and the
             * operator in the top display. Otherwise, calculate the current
             * operands, and place result and new operator in the stored display
             * (until "equals" is executed) */
            if (storedExpression == "") {
                operand1 = currentExpression.toFloat()
                storedExpression += currentExpression
            } else if (isNumeric(currentExpression)) {
                operand2 = currentExpression.toFloat()
                storedExpression = calculate(operand1, operand2, operator)
                operand1 = storedExpression.toFloat()
            }

            when (operatorBtn.id) {
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
            displayTop.setText(storedExpression)
            displayBottom.setText("")
        }
    }

    fun equalsFunction(view: View) {
        /* Equals displays the last executed equation in the top display
         * and shows the result in the bottom display */
        val displayTop: EditText = findViewById(R.id.calc_display_top)
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        val currentValue = displayBottom.text.toString()

        /* If there is no operand1 yet, display "*current value* =" in the top display.
         * Otherwise, display "*operand1* *operator* *operand2* =" in the top display and
         * then assign the result to storedExpression */
        if (storedExpression == "") {
            operand1 = currentValue.toFloat()
            storedExpression += currentValue
            storedExpression += " ="
            displayTop.setText(storedExpression)
            displayBottom.setText("")
        } else if (isNumeric(currentValue)) {
            operand2 = currentValue.toFloat()
            var topExpression = "$storedExpression $currentValue"
            storedExpression = calculate(operand1, operand2, operator)
            operand1 = storedExpression.toFloat()
            topExpression = "$topExpression = "
            displayTop.setText(topExpression)
            displayBottom.setText(storedExpression)
        }
    }


    /* I've always found the percent button confusing. So I'm using the following
     * webpage as a guide:
     * https://www.how2shout.com/how-to/how-to-calculate-percentage-in-calculator-with-a-percentage-key.html */
    /*fun percent(view: View) {
        val storeddisplay = findViewById<EditText>(R.id.calc_display_top)
        val currentdisplay = findViewById<EditText>(R.id.calc_display_bottom)
        val currentexpression = currentdisplay.text.toString()

        if (storedExpression == "") {
            currentdisplay.setText("0")
        }
        else if (isNumeric(currentexpression)) {
            operand1 = storedExpression.toDouble()
            operand2 = currentexpression.toDouble()
            when (operator) {
                "/" -> {
                    operand2 /= 100
                    storedExpression = calculate(operand1, operand2, operator)
                }
                "*" -> {
                    operand2 /= 100
                    storedExpression = calculate(operand1, operand2, operator)
                }
                "-" -> {
                    operand2 = operand1 * (operand2/100)
                    storedExpression = calculate(operand1, operand2, operator)
                }
                "+" -> {
                    operand2 = operand1 * (operand2/100)
                    storedExpression = calculate(operand1, operand2, operator)
                }
            }
            operand1 = storedExpression.toDouble()
            operand2 = 0.0
            operator = ""
            storeddisplay.setText(storedExpression)
            currentdisplay.setText("")
        }
    }*/

    private fun isNumeric(string: String): Boolean {
        return string.toDoubleOrNull() != null
    }

    private fun calculate(operandOne: Float, operandTwo: Float, operator:String): String {
        var result = ""
        when (operator) {
            "/" -> result = (operandOne / operandTwo).toString()
            "*" -> result = (operandOne * operandTwo).toString()
            "-" -> result = (operandOne - operandTwo).toString()
            "+" -> result = (operandOne + operandTwo).toString()
        }
        return result
    }
}