package com.example.tactalk.recording

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private var fRest :Boolean = true;
    private var sRest :Boolean = true;

    private val recordViewModel: RecordingViewModel by lazy {
        ViewModelProvider(this).get(RecordingViewModel::class.java)
    }




    override fun onCreate(savedInstanceState: Bundle?) {

        var baseSeconds = 0;
        var secondaryBaseSeconds = 12;

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
        var fileName = "/game_60084b37e8c56c0978f5b004_"+num+"_0_f.wav"
        var secondaryFileName = "/game_60084b37e8c56c0978f5b004_"+num+"_1_s.wav"

        // cache path & set up recorder
        var filePath: String = externalCacheDir?.absolutePath + fileName
        var secondaryFilePath: String = externalCacheDir?.absolutePath + secondaryFileName

        var waveRecorder = WaveRecorder(filePath)
        waveRecorder.waveConfig.sampleRate = 32000

        //secondary WaveRecorder
        var secondaryWaveRecorder = WaveRecorder(secondaryFilePath)
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
            Log.d("Recorder", "Recording stopped b")
            waveRecorder.stopRecording()
            fRest = true;

            cloudUploader(filePath, fileName, storageRef)

            deleteExternalStorage(fileName)

            getScore()

            num++
            fileName = "/game_60084b37e8c56c0978f5b004_"+num+"_"+baseSeconds+"_f.wav"
            filePath = externalCacheDir?.absolutePath + fileName
            waveRecorder = WaveRecorder(filePath)
            waveRecorder.waveConfig.sampleRate = 32000
            baseSeconds += 14;
        }, 14000, 24000)

        // start recorded once the activity is created
        recordingTimer.scheduleAtFixedRate(timerTask {
            Log.d("Recorder", "Recording started a")
            waveRecorder.startRecording()
            fRest = false;

        }, 1, 24000)




        //setup the secondary recorder
        secondaryRecordingTimer.scheduleAtFixedRate(timerTask {
                //anything you want to start after 3s

                Log.d("Recorder", "Recording stopped d")

                secondaryWaveRecorder.stopRecording()
                sRest = true;

                cloudUploader(secondaryFilePath, secondaryFileName, storageRef)

                deleteExternalStorage(secondaryFileName)

                getScore()

                num++
                secondaryFileName = "/game_60084b37e8c56c0978f5b004_"+num+"_"+secondaryBaseSeconds+"_s.wav"
                secondaryFilePath = externalCacheDir?.absolutePath +secondaryFileName
                secondaryWaveRecorder = WaveRecorder(secondaryFilePath)
                secondaryWaveRecorder.waveConfig.sampleRate = 32000
                secondaryBaseSeconds += 14;
        }, 26000, 24000)

        try {


            secondaryRecordingTimer.scheduleAtFixedRate(timerTask {
                Log.d("Recorder", "Recording started c")
                secondaryWaveRecorder.startRecording()
                sRest = false;

            }, 12001, 24000)

        }catch(ex:Exception)
        {
            Log.d("Recorder", "ERROR:"+ex.toString());
        }


        stopButton.setOnClickListener {
            recordingTimer.cancel()
            recordingTimer.purge()
            secondaryRecordingTimer.cancel()
            secondaryRecordingTimer.purge()
            if (!fRest) {
                waveRecorder.stopRecording()
            }
            if (!sRest) {
                secondaryWaveRecorder.stopRecording()
            }
            clock.stop()

            if (!fRest) {
                cloudUploader(filePath, fileName, storageRef)
            }
            if (!sRest)
            {
                cloudUploader(secondaryFilePath, secondaryFileName, storageRef)
            }

            deleteExternalStorage(fileName)
            deleteExternalStorage(secondaryFileName)

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