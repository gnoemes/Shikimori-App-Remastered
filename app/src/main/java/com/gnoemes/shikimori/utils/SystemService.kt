package com.gnoemes.shikimori.utils

import android.app.AlarmManager
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.hardware.SensorManager
import android.media.AudioManager
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager

fun Context.alarmManager(): AlarmManager =
        getSystemService(Context.ALARM_SERVICE) as AlarmManager

fun Context.audioManager(): AudioManager =
        getSystemService(Context.AUDIO_SERVICE) as AudioManager

fun Context.clipboardManager(): ClipboardManager =
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

fun Context.downloadManager(): DownloadManager? =
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

fun Context.inputMethodManager(): InputMethodManager? =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

fun Context.layoutInflater(): LayoutInflater =
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

fun Context.notificationManager(): NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

fun Context.sensorManager(): SensorManager =
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
