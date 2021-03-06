package com.example.tactalk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StatsFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_stats)

        val secondHalf : Button = findViewById(R.id.second_half)
        val endGame : Button = findViewById(R.id.end_game)

        val statPage = intent.getStringExtra("statPage")

        if (statPage != null) {

            if (statPage == "half"){
                secondHalf.visibility = View.VISIBLE
                endGame.visibility = View.GONE

                secondHalf.setOnClickListener {
                    Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show()
                    val timerVal = 2100000
                    val intent = Intent(this, RecordingPageFragment::class.java)
                    intent.putExtra("timerVal", timerVal)
                    startActivity(intent)
                    finish()
                }

            } else if (statPage == "full"){
                secondHalf.visibility = View.GONE
                endGame.visibility = View.VISIBLE

                endGame.setOnClickListener {
                    startActivity(Intent(this, EndGameFragment::class.java))
                }

            }
            //Log.i("statPage", statPage)
        }




    }

    override fun onBackPressed() {}

}