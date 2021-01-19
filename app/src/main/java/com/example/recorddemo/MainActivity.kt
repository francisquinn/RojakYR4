package com.example.recorddemo

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.squti.androidwaverecorder.WaveRecorder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {

        // Google Cloud Storage Bucket
        storage = Firebase.storage("gs://tactalk-bucket")

        // Points to the root reference
        val storageRef = storage.reference

        // Name of the recorded file
        val fileName = "audioFileFireThree.wav"

        // filePath located in the cache
        val filePath: String = externalCacheDir?.absolutePath + "/audioFile.wav"

        // Record the WAV file to the filePath cache
        val waveRecorder = WaveRecorder(filePath)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.button_start_recording)
        val stopButton: Button = findViewById(R.id.button_stop_recording)

        // Start Button
        startButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                ActivityCompat.requestPermissions(this, permissions, 0)
            } else {
                waveRecorder.startRecording()
                Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
            }
        }

        // Stop Button
        stopButton.setOnClickListener {
            waveRecorder.stopRecording()
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()

            // Retrieve the file from the filePath
            val file = Uri.fromFile(File(filePath))

            // Type of metadata
            val metadata = storageMetadata {
                contentType = "audio/wav"
            }

            // Upload to the Bucket
            val uploadTask = storageRef.child(fileName).putFile(file, metadata)

            // Listen for state changes, errors, and completion of the upload.
            // You'll need to import com.google.firebase.storage.ktx.component1 and
            // com.google.firebase.storage.ktx.component2
            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                val progress = (100.0 * bytesTransferred) / totalByteCount
                Log.d("Uploader", "Upload is $progress% done")
            }.addOnPausedListener {
                Log.d("Uploader", "Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                // Handle successful uploads on complete
                // ...
            }


        }

    }
}