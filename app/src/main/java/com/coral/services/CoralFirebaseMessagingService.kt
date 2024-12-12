package com.coral.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.coral.MainActivity
import com.coral.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class CoralFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "CoralFCMService"
        private const val CHANNEL_ID = "coral_notifications"
        private const val CHANNEL_NAME = "CORAL Updates"
        private const val CHANNEL_DESCRIPTION = "Notifications from CORAL app"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check for data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Check for notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.title, it.body)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        saveTokenToDatabase(token)
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val messageType = data["type"]
        val message = data["message"]

        when (messageType) {
            "activity_update" -> handleActivityUpdate(message)
            "goal_achieved" -> handleGoalAchievement(message)
            else -> Log.d(TAG, "Unknown message type: $messageType")
        }
    }

    private fun handleActivityUpdate(message: String?) {
        message?.let { sendNotification("Activity Update", it) }
    }

    private fun handleGoalAchievement(message: String?) {
        message?.let { sendNotification("Goal Achieved! 🎉", it) }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        if (title == null || messageBody == null) return

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, pendingIntentFlags
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun saveTokenToDatabase(token: String) {
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            FirebaseDatabase.getInstance().reference
                .child("users")
                .child(userId)
                .child("fcmToken")
                .setValue(token)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Token saved successfully")
                    } else {
                        Log.e(TAG, "Failed to save token", task.exception)
                    }
                }
        }
    }
}