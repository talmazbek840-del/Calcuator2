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
            binding.btnZero to 0,
            binding.btnOne to 1,
            binding.btnTwo to 2,
            binding.btnThree to 3,
            binding.btnFour to 4,
            binding.btnFive to 5,
            binding.btnSix to 6,
            binding.btnSeven to 7,
            binding.btnEight to 8,
            binding.btnNine to 9
        )

        digitButtons.forEach { (button, digit) ->
            button.setOnClickListener {
                inputDigit(mainStr, digit)
            }
        }

        binding.btnAC.setOnClickListener {
            inputAC()
        }

        binding.btnDelete.setOnClickListener {
            val editable = binding.mainInput.text
            if (editable.isNotEmpty()) {
                editable.delete(editable.length - 1, editable.length)
            }
        }

        binding.btnDot.setOnClickListener {
            inputDot()
        }

        binding.btnBrackets.setOnClickListener {
            if (!inputOpenBracket()) {
                inputCloseBracket()
            }
        }
        val operationButtons = mapOf(
            binding.btnPlus to "+",
            binding.btnMinus to "-",
            binding.btnMultiplication to "*",
            binding.btnDivision to "/",
            binding.btnPercent to "%"
        )
        operationButtons.forEach { (button, operator) ->
            button.setOnClickListener {
                addOperation(operator)
            }
        }

        binding.btnEqual.setOnClickListener {
            inputEqual()
        }

        binding.mainInput.setOnClickListener {
            binding.mainInput.setSelection(binding.mainInput.text?.length ?: 0)
            binding.mainInput.showSoftInputOnFocus = false
        }
    }

    private fun inputDigit(str: String, digit: Int) {
        if (str.isNotEmpty() && str.last() == ')') {
            mainStr += "*$digit"
            binding.mainInput.append("*$digit")
        } else {
            mainStr += "$digit"
            binding.mainInput.append("$digit")
        }
    }

    private fun inputEqual() {
        try {
            val str = binding.mainInput.text.toString()
            val rpn = infixToRPN(str)
            val result = evaluateRPN(rpn)
            if (kotlin.math.abs(result % 1.0) == 0.0) {
                binding.outputResult.text = result.toInt().toString()
            } else {
                binding.outputResult.text = result.toString()
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

    private fun addOperation(op: String) {
        if (mainStr != "" && canAddOperation()) {
            mainStr += op
            binding.mainInput.append(op)
        }
    }

    private fun inputCloseBracket() {
        when {
            mainStr.last().isDigit() && bracketsOpen && canCloseBracket(mainStr) -> {
                mainStr += ")"
                binding.mainInput.append(")")
                bracketsOpen = false
            }

            (mainStr.last() == '.' && bracketsOpen) -> {
                mainStr += "0)"
                binding.mainInput.append("0)")
                bracketsOpen = false
            }
        }
    }

    private fun inputOpenBracket(): Boolean {
        return when {
            mainStr.isEmpty() || (isOperation(mainStr.last())
                    && !bracketsOpen && mainStr.last() != '(') -> {
                mainStr += "("
                binding.mainInput.append("(")
                bracketsOpen = true
                true
            }

            (mainStr.last().isDigit() || mainStr.last() == ')') && !bracketsOpen -> {
                mainStr += "*("
                binding.mainInput.append("*(")
                bracketsOpen = true
                true
            }

            mainStr.last() == '.' && !bracketsOpen -> {
                mainStr += "0*("
                binding.mainInput.append("0*(")
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
            binding.mainInput.append(".")
        }
    }

    private fun inputAC() {
        mainStr = ""
        binding.mainInput.setText("")
        binding.outputResult.text = ""
        bracketsOpen = false
    }

}















