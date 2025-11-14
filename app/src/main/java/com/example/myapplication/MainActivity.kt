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
            binding.BtnZeroDigit to 0,
            binding.BtnOneDigit to 1,
            binding.BtnTwoDigit to 2,
            binding.BtnThreeDigit to 3,
            binding.BtnFourDigit to 4,
            binding.BtnFiveDigit to 5,
            binding.BtnSixDigit to 6,
            binding.BtnSevenDigit to 7,
            binding.BtnEightDigit to 8,
            binding.BtnNineDigit to 9
        )

        digitButtons.forEach { (button, digit) ->
            button.setOnClickListener {
                inputDigit(mainStr, digit)
            }
        }

        binding.btnAC.setOnClickListener {
            inputAC()
        }

        binding.BtnSymbolDelete.setOnClickListener {
            val editable = binding.MainExpressionInput.text
            if (editable.isNotEmpty()) {
                editable.delete(editable.length - 1, editable.length)
            }
        }

        binding.BtnDotSymbol.setOnClickListener {
            inputDot()
        }

        binding.btnBrackets.setOnClickListener {
            if (!inputOpenBracket()) {
                inputCloseBracket()
            }
        }
        val operationButtons = mapOf(
            binding.BtnPlusSymbol to "+",
            binding.BtnMinusSymbol to "-",
            binding.BtnMultiplicationSymbol to "*",
            binding.BtnDivisionSymbol to "/",
            binding.BtnPercentSymbol to "%"
        )
        operationButtons.forEach { (button, operator) ->
            button.setOnClickListener {
                addOperationSymbol(operator)
            }
        }

        binding.BtnEqualSymbol.setOnClickListener {
            inputEqual()
        }

        binding.MainExpressionInput.setOnClickListener {
            binding.MainExpressionInput.setSelection(binding.MainExpressionInput.text?.length ?: 0)
            binding.MainExpressionInput.showSoftInputOnFocus = false
        }
    }

    private fun inputDigit(str: String, digit: Int) {
        if (str.isNotEmpty() && str.last() == ')') {
            mainStr += "*$digit"
            binding.MainExpressionInput.append("*$digit")
        } else {
            mainStr += "$digit"
            binding.MainExpressionInput.append("$digit")
        }
    }

    private fun inputEqual() {
        try {
            val str = binding.MainExpressionInput.text.toString()
            val rpn = infixToRPN(str)
            val result = evaluateRPN(rpn)
            if (kotlin.math.abs(result % 1.0) == 0.0) {
                binding.TextviewOutputTxtResult.text = result.toInt().toString()
            } else {
                binding.TextviewOutputTxtResult.text = result.toString()
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
        print("counter $counter")
        val listToken =
            str.slice(counter + 1 until str.length).split("+", "-", "*", "/", "%", "(", ")")
        print("list $listToken")
        return listToken.size > 1
    }

    private fun addOperationSymbol(op: String) {
        if (mainStr != "" && canAddOperation()) {
            mainStr += op
            binding.MainExpressionInput.append(op)
        }
    }

    private fun inputCloseBracket() {
        when {
            mainStr.last().isDigit() && bracketsOpen && canCloseBracket(mainStr) -> {
                mainStr += ")"
                binding.MainExpressionInput.append(")")
                bracketsOpen = false
            }

            (mainStr.last() == '.' && bracketsOpen) -> {
                mainStr += "0)"
                binding.MainExpressionInput.append("0)")
                bracketsOpen = false
            }
        }
    }

    private fun inputOpenBracket(): Boolean {
        return when {
            mainStr.isEmpty() || (isOperation(mainStr.last())
                    && !bracketsOpen && mainStr.last() != '(') -> {
                mainStr += "("
                binding.MainExpressionInput.append("(")
                bracketsOpen = true
                true
            }

            (mainStr.last().isDigit() || mainStr.last() == ')') && !bracketsOpen -> {
                mainStr += "*("
                binding.MainExpressionInput.append("*(")
                bracketsOpen = true
                true
            }

            mainStr.last() == '.' && !bracketsOpen -> {
                mainStr += "0*("
                binding.MainExpressionInput.append("0*(")
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
            binding.MainExpressionInput.append(".")
        }
    }

    private fun inputAC() {
        mainStr = ""
        binding.MainExpressionInput.setText("")
        binding.TextviewOutputTxtResult.text = ""
        bracketsOpen = false
    }

}















