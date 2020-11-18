package com.example.homework5sem

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var operand1: Double? = null
    private var operand2: Double? = null
    private var pendingOperation = "="
    lateinit var sharedPreferences: SharedPreferences
    private var nightMode = 0
    private var check = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE)
        nightMode = sharedPreferences.getInt("NightModeInt", 1)
        check = sharedPreferences.getBoolean("SwitchKey", true)
        switch_theme.isChecked = check
        AppCompatDelegate.setDefaultNightMode(nightMode)

        switch_theme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                check = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                check = false
            }
        }

        val oplistener = View.OnClickListener { view ->
            val button = view as Button
            val op = button.text.toString()
            val value = newnum?.text.toString()
            try {
                val doublevalue = java.lang.Double.valueOf(value)
                performOperation(doublevalue, op)
            } catch (e: NumberFormatException) {
                newnum?.setText("")
            }
            pendingOperation = op
            if (pendingOperation == "xʸ") operation?.text = "^" else operation?.text =
                pendingOperation
        }

        newnum?.isEnabled = false
        result?.isEnabled = false

        val remove = View.OnClickListener {
            if (newnum?.text.toString() == "" || result?.getText().toString() == "Error") {
                newnum?.setText("0")
                result?.setText("")
                operation?.setText("")
                operand1 = null
                operand2 = null
                buttonac.text = "AC"
            }
            var str = newnum?.getText().toString()
            if (str.length > 1) {
                str = str.substring(0, str.length - 1)
                newnum?.setText(str)
            } else if (str.length <= 1) {
                newnum?.setText("0")
                operation?.setText("")
                buttonac.text = "AC"
            }
        }

        buttonac.setOnLongClickListener {
            newnum?.setText("0")
            result?.setText("")
            operation?.text = ""
            operand1 = null
            operand2 = null
            buttonac.text = "AC"
            false
        }
        buttonac.setOnClickListener(remove)


        val listener = View.OnClickListener { view ->
            val button = view as Button
            if (newnum.text.toString() == "0") {
                newnum.setText(button.text)
            } else newnum?.append(button.text.toString())
            buttonac.text = "C"
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)
        buttonEquals.setOnClickListener(oplistener)
        buttonDivide.setOnClickListener(oplistener)
        buttonMinus.setOnClickListener(oplistener)
        buttonMultiply.setOnClickListener(oplistener)
        buttonPlus.setOnClickListener(oplistener)

        buttonneg.setOnClickListener {
            val value = newnum?.getText().toString()
            if (value.isEmpty()) {
                newnum?.setText("-")
            } else {
                try {
                    var doubleValue = java.lang.Double.valueOf(value)
                    doubleValue *= -1.0
                    if (doubleValue % 1 == 0.0) {
                        newnum?.setText(String.format("%.0f", doubleValue))
                    } else newnum?.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    // newNumber was "-" or ".", so clear it
                    newnum?.setText("")
                }
            }
        }

        buttonpercent.setOnClickListener {
            val value = newnum?.text.toString()
            try {
                var doubleValue = java.lang.Double.valueOf(value)
                doubleValue /= 100.0
                newnum?.setText(doubleValue.toString())
            } catch (e: NumberFormatException) {
                // newNumber was "-" or ".", so clear it
                newnum?.setText("")
            }
        }
    }

    private fun performOperation(value: Double, op: String) {
        var error = false
        if (null == operand1) {
            operand1 = value
        } else {
            operand2 = value
            if (pendingOperation == "=") {
                pendingOperation = op
            }
            when (pendingOperation) {
                "=" -> operand1 = operand2
                "÷" -> if (operand2 == 0.0) {
                    error = true
                } else {
                    operand1 = operand1!! / operand2!!
                }
                "x" -> operand1 = operand1!! * operand2!!
                "-" -> operand1 = operand1!! - operand2!!
                "+" -> operand1 = operand1!! + operand2!!
                "xʸ" -> operand1 = Math.pow(operand1!!, operand2!!)
                "sin" -> {
                }
            }
        }
        if (!error) {
            if (operand1!! % 1 == 0.0) {
                result!!.setText(String.format("%.0f", operand1))
            } else result!!.setText(String.format("%.4f", operand1))
        } else {
            result!!.setText("Error")
            operand1 = 0.0
        }
        newnum!!.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        nightMode = AppCompatDelegate.getDefaultNightMode()
        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("NightModeInt", nightMode)
            putBoolean("SwitchKey", check)
            commit()
        }
    }
}