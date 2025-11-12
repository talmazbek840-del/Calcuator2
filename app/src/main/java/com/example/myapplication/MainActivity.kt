package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var numberLIST: List<String>
    var mainStr: String = ""
    var bracketsOpen = false
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.nine.setOnClickListener {
            InputDigit(mainStr, 9)
        }
        binding.eight.setOnClickListener {
            InputDigit(mainStr, 8)
        }
        binding.seven.setOnClickListener {
            InputDigit(mainStr, 7)
        }
        binding.six.setOnClickListener {
            InputDigit(mainStr, 6)
        }
        binding.five.setOnClickListener {
            InputDigit(mainStr, 5)
        }
        binding.four.setOnClickListener {
            InputDigit(mainStr, 4)
        }
        binding.three.setOnClickListener {
            InputDigit(mainStr, 3)
        }
        binding.two.setOnClickListener {
            InputDigit(mainStr, 2)
        }
        binding.one.setOnClickListener {
            InputDigit(mainStr, 1)
        }
        binding.zero.setOnClickListener {
            InputDigit(mainStr, 0)
        }
        binding.AC.setOnClickListener {
            inputAC()

        }

        binding.delete.setOnClickListener {
            val editable = binding.mainInput.text
            if (editable.isNotEmpty()) {
                editable.delete(editable.length - 1, editable.length)
            }
        }

        binding.dot.setOnClickListener {
            inputDot()


        }

        binding.brackets.setOnClickListener {
            if (inputOpenBracket()) {
            } else inputCloseBracket()
        }

        binding.plus.setOnClickListener {
            addOperationSymbol("+")
        }
        binding.minus.setOnClickListener {
            addOperationSymbol("-")
        }

        binding.multiplication.setOnClickListener {
            addOperationSymbol("*")
        }
        binding.division.setOnClickListener {
            addOperationSymbol("/")
        }
        binding.percent.setOnClickListener {
            addOperationSymbol("%")
        }


        binding.equal.setOnClickListener {
            inputEqual(mainStr)
        }

        binding.mainInput.setOnClickListener {
            // Move cursor to the end of the text
            binding.mainInput.setSelection(binding.mainInput.text?.length ?: 0)
            binding.mainInput.showSoftInputOnFocus=false
        }
    }

    fun InputDigit(str: String, digit: Int) {
        if (str.length != 0 && str.last() == ')') {
            mainStr += "*$digit"
            binding.mainInput.append("*$digit")
        } else {
            mainStr += "$digit"
            binding.mainInput.append("$digit")

        }

    }

    fun inputEqual(str: String) {
        try {
            val str = binding.mainInput.text.toString()
            val rpn = infixToRPN(str)
            val result = evaluateRPN(rpn)

            if (result % 1.0 == 0.0) {
                binding.output.text = "${result.toInt()}"
            } else {
                binding.output.text = "${result}"
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка ввода данных", Toast.LENGTH_SHORT).show()
        }

    }
    fun isOperation(lastchar: Char): Boolean {
        return lastchar == '+' || lastchar == '-' || lastchar == '*' || lastchar == '/' || lastchar == '%'
    }
    fun canAddOperation(lastchar: Char): Boolean {
        return mainStr.length != 0 && (mainStr.last()
            .isDigit() || mainStr.last() == ')') && mainStr.last() != '('
    }
    fun canCloseBracket(str: String): Boolean {
        if (str == "") return false
        var counter = 0
        val listToken: List<String>
        for (i in str.length - 1 downTo 0) {
            if (str[i] == '(') {
                counter = i
            }
        }

        print("counter $counter")

        listToken = str.slice(counter + 1 until str.length).split("+", "-", "*", "/", "%", "(", ")")
        print("list $listToken")
        return listToken.size > 1
    }
    fun addOperationSymbol(op: String) {
        if (mainStr != "" && canAddOperation(mainStr.last())) {
            mainStr += op

            binding.mainInput.append(op)
        }
    }
    fun inputCloseBracket() {
        if (mainStr.last().isDigit() && bracketsOpen == true && canCloseBracket(mainStr)) {
            mainStr += ")"
            binding.mainInput.append(")")
            bracketsOpen = false
        } else if (mainStr.last() == '.' && bracketsOpen == true) {

            mainStr += "0)"
            binding.mainInput.append("0)")
            bracketsOpen = false

        }
    }
    fun inputOpenBracket(): Boolean {
        if (mainStr.length == 0 || isOperation(mainStr.last())
            && bracketsOpen == false && mainStr.last() != '('
        ) {
            mainStr += "("
            binding.mainInput.append("(")
            bracketsOpen = true
            return true

        } else if ((mainStr.last()
                .isDigit() || mainStr.last() == ')') && bracketsOpen == false
        ) {
            mainStr += "*("
            binding.mainInput.append("*(")
            bracketsOpen = true
            return true

        } else if (mainStr.last() == '.' && bracketsOpen == false) {
            mainStr += "0*("
            binding.mainInput.append("0*(")
            bracketsOpen = true
            return true
        } else return false
    }

    fun inputDot() {
        numberLIST = mainStr.split("+", "-", "*", "/", "%", "(", ")")
        val lastPart = numberLIST.lastOrNull() ?: ""

        if (mainStr != "" && mainStr.last() != '.' && mainStr.last()
                .isDigit() && !lastPart.contains(".")
        ) {
            mainStr += "."
            binding.mainInput.append(".")
        }
    }

    fun inputAC() {
        mainStr = ""
        binding.mainInput.setText("")
        binding.output.text = ""
        bracketsOpen = false
    }


}















