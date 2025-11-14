package com.example.myapplication

import java.util.Stack

fun infixToRPN(expression: String): List<String> {
    val output = mutableListOf<String>()
    val stack = Stack<Char>()

    val tokens = tokenize(expression)

    for (token in tokens) {
        when {
            token.isDoubleOrInt() -> output.add(token)
            token.length == 1 && isOperator(token[0]) -> {
                val op = token[0]
                while (stack.isNotEmpty() && precedence(stack.peek()) >= precedence(op)) {
                    output.add(stack.pop().toString())
                }
                stack.push(op)
            }
            token == "(" -> stack.push('(')
            token == ")" -> {
                while (stack.isNotEmpty() && stack.peek() != '(') {
                    output.add(stack.pop().toString())
                }
                if (stack.isNotEmpty() && stack.peek() == '(') stack.pop()
            }
        }
    }

    while (stack.isNotEmpty()) {
        output.add(stack.pop().toString())
    }

    return output
}

fun evaluateRPN(rpn: List<String>): Double {
    val stack = Stack<Double>()

    for (token in rpn) {
        if (token.isDoubleOrInt()) {
            stack.push(token.toDouble())
        } else if (token.length == 1 && isOperator(token[0])) {
            val b = stack.pop()
            val a = stack.pop()
            val result = when (token[0]) {
                '+' -> a + b
                '-' -> a - b
                '×', '*' -> a * b
                '÷', '/' -> a / b
                '%' -> a/100.0*b
                else -> throw IllegalArgumentException("Unknown operator: ${token[0]}")
            }
            stack.push(result)
        }
    }
    return  stack.pop()

}

fun tokenize(expression: String): List<String> {
    val regex = Regex("([+\\-×÷*/()%])")
    return expression
        .replace(" ", "")
        .replace(regex, " $1 ")
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
}

fun isOperator(c: Char) = c in listOf('+', '-', '×', '÷', '*', '/','%')

fun precedence(c: Char): Int = when (c) {
    '+', '-' -> 1
    '×', '*', '÷', '/' ,'%'-> 2
    else -> 0
}

fun String.isDoubleOrInt(): Boolean = this.toDoubleOrNull() != null

