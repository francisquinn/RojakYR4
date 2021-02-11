package com.example.tactalk


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.tactalk.activity.LoginFragment
import com.example.tactalk.activity.RegisterFragment
import com.example.tactalk.statistics.StatisticFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home_screen)
        val stopButton : Button = findViewById(R.id.GoMainMenu)

        stopButton.setOnClickListener{
            Toast.makeText(this, "pressed!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainMenuFragment::class.java))
        }

        val login : Button = findViewById(R.id.btn_login_page)
        login.setOnClickListener{
            startActivity(Intent(this, LoginFragment::class.java))
        }

        val reg : Button = findViewById(R.id.btn_register_page)
        reg.setOnClickListener{
            startActivity(Intent(this, RegisterFragment::class.java))
        }

        val tes : Button = findViewById(R.id.testing)
        tes.setOnClickListener{
            startActivity(Intent(this, StatisticFragment::class.java))
        }




    }
}

