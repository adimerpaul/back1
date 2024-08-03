package com.example.back1

import android.os.Bundle
import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.back1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.add.setOnClickListener {
            val n1 = binding.num1.text.toString()
            val n2 = binding.num2.text.toString()
            viewModel.calculateSum(n1, n2)
        }

        viewModel.sumResult.observe(this, Observer { result ->
            binding.result.text = result
            if (result == "Please enter two numbers" || result == "Invalid number format") {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
