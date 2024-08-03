package com.example.back1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _sumResult = MutableLiveData<String>()
    val sumResult: LiveData<String> get() = _sumResult

    fun calculateSum(n1: String, n2: String) {
        if (n1.isEmpty() || n2.isEmpty()) {
            _sumResult.value = "Please enter two numbers"
        } else {
            try {
                val sum = n1.toInt() + n2.toInt()
                _sumResult.value = "Result: $sum"
            } catch (e: NumberFormatException) {
                _sumResult.value = "Invalid number format"
            }
        }
    }
}
