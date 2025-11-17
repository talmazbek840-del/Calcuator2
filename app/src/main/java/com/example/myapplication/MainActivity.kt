package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var numberLIST: List<String>
    private var mainStr: String = ""
    private var bracketsOpen: Boolean = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonClick()
    }

    private fun buttonClick() {
        val digitButtons = listOf(
            binding.btnZeroDigit to 0,
            binding.btnOneDigit to 1,
            binding.btnTwoDigit to 2,
            binding.btnThreeDigit to 3,
            binding.btnFourDigit to 4,
            binding.btnFiveDigit to 5,
            binding.btnSixDigit to 6,
            binding.btnSevenDigit to 7,
            binding.btnEightDigit to 8,
            binding.btnNineDigit to 9
        )

        digitButtons.forEach { (button, digit) ->
            button.setOnClickListener {
                inputDigit(mainStr, digit)
            }
        }

        binding.btnAC.setOnClickListener {
            inputAC()
        }

        binding.btnSymbolDelete.setOnClickListener {
            val editable = binding.mainExpressionInput.text
            if (editable.isNotEmpty()) {
                editable.delete(editable.length - 1, editable.length)
            }
        }

        binding.btnDotSymbol.setOnClickListener {
            inputDot()
        }

        binding.btnBracketsSymbol.setOnClickListener {
            if (!inputOpenBracket()) {
                inputCloseBracket()
            }
        }
        val operationButtons = mapOf(
            binding.btnPlusSymbol to "+",
            binding.btnMinusSymbol to "-",
            binding.btnMultiplicationSymbol to "*",
            binding.btnDivisionSymbol to "/",
            binding.btnPercentSymbol to "%"
        )
        operationButtons.forEach { (button, operator) ->
            button.setOnClickListener {
                addOperationSymbol(operator)
            }
        }

        binding.btnEqual.setOnClickListener {
            inputEqual()
        }

        binding.mainExpressionInput.setOnClickListener {
            binding.mainExpressionInput.setSelection(binding.mainExpressionInput.text?.length ?: 0)
            binding.mainExpressionInput.showSoftInputOnFocus = false
        }
    }

    private fun inputDigit(str: String, digit: Int) {
        if (str.isNotEmpty() && str.last() == ')') {
            mainStr += "*$digit"
            binding.mainExpressionInput.append("*$digit")
        } else {
            mainStr += "$digit"
            binding.mainExpressionInput.append("$digit")
        }
    }

    private fun inputEqual() {
        try {
            val str = binding.mainExpressionInput.text.toString()
            val rpn = infixToRPN(str)
            val result = evaluateRPN(rpn)
            if (kotlin.math.abs(result % 1.0) == 0.0) {
                binding.textviewOutputTxtResult.text = result.toInt().toString()
            } else {
                binding.textviewOutputTxtResult.text = result.toString()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this, getString(R.string.errorInput), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isOperation(lastChar: Char): Boolean {
        return lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/'
                || lastChar == '%'
    }

    private fun canAddOperation(): Boolean {
        return mainStr.isNotEmpty() && (mainStr.last()
            .isDigit() || mainStr.last() == ')') && mainStr.last() != '('
    }

    private fun canCloseBracket(str: String): Boolean {
        if (str == "") return false
        var counter = 0
        for (i in str.length - 1 downTo 0) {
            if (str[i] == '(') {
                counter = i
            }
        }
        val listToken =
            str.slice(counter + 1 until str.length).split("+", "-", "*", "/", "%", "(", ")")
        return listToken.size > 1
    }

    private fun addOperationSymbol(op: String) {
        if (mainStr != "" && canAddOperation()) {
            mainStr += op
            binding.mainExpressionInput.append(op)
        }
    }

    private fun inputCloseBracket() {
        when {
            mainStr.last().isDigit() && bracketsOpen && canCloseBracket(mainStr) -> {
                mainStr += ")"
                binding.mainExpressionInput.append(")")
                bracketsOpen = false
            }

            (mainStr.last() == '.' && bracketsOpen) -> {
                mainStr += "0)"
                binding.mainExpressionInput.append("0)")
                bracketsOpen = false
            }
        }
    }

    private fun inputOpenBracket(): Boolean {
        return when {
            mainStr.isEmpty() || (isOperation(mainStr.last())
                    && !bracketsOpen && mainStr.last() != '(') -> {
                mainStr += "("
                binding.mainExpressionInput.append("(")
                bracketsOpen = true
                true
            }

            (mainStr.last().isDigit() || mainStr.last() == ')') && !bracketsOpen -> {
                mainStr += "*("
                binding.mainExpressionInput.append("*(")
                bracketsOpen = true
                true
            }

            mainStr.last() == '.' && !bracketsOpen -> {
                mainStr += "0*("
                binding.mainExpressionInput.append("0*(")
                bracketsOpen = true
                true
            }

            else -> false
        }
    }

    private fun inputDot() {
        numberLIST = mainStr.split("+", "-", "*", "/", "%", "(", ")")
        val lastPart = numberLIST.lastOrNull() ?: ""

        if (mainStr != "" && mainStr.last() != '.' && mainStr.last()
                .isDigit() && !lastPart.contains(".")
        ) {
            mainStr += "."
            binding.mainExpressionInput.append(".")
        }
    }

    private fun inputAC() {
        mainStr = ""
        binding.mainExpressionInput.setText("")
        binding.textviewOutputTxtResult.text = ""
        bracketsOpen = false
    }

}















