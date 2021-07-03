package com.example.lotterymachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton: Button by lazy { findViewById(R.id.clearButton) }

    private val addButton: Button by lazy { findViewById(R.id.addButton) }

    private val runButton: Button by lazy { findViewById(R.id.runButton) }

    private val numberPicker: NumberPicker by lazy { findViewById(R.id.numberPicker) }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.number1),
            findViewById(R.id.number2),
            findViewById(R.id.number3),
            findViewById(R.id.number4),
            findViewById(R.id.number5),
            findViewById(R.id.number6)
        )
    }

    private var didRun = false
    private var pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPickerValue() //NumberPicker 초기화
        initRunButton() //랜덤 로또번호 생성
        initAddButton() //번호 추가
        initClearButton() //초기화
    }

    fun initNumberPickerValue() {
        numberPicker.minValue = 1
        numberPicker.maxValue = 45
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
            }
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            if (validateCheck()) {
                val number = numberTextViewList[pickNumberSet.size]
                number.isVisible = true
                number.text = numberPicker.value.toString()

                pickNumberSet.add(numberPicker.value)
            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }

            didRun = false
        }
    }

    private fun validateCheck(): Boolean {
        if (didRun) {
            Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pickNumberSet.size >= 5) {
            Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pickNumberSet.contains(numberPicker.value)) {
            Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun getRandomNumber(): List<Int> {

        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }

                this.add(i)
            }
        }

        numberList.shuffle()

        val newNumberList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        return newNumberList.sorted()
    }
}