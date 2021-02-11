package com.example.tactalk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tactalk.activity.LoginFragment
import com.example.tactalk.activity.RegisterFragment

class HomeFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_home_screen)

        val stopButton : Button = findViewById(R.id.GoMainMenu)

        stopButton.setOnClickListener{
            Toast.makeText(this, "pressed!", Toast.LENGTH_SHORT).show()
        }
        val login : Button = findViewById(R.id.btn_login_page)
        login.setOnClickListener{
            startActivity(Intent(this, LoginFragment::class.java))
        }


    }

    fun login(){
        val login : Button = findViewById(R.id.btn_login_page)
        login.setOnClickListener{
            startActivity(Intent(this, LoginFragment::class.java))
        }

    }

    fun reg(){
        val reg : Button = findViewById(R.id.btn_register_page)
        reg.setOnClickListener{
            startActivity(Intent(this, RegisterFragment::class.java))
        }

    }





   /* override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)



        view.GoMainMenu.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.HomeToMain)}

        view.btn_login_page.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.HomeToLogin)}

        view.btn_register_page.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.HomeToReg)}


        return view
    }*/



}