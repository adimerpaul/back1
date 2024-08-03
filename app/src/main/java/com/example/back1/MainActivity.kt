package com.example.back1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.back1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.add.setOnClickListener {
            val n1 = binding.num1.text.toString()
            val n2 = binding.num2.text.toString()
            if (n1.isEmpty() || n2.isEmpty()) {
                binding.result.text = "Please enter two numbers"
                Toast.makeText(this, "Please enter two numbers", Toast.LENGTH_SHORT).show()
            } else {
                val sum = n1.toInt() + n2.toInt()
                binding.result.text = "Result: $sum"
            }
        }
    }
}