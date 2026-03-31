package com.privatenotifications

import android.app.Notification
import android.service.notification.StatusBarNotification

/**
 * Data class to store hidden notification information
 * We store this so we can restore notifications when phone is flipped face-up
 */
data class NotificationData(
    val key: String,
    val packageName: String,
    val notification: Notification,
    val postTime: Long,
    val id: Int,
    val tag: String?
) {
    companion object {
        /**
         * Creates NotificationData from StatusBarNotification
         */
        fun from(sbn: StatusBarNotification): NotificationData {
            return NotificationData(
                key = sbn.key,
                packageName = sbn.packageName,
                notification = sbn.notification,
                postTime = sbn.postTime,
                id = sbn.id,
                tag = sbn.tag
            )
        }
    }
}
