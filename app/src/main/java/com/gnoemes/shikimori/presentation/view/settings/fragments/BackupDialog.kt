package com.gnoemes.shikimori.presentation.view.settings.fragments

import android.Manifest
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.fileChooser
import com.crashlytics.android.Crashlytics
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.entity.app.domain.ThemeExtras
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.network.MapDeserializerDoubleAsIntFix
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.dialog_backup.*
import kotlinx.android.synthetic.main.dialog_base_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_backup_save.view.*
import org.joda.time.DateTime
import org.joda.time.Days
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class BackupDialog : BaseBottomSheetDialogFragment() {

    private val firebase by lazy { FirebaseStorage.getInstance() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = context.dimenAttr(android.R.attr.actionBarSize)

        checkCloudBackup(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(toolbar) {
            setTitle(R.string.settings_backup_title)
            addBackButton(R.drawable.ic_close) { dismiss() }
        }

        saveLayout.cloudBtn.onClick {
            val userId = getUserId(it.context)
            if (userId == Constants.NO_ID) Toast.makeText(it.context, R.string.common_need_auth, Toast.LENGTH_LONG).show()
            else uploadToCloud(userId, createBackupPrivate())
        }
        saveLayout.deviceBtn.onClick { writeFileToPublic(createBackupPrivate()) }
        saveLayout.shareBtn.onClick { shareBackup() }
        downloadLayout.deviceBtn.onClick { findBackupLocal() }
        downloadLayout.cloudBtn.onClick {
            val userId = getUserId(it.context)
            if (userId == Constants.NO_ID) Toast.makeText(it.context, R.string.common_need_auth, Toast.LENGTH_LONG).show()
            else downloadFromCloud(userId)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.dialog_backup

    ///////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////

    private fun createBackupPrivate(): File {
        val prefs = context!!.getDefaultSharedPreferences().all
                .plus(context!!.getThemeSharedPreferences().all)

        val ignore = ignoreFields
        val items = mutableMapOf<String, Any?>()
        prefs.forEach { entry ->
            if (!ignore.contains(entry.key)) items[entry.key] = entry.value
        }
        val file = File(context!!.filesDir, Constants.BACKUP_FILE_NAME)

        try {
            if (!file.exists()) file.createNewFile()
            val writer = FileWriter(file.absoluteFile)
            BufferedWriter(writer).run {
                write(Gson().toJson(items))
                close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    private fun writeFileToPublic(file: File) {
        checkStoragePermissions {
            try {
                val androidDownloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                val path = context!!.getDefaultSharedPreferences().getString(SettingsExtras.DOWNLOAD_FOLDER, androidDownloadFolder)
                file.copyTo(File(path, Constants.BACKUP_FILE_NAME), overwrite = true)
                Toast.makeText(context, R.string.backup_saved, Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Crashlytics.logException(e)
                Toast.makeText(context, R.string.backup_write_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadToCloud(userId: Long, file: File) {
        firebase.reference.child("backups/$userId/${Constants.BACKUP_FILE_NAME}").putFile(Uri.fromFile(file))
                .addOnFailureListener {
                    Toast.makeText(context, R.string.backup_cloud_upload_error, Toast.LENGTH_LONG).show()
                    it.printStackTrace()
                }
                .addOnSuccessListener {
                    Toast.makeText(context, R.string.backup_cloud_upload_success, Toast.LENGTH_LONG).show()
                    val dateMills = it.metadata?.creationTimeMillis
                    if (dateMills != null) {
                        val dateTime = DateTime(dateMills)
                        setCloudBackupDate(dateTime)
                    }
                }
    }

    private fun downloadFromCloud(userId: Long) {
        val tempFile = File(context!!.filesDir, "cloud-" + Constants.BACKUP_FILE_NAME)
        firebase.reference.child("backups/$userId/${Constants.BACKUP_FILE_NAME}")
                .getFile(tempFile)
                .addOnSuccessListener { readBackup(tempFile) }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(context, R.string.backup_cloud_download_error, Toast.LENGTH_LONG).show()
                }
    }

    private fun checkCloudBackup(context: Context) {
        val userId = getUserId(context)
        if (userId == Constants.NO_ID) return

        firebase.reference.child("backups/$userId/${Constants.BACKUP_FILE_NAME}")
                .metadata
                .addOnSuccessListener {
                    val dateMills = it?.creationTimeMillis
                    if (dateMills != null) {
                        val dateTime = DateTime(dateMills)
                        setCloudBackupDate(dateTime)
                    }
                }
                .addOnFailureListener { it.printStackTrace() }
    }

    private fun findBackupLocal() {
        checkStoragePermissions {
            try {
                val filePart = "/${Constants.BACKUP_FILE_NAME}"
                val androidDownloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + filePart
                val appFolder = context!!.getDefaultSharedPreferences().getString(SettingsExtras.DOWNLOAD_FOLDER, "")?.let {
                    if (it.isNotEmpty()) it + filePart
                    else it
                }

                val downloadsFile = File(androidDownloadFolder)
                val folderFile = File(appFolder)

                val read: (File?) -> Unit = { readBackup(it) }

                if (downloadsFile.exists()) {
                    context?.fileFoundDialog({ read.invoke(downloadsFile) }) { showFolderChooserDialog(read) }
                } else if (!appFolder.isNullOrBlank() && folderFile.exists()) {
                    context?.fileFoundDialog({ read.invoke(folderFile) }) { showFolderChooserDialog(read) }
                } else {
                    showFolderChooserDialog(read)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Crashlytics.logException(e)
                Toast.makeText(context, R.string.backup_read_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun readBackup(file: File?) {
        if (file == null || !file.exists()) {
            Toast.makeText(context, R.string.backup_read_error, Toast.LENGTH_LONG).show()
            return
        }

        try {
            val typeToken = object : TypeToken<Map<String, Any?>>() {}.type

            val gson = GsonBuilder()
                    .registerTypeAdapter(typeToken, MapDeserializerDoubleAsIntFix())
                    .create()

            val json = String(file.readBytes())
            val prefs = context?.getDefaultSharedPreferences()
            val themePrefs = context?.getThemeSharedPreferences()

            var result = 0

            (gson.fromJson(json, typeToken) as? Map<String, Any?>)?.forEach { entry ->
                val field = allFields[entry.key]
                if (field != null) {
                    (if (themeFields.contains(entry.key)) themePrefs else prefs)?.putSetting(entry.key, getData(field, entry.value))
                    result++
                }
            }

            val text = if (result == 0) R.string.backup_empty_file else R.string.backup_read_success
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Crashlytics.logException(e)
            Toast.makeText(context, R.string.backup_read_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun shareBackup() {
        try {
            context?.shareFile(createBackupPrivate().absolutePath)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Crashlytics.logException(e)
            Toast.makeText(context, R.string.backup_write_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun Context.fileFoundDialog(onAccepted: () -> Unit, onCancel: () -> Unit) {
        MaterialDialog(this).show {
            title(res = R.string.backup_found_title)
            message(res = R.string.backup_found_message)
            positiveButton(res = R.string.common_apply) { onAccepted.invoke() }
            negativeButton(res = R.string.filter_select) { onCancel.invoke() }
        }
    }

    private fun checkStoragePermissions(onAccepted: () -> Unit) {
        KotlinPermissions.with(activity!!)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onAccepted { onAccepted.invoke() }
                .ask()
    }

    private fun showFolderChooserDialog(onFileChosen: (File) -> Unit) {
        MaterialDialog(context!!).show {
            fileChooser(
                    allowFolderCreation = false,
                    emptyTextRes = R.string.download_folder_empty)
            { _, file -> onFileChosen.invoke(file) }
        }
    }

    private fun getData(field: Any, value: Any?): Any? =
            when {
                field is Int && value is Long -> value.toInt()
                field is Long -> value as? Long
                field is Boolean -> value as? Boolean
                field is String -> value as? String
                else -> value
            }

    private fun SharedPreferences.putSetting(key: String, data: Any?) {
        when (data) {
            is Long -> putLong(key, data)
            is String -> putString(key, data)
            is Int -> putInt(key, data)
            is Boolean -> putBoolean(key, data)
            is Float -> putFloat(key, data)
        }
    }

    private fun Context.shareFile(path: String?) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"

        val media = File(path)
        val uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", media)

        share.putExtra(Intent.EXTRA_STREAM, uri)
        share.clipData = ClipData.newRawUri("", uri)
        share.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION


        val shareChooser = Intent.createChooser(share, getString(R.string.common_share))
        shareChooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION

        startActivity(shareChooser)
    }

    private fun setCloudBackupDate(date: DateTime) {
        val days = Math.abs(Days.daysBetween(DateTime.now(), date).days)
        val text = if (days == 0) context!!.getString(R.string.common_today) else
            context!!.resources.getQuantityString(R.plurals.days_ago, days, days)
        downloadLayout.cloudLabel.text = text
    }

    private fun getUserId(context: Context): Long {
        return context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)?.getLong(SettingsExtras.USER_ID, Constants.NO_ID)
                ?: Constants.NO_ID
    }

    //TODO reflection?
    private val allFields: Map<String, Any> by lazy {
        mapOf(
                ThemeExtras.THEME_KEY to 0,
                ThemeExtras.ASCENT_KEY to 0,
                ThemeExtras.NIGHT_THEME_END_KEY to 0,
                ThemeExtras.NIGHT_THEME_START_KEY to 0,
                ThemeExtras.NIGHT_THEME_KEY to 0,
                SettingsExtras.IS_ROMADZI_NAMING to true,
                SettingsExtras.DOWNLOAD_FOLDER to "",
                SettingsExtras.IS_AUTO_INCREMENT to true,
                SettingsExtras.IS_AUTO_STATUS to true,
                SettingsExtras.TRANSLATION_TYPE to "",
                SettingsExtras.PLAYER_TYPE to "",
                SettingsExtras.IS_REMEMBER_PLAYER to true,
                SettingsExtras.IS_USE_LOCAL_TRANSLATION_SETTINGS to true,
                SettingsExtras.IS_BEST_EXTERNAL_QUALITY to true,
                SettingsExtras.RATE_SWIPE_TO_LEFT_ACTION to "",
                SettingsExtras.RATE_SWIPE_TO_RIGHT_ACTION to "",
                SettingsExtras.IS_NOTIFICATIONS_ENABLED to true,
                SettingsExtras.PLAYER_IS_GESTURES_ENABLED to true,
                SettingsExtras.PLAYER_IS_VOLUME_BRIGHTNESS_GESTURES_ENABLED to true,
                SettingsExtras.PLAYER_IS_VOLUME_AND_BRIGHTNESS_INVERTED to true,
                SettingsExtras.PLAYER_IS_FORWARD_REWIND_SLIDE to true,
                SettingsExtras.PLAYER_IS_OPEN_LANDSCAPE to true,
                SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET to 0L,
                SettingsExtras.PLAYER_FORWARD_REWIND_OFFSET_BIG to 0L,
                SettingsExtras.PLAYER_IS_ZOOM_PROPORTIONAL to true,
                SettingsExtras.PLAYER_IS_AUTO_PIP to true,
                SettingsExtras.CHRONOLOGY_TYPE to 0,
                SettingsExtras.SORT_KEY_PART + "_${Type.ANIME.name}" to 0,
                SettingsExtras.SORT_KEY_PART + "_${Type.MANGA.name}" to 0,
                SettingsExtras.ORDER_KEY_PART + "_${Type.ANIME.name}" to true,
                SettingsExtras.ORDER_KEY_PART + "_${Type.MANGA.name}" to true
        )
    }

    private val ignoreFields: Array<String> by lazy {
        arrayOf(
                SettingsExtras.USER_BRIEF,
                SettingsExtras.USER_STATUS,
                SettingsExtras.USER_ID,
                SettingsExtras.BACKUP_SETTINGS,
                SettingsExtras.DONATION_LINK,
                SettingsExtras.NEW_VERSION_AVAILABLE
        )
    }

    private val themeFields: Array<String> by lazy {
        arrayOf(
                ThemeExtras.THEME_KEY,
                ThemeExtras.ASCENT_KEY,
                ThemeExtras.NIGHT_THEME_END_KEY,
                ThemeExtras.NIGHT_THEME_START_KEY,
                ThemeExtras.NIGHT_THEME_KEY
        )
    }
}