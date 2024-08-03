package com.example.back1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.back1.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupSocket()
        setupListeners()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSocket() {
        try {
            mSocket = IO.socket("http://192.168.1.3:3000")
            mSocket.connect()
            mSocket.on("chat message") { args ->
                runOnUiThread {
                    val data = args[0] as String
                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Toast.makeText(this, "Error connecting to socket", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        binding.add.setOnClickListener {
            val n1 = binding.num1.text.toString()
            val n2 = binding.num2.text.toString()
            viewModel.calculateSum(n1, n2)
        }

        viewModel.sumResult.observe(this, { result ->
            binding.result.text = result
            if (result == "Please enter two numbers" || result == "Invalid number format") {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
