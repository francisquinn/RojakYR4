package com.example.tactalk


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tactalk.activity.LoginFragment
import com.example.tactalk.activity.RegisterFragment


class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_app_start_page)

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_login_page -> {
                // go to login page
                startActivity(Intent(this, LoginFragment::class.java))
            }
            R.id.btn_register_page -> {
                //go to register page
                startActivity(Intent(this, RegisterFragment::class.java))
            }
        }

    }

}