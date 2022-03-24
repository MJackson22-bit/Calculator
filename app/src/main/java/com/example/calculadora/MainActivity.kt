package com.example.calculadora

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.mariuszgromada.math.mxparser.*
import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import com.example.calculadora.databinding.ActivityMainBinding
import java.lang.StringBuilder
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etOperacion.showSoftInputOnFocus = false
        buttons()
    }

    private fun buttons() {
        binding.btn0.setOnClickListener {
            textChange("0")
        }
        binding.btn1.setOnClickListener {
            textChange("1")
        }
        binding.btn2.setOnClickListener {
            textChange("2")
        }
        binding.btn3.setOnClickListener {
            textChange("3")
        }
        binding.btn4.setOnClickListener {
            textChange("4")
        }
        binding.btn5.setOnClickListener {
            textChange("5")
        }
        binding.btn6.setOnClickListener {
            textChange("6")
        }
        binding.btn7.setOnClickListener {
            textChange("7")
        }
        binding.btn8.setOnClickListener {
            textChange("8")
        }
        binding.btn9.setOnClickListener {
            textChange("9")
        }
        binding.btnClear.setOnClickListener {
            binding.etOperacion.setText("")
        }
        binding.btnPercent.setOnClickListener {
            textChange("%")
        }
        binding.btnMore.setOnClickListener {
            textChange("+")
        }
        binding.btnLess.setOnClickListener {
            textChange("-")
        }
        binding.btnMultiply.setOnClickListener {
            textChange("×")
        }
        binding.btnEnter.setOnClickListener {
            textChange("÷")
        }
        binding.btnBackspace.setOnClickListener {
            val positionCursor: Int = binding.etOperacion.selectionStart
            val textLen: Int = binding.etOperacion.text.length
            if (positionCursor != 0 && textLen != 0) {
                val selection: SpannableStringBuilder =
                    binding.etOperacion.text as SpannableStringBuilder
                selection.replace(positionCursor - 1, positionCursor, "")
                binding.etOperacion.text = selection
                binding.etOperacion.setSelection(positionCursor - 1)
            }
        }
        binding.btnParentesis.setOnClickListener {
            val cursorPosition = binding.etOperacion.selectionStart
            var open = 0
            var closed = 0
            val textLen = binding.etOperacion.text.length
            for (i in 0 until cursorPosition) {
                if (binding.etOperacion.text.toString().substring(i, i + 1) == "(") {
                    open += 1
                }
                if (binding.etOperacion.text.toString().substring(i, i + 1) == ")") {
                    closed += 1
                }
            }
            if (open == closed || binding.etOperacion.text.toString()
                    .substring(textLen - 1, textLen) == "("
            ) {
                textChange("(")
            } else if (closed < open && binding.etOperacion.text.toString()
                    .substring(textLen - 1, textLen) != "("
            ) {
                textChange(")")
            }
            binding.etOperacion.setSelection(cursorPosition + 1)
        }
        binding.btnEqual.setOnClickListener {
            var expression: String = binding.etOperacion.text.toString()
            expression = expression.replace("÷", "/")
            expression = expression.replace("×", "*")
            val exp: Expression = Expression(expression)
            val result: String = exp.calculate().toString()
            if(result.toDouble().isNaN()) {
                Toast.makeText(this, "El formato usado no es válido.", Toast.LENGTH_SHORT).show()
                binding.tvResultado.text = ""
            }else{
                binding.etOperacion.setText(result)
                binding.etOperacion.setSelection(result.length)
                binding.tvResultado.text = ""
            }
        }
        binding.etOperacion.doOnTextChanged { _, _, _, _ ->
            var expression: String = binding.etOperacion.text.toString()
            expression = expression.replace("÷", "/")
            expression = expression.replace("×", "*")
            val exp = Expression(expression)
            val result: String = exp.calculate().toString()
            if(result.toDouble().isNaN()){
                binding.tvResultado.text = ""
            }else{
                binding.tvResultado.text = result
            }
        }
        binding.btnMoreLess.setOnClickListener {
            val valor = binding.tvResultado.text.toString().toDouble()
            val result = valor * (-1)
            binding.etOperacion.setText(result.toString())
        }
    }

    private fun textChange(text: String) {
        val belowString: String = binding.etOperacion.text.toString()
        val cursorPosition: Int = binding.etOperacion.selectionStart
        val startText: String = belowString.substring(0, cursorPosition)
        val endText: String = belowString.substring(cursorPosition)
        binding.etOperacion.setText(String.format("%s%s%s", startText, text, endText))
        binding.etOperacion.setSelection(cursorPosition + 1)
    }

}