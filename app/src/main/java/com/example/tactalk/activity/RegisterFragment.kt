package com.example.tactalk.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.example.tactalk.R
import com.example.tactalk.Retrofit.IMyService
import com.example.tactalk.Retrofit.RetrofitClient
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : AppCompatActivity(){


    lateinit var iMyService: IMyService
    internal var compositeDisposable = CompositeDisposable()

    override fun onStop() {
        compositeDisposable.clear();
        super.onStop()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        val retrofit = RetrofitClient.getInstance();
        iMyService = retrofit.create(IMyService::class.java)

    }

    fun onClick(view: View) {
        when(view.id){
            R.id.btn_register -> {
                val itemView = LayoutInflater.from(this@RegisterFragment)
                    .inflate(R.layout.fragment_register, null)

                MaterialStyledDialog.Builder(this@RegisterFragment)
//                    .setIcon(R.drawable.logo)
                    .setTitle("SIGN UP")
                    .setDescription("Please fill all fields")
                    .setCustomView(itemView)
                    .setNegativeText("CANCEL")
                    .onNegative { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveText("Register")
                    .onPositive(
                        MaterialDialog.SingleButtonCallback { _, _ ->

                            val edt_name = itemView.findViewById<View>(R.id.edt_name) as EditText
                            val edt_email = itemView.findViewById<View>(R.id.edt_email) as EditText
                            val edt_password = itemView.findViewById<View>(R.id.edt_password) as EditText
                            val confirm_password = itemView.findViewById<View>(R.id.confirm_password) as EditText

                            if(TextUtils.isEmpty(edt_name.text.toString())){
                                Toast.makeText(this@RegisterFragment, "Name can not be null or Empty", Toast.LENGTH_SHORT).show()
                                return@SingleButtonCallback;
                            }

                            if(TextUtils.isEmpty(edt_email.text.toString())){
                                Toast.makeText(this@RegisterFragment, "Email can not be null or Empty", Toast.LENGTH_SHORT).show()
                                return@SingleButtonCallback;
                            }

                            if(TextUtils.isEmpty(edt_password.text.toString())){
                                Toast.makeText(this@RegisterFragment, "Password can not be null or Empty", Toast.LENGTH_SHORT).show()
                                return@SingleButtonCallback;
                            }

                            if(TextUtils.isEmpty(confirm_password.text.toString())){
                                Toast.makeText(this@RegisterFragment, "Confirm Password can not be null or Empty", Toast.LENGTH_SHORT).show()
                                return@SingleButtonCallback;
                            }

                            if(edt_password != confirm_password){
                                Toast.makeText(this@RegisterFragment, "Confirm Password not match with password ", Toast.LENGTH_SHORT).show()
                                return@SingleButtonCallback;
                            }

                            registerUser(edt_email.text.toString(), edt_name.text.toString(), edt_password.text.toString(), confirm_password.text.toString())

                        })
            }
            R.id.goLogin -> {
                startActivity(Intent(this, LoginFragment::class.java))
            }

        }

    }


    private fun registerUser(name: String, email: String, password: String, confirmPassword: String) {
        compositeDisposable.addAll(iMyService.registerUser(name, email, password, confirmPassword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                Toast.makeText(this@RegisterFragment, "" +result, Toast.LENGTH_SHORT).show()
            })
    }

}