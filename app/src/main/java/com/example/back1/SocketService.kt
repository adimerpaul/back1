package com.example.back1
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.app.Service
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketService : Service() {
    private lateinit var mSocket: Socket
    private val CHANNEL_ID = "socket_channel_id"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Socket Service")
            .setContentText("Listening for messages...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Reemplaza con tu icono
            .build()
        startForeground(1, notification)

        setupSocket()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Socket Service Channel"
            val descriptionText = "Channel for socket service"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupSocket() {
        try {
            mSocket = IO.socket("http://192.168.1.3:3000") // Reemplaza con tu URL de socket
            mSocket.connect()
            mSocket.on("chat message") { args ->
                val data = args[0] as String
                showNotification(data)
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(message: String) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Reemplaza con tu icono
            .setContentTitle("New Message")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(2, notificationBuilder.build())
    }
}
