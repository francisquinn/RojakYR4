package com.example.recorddemo

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class MainActivity : AppCompatActivity() {


    var recorder: MediaRecorder? = null
    private var fileName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.wav"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton : Button = findViewById(R.id.button_start_recording)
        val stopButton : Button = findViewById(R.id.button_stop_recording)


        startButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                startRecording()
                Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
            }
        }

        stopButton.setOnClickListener{
            stopRecording()
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()
        }

       // button_pause_recording.setOnClickListener {
           // pauseRecording()
        //}
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(96000)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                prepare()
                Log.i("AudioRecordTest", "Recording started")
            } catch (e: IOException) {
                Log.e("AudioRecordTest", "prepare() failed")
            }

            start()
        }
    }



    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()

        }

        Log.i("AudioRecordTest", "Recording stopped!")
        Log.i("AudioRecordTest", "${fileName}")
        recorder = null
    }

}