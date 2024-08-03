package com.example.back1

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.back1.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mSocket: Socket
    private val CHANNEL_ID = "my_channel_id"
    private val NOTIFICATION_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestNotificationPermission()
        createNotificationChannel()
        setupWindowInsets()
        setupSocket()
        setupListeners()
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not included in the
        // support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.areNotificationsEnabled()) {
                // Redirigir al usuario a la configuración de notificaciones
                startActivity(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .apply {
                        putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, packageName)
                    })
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun sendNotification(message: String) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Reemplaza con tu icono
            .setContentTitle("New Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
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
                    sendNotification(data)
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
