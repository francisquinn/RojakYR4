package com.example.tactalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tactalk.statistics.StatisticFragment
import com.example.tactalk.statistics.StatisticViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.File

class RecordingPageFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        val fileName = File("/audioFile.wav")

        val filePath: String = externalCacheDir?.absolutePath + fileName
        val waveRecorder = WaveRecorder(filePath)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recording_page)

        val stopButton: Button = findViewById(R.id.pause)

        //Button to go to stats Page
        stats()

        waveRecorder.startRecording()
        Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()

        stopButton.setOnClickListener {
            waveRecorder.stopRecording()
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()


        }


        timer()

    }

    //function that starts the chronometer
    fun timer() {

        //Chronometer for timer
        val meter = findViewById<Chronometer>(R.id.chronometertest2)

        //access the button using id
        val btn = findViewById<Button>(R.id.pause)
        //set to true so it starts on page load
        var isWorking = true
        meter.start()
        btn?.setOnClickListener(object : View.OnClickListener {


            override fun onClick(v: View) {
                if (!isWorking) {
                    meter.start()
                    isWorking = true
                } else {
                    meter.stop()
                    isWorking = false
                }

                btn.setText(if (isWorking) R.string.Pause else R.string.Start)

                Toast.makeText(
                    this@RecordingPageFragment, getString(
                        if (isWorking)
                            R.string.working
                        else
                            R.string.stopped
                    ),
                    Toast.LENGTH_SHORT
                ).show()


            }

        })
    }

    fun stats() {
        val tes: Button = findViewById(R.id.endHalf)
        tes.setOnClickListener {
            startActivity(Intent(this, StatisticFragment::class.java))
        }
    }


}