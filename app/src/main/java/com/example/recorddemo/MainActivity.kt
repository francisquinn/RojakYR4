package com.example.recorddemo

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.recorddemo.R
import com.github.squti.androidwaverecorder.WaveRecorder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.visualizer.amplitude.AudioRecordView
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    lateinit var storage: FirebaseStorage
    private lateinit var audioRecordView: AudioRecordView

    override fun onCreate(savedInstanceState: Bundle?) {

        // Google Cloud Storage Bucket
        storage = Firebase.storage("gs://tactalk-bucket")

        // Points to the root reference
        val storageRef = storage.reference

        val timer = Timer()
        var num = 0

        var fileName = "/audioFile$num.wav"
        var filePath: String = externalCacheDir?.absolutePath + fileName
        var waveRecorder = WaveRecorder(filePath)
        waveRecorder.waveConfig.sampleRate = 48000

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startButton: Button = findViewById(R.id.button_start_recording)
        val stopButton: Button = findViewById(R.id.button_stop_recording)

        audioRecordView = findViewById(R.id.audioRecordView)



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



                val waveTimer = Timer()
                waveTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        //val currentMaxAmplitude : Int = waveRecorder.onAmplitudeListener.toString()
                        //audioRecordView.update(currentMaxAmplitude); //redraw view
                        //Log.d("Recorder", waveRecorder.onAmplitudeListener.toString())
                        waveRecorder.onAmplitudeListener = {
                            Log.d("Recorder", "Amplitude : $it")
                            audioRecordView.update(it);
                        }
                    }
                }, 0, 100)

                timer.scheduleAtFixedRate(timerTask {
                    //waveRecorder.startRecording()
                    Log.d("Recorder", "Recording stopped")
                    waveRecorder.stopRecording()
                    println("StopRecording")

                    cloudUploader(filePath, fileName, storageRef)

                    num++
                    fileName = "/audioFile$num.wav"
                    filePath = externalCacheDir?.absolutePath + fileName
                    waveRecorder = WaveRecorder(filePath)
                }, 10000, 10000)

                timer.scheduleAtFixedRate(timerTask {
                    waveRecorder.startRecording()
                    Log.d("Recorder", "Recording started")
                }, 1, 10000)

                Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
            }
        }
        stopButton.setOnClickListener {
            timer.cancel()
            timer.purge()
            waveRecorder.stopRecording()
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()
            Log.d("Recorder", "Recording stopped")

            cloudUploader(filePath, fileName, storageRef)
        }
    }

    private fun cloudUploader(filePath:String, fileName:String, storageRef:StorageReference){
        // Retrieve the file from the filePath
        val file = Uri.fromFile(File(filePath))

        // Type of metadata
        val metadata = storageMetadata {
            contentType = "audio/wav"
        }

        Log.d("Recorder", "Uploading to the cloud...")

        // Upload to the Bucket
        val uploadTask = storageRef.child(fileName).putFile(file, metadata)

        // Listen for state changes, errors, and completion of the upload.
        // You'll need to import com.google.firebase.storage.ktx.component1 and
        // com.google.firebase.storage.ktx.component2
        uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
            val progress = (100.0 * bytesTransferred) / totalByteCount
            Log.d("Recorder", "Upload is $progress% done")
        }.addOnPausedListener {
            Log.d("Recorder", "Upload is paused")
        }.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {

        }
    }


}