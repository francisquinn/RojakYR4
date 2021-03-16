package com.example.tactalk.recording

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tactalk.R
import com.example.tactalk.databinding.FragmentRecordingPageBinding
import com.example.tactalk.statistics.StatisticFragment
import com.github.squti.androidwaverecorder.WaveRecorder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.visualizer.amplitude.AudioRecordView
import java.io.File
import java.util.*
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import kotlin.concurrent.timerTask

class RecordingPageFragment : AppCompatActivity() {

    lateinit var storage: FirebaseStorage
    private lateinit var audioRecordView: AudioRecordView

    private val recordViewModel: RecordingViewModel by lazy {
        ViewModelProvider(this).get(RecordingViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        var baseSeconds = 0;
        var secondaryBaseSeconds = 13;

        // Google Cloud Storage Bucket
        storage = Firebase.storage("gs://tactalk-bucket")


        // Points to the root reference
        val storageRef = storage.reference

        // 15 second audio upload timer
        val recordingTimer = Timer()

        val secondaryRecordingTimer = Timer()

        // file order number
        var num = 1

        // test file name with hardcoded game_id
        var fileName = "/game_60084b37e8c56c0978f5b004_"+num+"_0.wav"
        var secondaryFileName = "/game_60084b37e8c56c0978f5b004_"+num+"_1.wav"

        // cache path & set up recorder
        var filePath: String = externalCacheDir?.absolutePath + fileName
        var waveRecorder = WaveRecorder(filePath)
        waveRecorder.waveConfig.sampleRate = 32000

        //secondary WaveRecorder
        var secondaryWaveRecorder = WaveRecorder(filePath)
        secondaryWaveRecorder.waveConfig.sampleRate = 32000

        super.onCreate(savedInstanceState)

        val binding: FragmentRecordingPageBinding = DataBindingUtil.setContentView(this, R.layout.fragment_recording_page)
        binding.lifecycleOwner = this
        binding.recordViewModel = recordViewModel

        // Match timer
        // Retrieve value from Intent
        val timerVal = intent.getIntExtra("timerVal", 0)

        val clock: Chronometer = findViewById(R.id.match_time)
        clock.typeface = ResourcesCompat.getFont(this, R.font.orbitron_medium)
        clock.base = SystemClock.elapsedRealtime() - timerVal
        clock.start()

        val stopButton: Button = findViewById(R.id.endHalf)
        val pauseButton: Button = findViewById(R.id.pause)
        audioRecordView = findViewById(R.id.audioRecordView)

        // Wave animation
        val waveTimer = Timer()
        waveTimer.schedule(object : TimerTask() {
            override fun run() {
                waveRecorder.onAmplitudeListener = {
                    audioRecordView.update(it);
                }
            }
        }, 0, 100)

        // Stop the recorder at 15 seconds, upload the file from cache,
        // then start the recorder again
        recordingTimer.scheduleAtFixedRate(timerTask {
            Log.d("Recorder", "Recording stopped")
            waveRecorder.stopRecording()

            cloudUploader(filePath, fileName, storageRef)

            deleteExternalStorage(fileName)

            getScore()

            num++
            fileName = "/game_60084b37e8c56c0978f5b004_"+num+"_"+baseSeconds+".wav"
            filePath = externalCacheDir?.absolutePath + fileName
            waveRecorder = WaveRecorder(filePath)
            waveRecorder.waveConfig.sampleRate = 32000
            baseSeconds += 15;
        }, 30000, 15000)

        // start recorded once the activity is created
        recordingTimer.scheduleAtFixedRate(timerTask {
            waveRecorder.startRecording()
            Log.d("Recorder", "Recording started")
        }, 1, 15000)





        //setup the secondary recorder
        secondaryRecordingTimer.scheduleAtFixedRate(timerTask {
                //anything you want to start after 3s

                Log.d("Recorder", "Recording stopped")
                secondaryWaveRecorder.stopRecording()


                cloudUploader(filePath, secondaryFileName, storageRef)

                deleteExternalStorage(secondaryFileName)

                getScore()

                num++
                secondaryFileName = "/game_60084b37e8c56c0978f5b004_"+num+"_"+secondaryBaseSeconds+".wav"
                filePath = externalCacheDir?.absolutePath + secondaryFileName
                secondaryWaveRecorder = WaveRecorder(filePath)
                secondaryWaveRecorder.waveConfig.sampleRate = 32000
                secondaryBaseSeconds += 15;
        }, 28001, 15000)

        secondaryRecordingTimer.scheduleAtFixedRate(timerTask {
            secondaryWaveRecorder.startRecording()
            Log.d("Recorder", "Recording started")
        }, 13001, 15000)




        stopButton.setOnClickListener {
            recordingTimer.cancel()
            recordingTimer.purge()
            secondaryRecordingTimer.cancel()
            secondaryRecordingTimer.purge()
            waveRecorder.stopRecording()
            secondaryWaveRecorder.stopRecording()
            clock.stop()

            cloudUploader(filePath, fileName, storageRef)

            deleteExternalStorage(fileName)

            getScore()

            //Log.i("timerVAL", timerVal.toString())

            if (timerVal > 1800000) {
                val statPage = "full"
                val intent = Intent(this, StatisticFragment::class.java)
                intent.putExtra("statPage", statPage)
                startActivity(intent)
                finish()
            } else {
                val statPage = "half"
                val intent = Intent(this, StatisticFragment::class.java)
                intent.putExtra("statPage", statPage)
                startActivity(intent)
                finish()
            }

        }

        var buttonState = true
        pauseButton.setOnClickListener {
            if (buttonState) {
                pauseButton.setText(R.string.Start)
                buttonState = !buttonState
                waveRecorder.stopRecording()
                Log.d("Recorder", "Recording paused")
            } else {
                pauseButton.setText(R.string.Pause)
                buttonState = !buttonState
                waveRecorder.startRecording()
                Log.d("Recorder", "Recording Started after pause")
            }
        }
    }

    // function to upload audio file to the cloud
    private fun cloudUploader(filePath: String, fileName: String, storageRef: StorageReference) {
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

    private fun getScore() {

        Log.i("getScore", "calling update game...")

    }

    private fun deleteExternalStorage(fileName: String) {
        val filePath = externalCacheDir?.absolutePath
        try {
            val file = File(filePath, fileName)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            Log.e("CacheDelete", "Exception while deleting file " + e.message)
        }
    }

    // disable back button
    override fun onBackPressed() {}
}