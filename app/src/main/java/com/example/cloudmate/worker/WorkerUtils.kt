package com.example.cloudmate.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cloudmate.MainActivity
import com.example.cloudmate.R

fun makeFloodNotification(
    message: String,
    context: Context
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            FLOOD_ALERT_CHANNEL_ID,
            FLOOD_ALERT_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = FLOOD_ALERT_CHANNEL_DESCRIPTION
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Tạo PendingIntent để mở MainActivity
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        FLOOD_ALERT_NOTIFICATION_ID, // Request code
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Xây dựng thông báo
    val builder = NotificationCompat.Builder(context, FLOOD_ALERT_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // Icon của ứng dụng
        .setContentTitle(FLOOD_ALERT_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true) // Đóng thông báo khi người dùng nhấn vào
        .setContentIntent(pendingIntent) // Gắn PendingIntent vào thông báo

    // Hiển thị thông báo
    with(NotificationManagerCompat.from(context)) {
        notify(FLOOD_ALERT_NOTIFICATION_ID, builder.build())
    }
}