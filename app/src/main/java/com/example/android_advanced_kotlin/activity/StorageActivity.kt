package com.example.android_advanced_kotlin.activity

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.utils.Utils
import com.example.android_advanced_kotlin.activity.utils.Utils.fireToast
import java.io.*
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class StorageActivity : AppCompatActivity() {
    val isInternal = true
    var isPersistent = true
    var readPermissionGranted = false
    var writePermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)
        initViews()
        requestPermissions()
    }

    private fun initViews() {
        val b_save_int = findViewById<Button>(R.id.b_save_int)
        val b_read_int = findViewById<Button>(R.id.b_read_int)
        val b_delete_int = findViewById<Button>(R.id.b_delete_int)
        val b_save_ext = findViewById<Button>(R.id.b_save_ext)
        val b_read_ext = findViewById<Button>(R.id.b_read_ext)
        val b_delete_ext = findViewById<Button>(R.id.b_delete_ext)
        val b_camera = findViewById<Button>(R.id.b_camera)
        b_camera.setOnClickListener {
            takePhoto.launch()
        }
        b_save_int.setOnClickListener { saveInternalFile("PDP Internal") }
        b_read_int.setOnClickListener { readInternalFile() }
        b_delete_int.setOnClickListener { deleteInternalFile() }
        b_save_ext.setOnClickListener {
            saveExternalFile("PDP External")
        }
        b_read_ext.setOnClickListener {
            readExternalFile()
        }
        b_delete_ext.setOnClickListener { deleteExternalFile() }

        checkStoragePaths()
        //createInternalFile()
    }

    private fun requestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionsToRequest = mutableListOf<String>()
        if (!readPermissionGranted)
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!writePermissionGranted)
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionsToRequest.isNotEmpty())
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted =
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
            writePermissionGranted =
                permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted

            if (readPermissionGranted) fireToast(this, "READ_EXTERNAL_STORAGE")
            if (writePermissionGranted) fireToast(this, "WRITE_EXTERNAL_STORAGE")
        }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->

            val filename = UUID.randomUUID().toString()

            val isPhotoSaved = if (isInternal) {
                savePhotoToInternalStorage(filename, bitmap!!)
            } else {
                if (writePermissionGranted) {
                    savePhotoToExternalStorage(filename, bitmap!!)
                } else {
                    false
                }
            }
            if (isPhotoSaved) {
                fireToast(this, "Photo saved successfully")
            } else {
                fireToast(this, "Failed to save photo")
            }
        }

    private fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
        return try {
            openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Couldn't save bitmap.")
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun savePhotoToExternalStorage(filename: String, bmp: Bitmap): Boolean {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$filename.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }
        return try {
            contentResolver.insert(collection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }


    fun loadPhotosFromExternalStorage(): List<Uri> {

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
        )
        val photos = mutableListOf<Uri>()
        return contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val width = cursor.getInt(widthColumn)
                val height = cursor.getInt(heightColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                photos.add(contentUri)
            }
            photos.toList()
        } ?: listOf()
    }

    fun loadPhotosFromInternalStorage(): List<Bitmap> {
        val files = filesDir.listFiles()
        return files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }?.map {
            val bytes = it.readBytes()
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bmp
        } ?: listOf()
    }

    //Internal & External Paths
    fun checkStoragePaths() {
        val internal_m1 = getDir("custom", 0)
        val internal_m2 = filesDir

        val external_m1 = getExternalFilesDir(null)
        val external_m2 = externalCacheDir
        val external_m3 = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        Log.d("StorageActivity ", internal_m1.absolutePath)
        Log.d("StorageActivity ", internal_m2.absolutePath)
        Log.d("StorageActivity", external_m1!!.absolutePath)
        Log.d("StorageActivity ", external_m2!!.absolutePath)
        Log.d("StorageActivity ", external_m3!!.absolutePath)
    }

    //Internal Storages

    private fun createInternalFile() {
        val fileName = "pdp_internal.txt"
        val file: File
        file = if (isPersistent) {
            File(filesDir, fileName)
        } else {
            File(cacheDir, fileName)
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
                Utils.fireToast(
                    this, String.format
                        ("File %s has been created", fileName)
                )
            } catch (e: IOException) {
                Utils.fireToast(
                    this, String.format
                        ("File %s creation failed", fileName)
                )
            }
        } else {
            Utils.fireToast(
                this, String.format
                    ("File %s already exists", fileName)
            )
        }
    }

    private fun saveInternalFile(data: String) {
        val fileName = "pdp_internal.txt"
        try {
            val fileOutputStream: FileOutputStream
            fileOutputStream = if (isPersistent) {
                openFileOutput(fileName, MODE_PRIVATE)
            } else {
                val file = File(cacheDir, fileName)
                FileOutputStream(file)
            }
            fileOutputStream.write(data.toByteArray(Charset.forName("UTF-8")))
            Utils.fireToast(
                this, String.format
                    ("Write to %s successful", fileName)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Utils.fireToast(
                this, String.format
                    ("Write to file %s failed", fileName)
            )
        }
    }

    private fun readInternalFile() {
        val fileName = "pdp_internal.txt"
        try {
            val fileInputStream: FileInputStream
            fileInputStream = if (isPersistent) {
                openFileInput(fileName)
            } else {
                val file = File(cacheDir, fileName)
                FileInputStream(file)
            }
            val inputStreamReader = InputStreamReader(
                fileInputStream,
                Charset.forName("UTF-8")
            )
            val lines: MutableList<String?> = ArrayList()
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            val readText = TextUtils.join("\n", lines)
            Utils.fireToast(
                this, String.format
                    ("Read from file %s successful", fileName)
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Utils.fireToast(
                this, String.format
                    ("Read from file %s failed", fileName)
            )
        }
    }

    private fun deleteInternalFile() {
        val fileName = "pdp_internal.txt"
        val file: File
        file = if (isPersistent) {
            File(filesDir, fileName)
        } else {
            File(cacheDir, fileName)
        }
        if (file.exists()) {
            file.delete()
            Utils.fireToast(
                this, String.format
                    ("File %s has been deleted", fileName)
            )
        } else {
            Utils.fireToast(
                this, String.format
                    ("File %s doesn't exist", fileName)
            )
        }
    }

    //External Storages

    private fun createExternalFile() {
        val fileName = "pdp_external.txt"
        val file: File
        file = if (isPersistent) {
            File(getExternalFilesDir(null), fileName)
        } else {
            File(externalCacheDir, fileName)
        }
        Log.d("@@@", "absolutePath: " + file.absolutePath)
        if (!file.exists()) {
            try {
                file.createNewFile()
                Utils.fireToast(
                    this, String.format
                        ("File %s has been created", fileName)
                )
            } catch (e: IOException) {
                Utils.fireToast(
                    this, String.format
                        ("File %s creation failed", fileName)
                )
            }
        } else {
            Utils.fireToast(
                this, String.format
                    ("File %s already exists", fileName)
            )
        }
    }

    private fun saveExternalFile(data: String) {
        val fileName = "pdp_external.txt"

        val file: File
        file = if (isPersistent) {
            File(getExternalFilesDir(null), fileName)
        } else {
            File(externalCacheDir, fileName)
        }
        Log.d("@@@", "absolutePath: " + file.absolutePath)
        try {
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray(Charset.forName("UTF-8")))
            Utils.fireToast(
                this, String.format
                    ("Write to %s successful", fileName)
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Utils.fireToast(
                this, String.format
                    ("Write to file %s failed", fileName)
            )
        }
    }

    private fun readExternalFile() {
        val fileName = "pdp_external.txt"
        val file: File
        file = if (isPersistent)
            File(getExternalFilesDir(null), fileName)
        else
            File(externalCacheDir, fileName)

        Log.d("@@@", "absolutePath: " + file.absolutePath)

        try {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream, Charset.forName("UTF-8"))
            val lines: MutableList<String?> = java.util.ArrayList()
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            val readText = TextUtils.join("\n", lines)
            Log.d("StorageActivity", readText)
            Utils.fireToast(this, String.format("Read from file %s successful", fileName))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Utils.fireToast(this, String.format("Read from file %s failed", fileName))
        }
    }

    private fun deleteExternalFile() {
        val fileName = "pdp_external.txt"
        val file: File
        file = if (isPersistent) {
            File(getExternalFilesDir(null), fileName)
        } else {
            File(externalCacheDir, fileName)
        }
        if (file.exists()) {
            file.delete()
            Utils.fireToast(
                this, String.format
                    ("File %s has been deleted", fileName)
            )
        } else {
            Utils.fireToast(
                this, String.format
                    ("File %s doesn't exist", fileName)
            )
        }
    }

}
