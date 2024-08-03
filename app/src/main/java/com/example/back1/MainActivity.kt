package com.example.back1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.back1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicia el servicio de socket en primer plano
        startSocketService()

        setupWindowInsets()
        setupListeners()
    }

    private fun startSocketService() {
        // Inicia el servicio de socket si no está ya en ejecución
        val serviceIntent = Intent(this, SocketService::class.java)
//        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
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
