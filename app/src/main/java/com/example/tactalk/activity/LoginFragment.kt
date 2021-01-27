package com.example.tactalk.activity


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tactalk.R
import com.example.tactalk.Retrofit.IMyService
import com.example.tactalk.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : AppCompatActivity() {

    lateinit var iMyService: IMyService
    internal var compositeDisposable = CompositeDisposable()


    override fun onStop() {
        compositeDisposable.clear();
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        //API
        val retrofit = RetrofitClient.getInstance();
        iMyService = retrofit.create(IMyService::class.java)

    }

    fun onClick(view: View) {
        when(view.id){
            R.id.btn_login -> {
                // go to login page
                loginUser(edt_name.text.toString(), edt_password.text.toString())
            }
            R.id.goRegister -> {
                // go to login page
                startActivity(Intent(this, RegisterFragment::class.java))
            }

        }

    }

    private fun loginUser(name: String, password: String) {
        //check empty
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this@LoginFragment, "Name can not be null or Empty", Toast.LENGTH_SHORT).show()
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this@LoginFragment, "Password can not be null or Empty", Toast.LENGTH_SHORT).show()
            return;
        }

        compositeDisposable.addAll(iMyService.loginUser(name, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                Toast.makeText(this@LoginFragment, "" +result, Toast.LENGTH_SHORT).show()
            })
    }
}
