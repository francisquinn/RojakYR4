package com.example.tactalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class SetUpMatchFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_set_up_match)

        val back  : Button = findViewById(R.id.doneButton)
        back.setOnClickListener{
            startActivity(Intent(this, MainMenuFragment::class.java))
            finish()
        }
    }
}