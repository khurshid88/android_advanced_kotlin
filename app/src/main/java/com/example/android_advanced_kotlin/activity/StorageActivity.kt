package com.example.android_advanced_kotlin.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.utils.Utils
import java.io.*
import java.nio.charset.Charset

const val REQUEST_CODE = 1001

class StorageActivity : AppCompatActivity() {
    var isPersistent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)
        initViews()
        requestPermissions()
    }

    private fun hasReadPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun hasWritePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if (!hasReadPermission()) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!hasWritePermission()) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "${permissions[i]} granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun initViews() {
        val b_save_int = findViewById<Button>(R.id.b_save_int)
        val b_read_int = findViewById<Button>(R.id.b_read_int)
        val b_delete_int = findViewById<Button>(R.id.b_delete_int)
        val b_save_ext = findViewById<Button>(R.id.b_save_ext)
        val b_read_ext = findViewById<Button>(R.id.b_read_ext)
        val b_delete_ext = findViewById<Button>(R.id.b_delete_ext)
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

        //checkStoragePaths()
        //createInternalFile()
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

        Log.d("@@@",file.absolutePath)

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