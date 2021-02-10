package com.example.tactalk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tactalk.activity.LoginFragment
import com.example.tactalk.activity.RegisterFragment
import com.example.tactalk.overview.OverviewFragment
import com.example.tactalk.overview.OverviewViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.edt_email
import kotlinx.android.synthetic.main.fragment_register.edt_password
import java.io.File

class RecordingPageFragment : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {



        val fileName =  File("/audioFile.wav")

        val filePath:String = externalCacheDir?.absolutePath + fileName
        val waveRecorder = WaveRecorder(filePath)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recording_page)

        val stopButton : Button = findViewById(R.id.pause)

        waveRecorder.startRecording()
        Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()

        stopButton.setOnClickListener{
            waveRecorder.stopRecording()
            Toast.makeText(this, "Recording stopped!", Toast.LENGTH_SHORT).show()



        }
        val over : Button = findViewById(R.id.endHalf)
        over.setOnClickListener{
            startActivity(Intent(this, OverviewFragment::class.java))
        }

        timer()

    }

    fun timer(){

        //Chronometer for timer
        val meter = findViewById<Chronometer>(R.id.chronometertest2)

        //access the button using id
        val btn = findViewById<Button>(R.id.pause)

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

                Toast.makeText(this@RecordingPageFragment, getString(
                    if (isWorking)
                        R.string.working
                    else
                        R.string.stopped),
                    Toast.LENGTH_SHORT).show()


            }

        })
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