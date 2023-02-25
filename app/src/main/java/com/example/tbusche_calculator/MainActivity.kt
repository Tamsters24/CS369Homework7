package com.example.tbusche_calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    /* There is a top display for storing previously entered expressions
     * while the bottom display contains the current entry. Meanwhile,
     * equations will have a first operand stored (until cleared), and
     * when an operation is performed, a second operand from the bottom
     * display value is used. The two operands are calculated according
     * to the operator indicated. This continues until the display is
     * cleared. */
    private var topExpression = ""
    private var operand1: Float = 0F
    private var operand2: Float = 0F
    private var operator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        /* The initial screen displays "0" in the bottom
         * display and nothing in the top display */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun concatNumber(view: View) {
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        var displayNumber = displayBottom.text.toString()
        val numberBtn = view as Button

        /* If on an initial screen, Replace "0" on the bottom display with
         * the new value to be concatenated. */
        if (displayNumber == "0")
            displayNumber = ""
        /* Concatenate integers or decimal */
        when (numberBtn.id) {
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
            R.id.decimal_button -> {    /* Only accept one decimal to append */
                if (displayNumber.contains(".")) { /* do nothing */ }
                else if (displayNumber == "")
                    displayNumber = "0."
                else
                    displayNumber += "."
            }
            R.id.pos_neg_button -> {    /* Switch between negative and positive */
                displayNumber = if (displayNumber == "") {
                    /* Set the bottom display to "0" */
                    "0"
                } else {
                    if (displayNumber[0] == '-')
                        displayNumber.drop(1)   // Odd. It doesn't work with n = 0
                    else
                        "-$displayNumber"
                }
            }
        }
        displayBottom.setText(displayNumber)
    }

    fun delete(view: View) {                                                    /* Because the delete button is     */
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)    /* not a button but an ImageButton, */
        var displayNumber = displayBottom.text.toString()                       /* cannot use the concat switch     */
        displayNumber =
            if (displayNumber.length > 1)
                displayNumber.subSequence(0, displayNumber.length - 1) as String
            else
                "0"
        displayBottom.setText(displayNumber)
    }

    fun numberOperation(view: View) {
        /* When an operation is clicked:
         * 1) store the current value for subsequent calculation.
         * 2) store the operand for when the calculation is executed.
         * 3) concatenate the stored expression with values and operator symbols.
         * 4) update the top display with the stored expression.
         * 5) Refresh the bottom display. */

        val displayTop: EditText = findViewById(R.id.calc_display_top)
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        val operatorBtn = view as Button

        /* If equals was last executed, replace the stored expression
         * with the result and operand, then reset the bottom display*/
        if (topExpression.contains("=")) {
            topExpression = operand1.toString()
            if (topExpression.endsWith(".0")) { // If the result is an integer, remove the trailing digit
                topExpression = topExpression.dropLast(2)
            }

            when (operatorBtn.id) {
                R.id.divide_button -> {
                    topExpression += " /"
                    operator = "/"
                }
                R.id.multiply_button -> {
                    topExpression += " *"
                    operator = "*"
                }
                R.id.subtract_button -> {
                    topExpression += " -"
                    operator = "-"
                }
                R.id.addition_button -> {
                    topExpression += " +"
                    operator = "+"
                }
            }
            displayTop.setText(topExpression)
            displayBottom.setText("")
        }

        /* Otherwise, proceed normally */
        val currentExpression = displayBottom.text.toString()
        // prevent operation if a second operand has not been entered
        if (isNumeric(currentExpression)) {
            /* If there is no operand1 yet, assign it and store it and the
             * operator in the top display. */
            if (topExpression == "") {
                operand1 = currentExpression.toFloat()
                topExpression += currentExpression
            }
            /* Otherwise, calculate the current operands, and place result
             * and new operator in the stored display (until "equals" is executed) */
            else {
                operand2 = currentExpression.toFloat()
                topExpression = calculate(operand1, operand2, operator)
                operand1 = topExpression.toFloat()
            }
            when (operatorBtn.id) {
                R.id.divide_button -> {
                    topExpression += " /"
                    operator = "/"
                }
                R.id.multiply_button -> {
                    topExpression += " *"
                    operator = "*"
                }
                R.id.subtract_button -> {
                    topExpression += " -"
                    operator = "-"
                }
                R.id.addition_button -> {
                    topExpression += " +"
                    operator = "+"
                }
            }
            displayTop.setText(topExpression)
            displayBottom.setText("")
        }
    }

    fun equalsFunction(view: View) {
        /* Equals displays the last executed equation in the top display
         * and shows the result in the bottom display */
        val displayTop: EditText = findViewById(R.id.calc_display_top)
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)

        /* Values for the top display's equation */
        var leftValue = operand1.toString()
        // If the left value is an integer, parse the digits
        if (leftValue.endsWith(".0")) {
            leftValue = leftValue.dropLast(2)
        }
        val rightValue = displayBottom.text.toString()

        /* If there is no operand1 yet, do nothing */
        if (topExpression == "") { /* Do nothing */ }
        /* Otherwise, display "*operand1* *operator* *operand2* = result"
         * in the top display and then assign the result to topExpression */
        else if (isNumeric(rightValue)) {
            operand2 = rightValue.toFloat()
            val currentResult = calculate(operand1, operand2, operator)
            val topView = "$leftValue $operator $rightValue = $currentResult"
            displayTop.setText(topView)
            operand1 = currentResult.toFloat()
            topExpression = topView
            displayBottom.setText(currentResult)
        }
    }

    private fun calculate(operandOne: Float, operandTwo: Float, operator:String): String {
        var result = ""
        when (operator) {
            "/" -> result = (operandOne / operandTwo).toString()
            "*" -> result = (operandOne * operandTwo).toString()
            "-" -> result = (operandOne - operandTwo).toString()
            "+" -> result = (operandOne + operandTwo).toString()
        }
        if (result.endsWith(".0")) {    // If the result is an integer, parse the digits
            result = result.dropLast(2)
        }
        operand1 = result.toFloat()
        return result
    }

    fun percent(view: View) {
        /* Percent will take the current display value (bottom display) and
         * then display that number with a percent symbol in the top display.
         * The bottom display will be updated with the result for that value
         * divided by 100. The percent result will be set to operand1 */
        val displayTop: EditText = findViewById(R.id.calc_display_top)
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        val bottomNumber = displayBottom.text.toString()
        val topNumber = "$bottomNumber%"
        displayTop.setText(topNumber)
        var numberValue = bottomNumber.toFloat()
        numberValue /= 100
        displayBottom.setText(numberValue.toString())
        operand1 = numberValue
    }

    fun clear(view: View) {                                         /* Resets all values and initializes display */
        val displayTop: EditText = findViewById(R.id.calc_display_top)
        val displayBottom: EditText = findViewById(R.id.calc_display_bottom)
        topExpression = ""
        operand1 = 0F
        operand2 = 0F
        operator = ""
        displayTop.setText("")
        displayBottom.setText("0")
    }

    private fun isNumeric(string: String): Boolean {
        return string.toDoubleOrNull() != null
    }

}