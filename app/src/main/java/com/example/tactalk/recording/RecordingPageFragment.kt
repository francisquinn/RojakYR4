package com.example.tactalk.recording

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tactalk.R
import com.example.tactalk.databinding.FragmentRecordingPageBinding
import com.example.tactalk.statistics.StatisticFragment
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_recording_page.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_statistic.*
import java.io.File

class RecordingPageFragment : AppCompatActivity() {

    private val viewModel: RecordingPageViewModel by lazy {
        ViewModelProvider(this).get(RecordingPageViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        val fileName = File("/audioFile.wav")

        val filePath: String = externalCacheDir?.absolutePath + fileName
        val waveRecorder = WaveRecorder(filePath)


        super.onCreate(savedInstanceState)

        val binding: FragmentRecordingPageBinding = DataBindingUtil.setContentView(this, R.layout.fragment_recording_page)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel



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