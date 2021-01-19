package com.example.recorddemo

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.squti.androidwaverecorder.WaveRecorder
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val filePath:String = externalCacheDir?.absolutePath + "/audioFile.wav"

        val waveRecorder = WaveRecorder(filePath)
        waveRecorder.waveConfig.sampleRate = 44100
        waveRecorder.noiseSuppressorActive = true

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
                waveRecorder.startRecording()
                Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
            }
        }

        stopButton.setOnClickListener{
            waveRecorder.stopRecording()
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()
        }


    }




}