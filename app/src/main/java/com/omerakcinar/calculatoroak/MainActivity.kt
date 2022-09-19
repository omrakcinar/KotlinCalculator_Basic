package com.omerakcinar.calculatoroak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.omerakcinar.calculatoroak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    fun numberPressed(view: View) {
        if (view is Button) {
            binding.pressedShowText.append(view.text)
            canAddOperation = true
            canAddDecimal = true
        }
    }

    fun decimalPressed(view: View) {
        if (view is Button && view.text == "." && canAddDecimal) {
            binding.pressedShowText.append(view.text)
            canAddDecimal = false
        }
    }

    fun operationPressed(view: View) {
        if (view is Button && canAddOperation) {
            binding.pressedShowText.append(view.text)
            canAddOperation = false
            canAddDecimal = true

        }
    }

    fun allClearPressed(view: View) {
        binding.pressedShowText.text = ""
        binding.resultShowText.text = ""
    }

    fun backSpacePressed(view: View) {
        val length = binding.pressedShowText.length()
        if (length > 0) {
            binding.pressedShowText.text = binding.pressedShowText.text.subSequence(0, length - 1)
        }
    }

    fun equalsPressed(view: View) {
        val separateCharacters = separateCharacters()

        val timesDivision = calcDivideOrMultiply(separateCharacters)

        val result = addOrSubtract(timesDivision)
        binding.resultShowText.text = result.toString()
    }

    private fun separateCharacters(): ArrayList<Any> {
        var eachDigit = ""
        val operationList = arrayListOf<Any>()

        if (binding.pressedShowText.text != "") {
            for (character in binding.pressedShowText.text) {
                if (character.isDigit() || character == '.') {
                    eachDigit += character
                } else {
                    operationList.add(eachDigit.toFloat())
                    eachDigit = ""
                    operationList.add(character)
                }
            }
            if (eachDigit != "") {
                operationList.add(eachDigit.toFloat())
            }
        }
        return operationList
    }

    private fun divideOrMultiply(comingList: ArrayList<Any>): ArrayList<Any> {
        val newList = arrayListOf<Any>()
        var restartIndex = comingList.size

        for (i in comingList.indices) {
            if (comingList[i] is Char && i != comingList.lastIndex && i < restartIndex) {
                val operator = comingList[i]
                val prevDigit = comingList[i - 1] as Float
                val nextDigit = comingList[i + 1] as Float
                when (operator) {
                    '×' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '÷' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex) {
                newList.add(comingList[i])
            }
        }
        println("NEW LIST = " + newList)
        return newList

    }

    private fun calcDivideOrMultiply(passedList: ArrayList<Any>): ArrayList<Any> {
        var list = passedList
        while (list.contains('×') || list.contains('÷')) {
            list = divideOrMultiply(list)
        }
        return list
    }

    private fun addOrSubtract(passedList: ArrayList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+') {
                    result += nextDigit
                }
                if (operator == '-') {
                    result -= nextDigit
                }
            }
        }

        return result

    }
}

