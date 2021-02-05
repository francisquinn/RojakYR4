package com.example.tactalk

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.squti.androidwaverecorder.WaveRecorder
import java.io.File

class RecordingPageFragment : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {

        val fileName =  File("/audioFile.wav")

        val filePath:String = externalCacheDir?.absolutePath + fileName
        val waveRecorder = WaveRecorder(filePath)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recording_page)

        val stopButton : Button = findViewById(R.id.endHalf)

        waveRecorder.startRecording()
        Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()

        stopButton.setOnClickListener{
            waveRecorder.stopRecording()
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()
           

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Loggerx", "ondestroy called")

    }

    /*override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recording_page, container, false)

        view.Stats.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.RecordToStat)}

        view.Stop.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.RecordToEnd)}



        return view
    }*/


}