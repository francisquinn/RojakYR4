package com.example.tactalk.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tactalk.MainMenuFragment
import com.example.tactalk.R
import com.example.tactalk.network.RetrofitClient
import com.example.tactalk.network.TacTalkAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_main_menu.view.*
import kotlinx.android.synthetic.main.fragment_set_up_match.*
import kotlinx.android.synthetic.main.fragment_set_up_match.view.*
import java.util.*


class SetUpMatchFragment : AppCompatActivity() {

    lateinit var tacTalkAPI: TacTalkAPI
    private var compositeDisposable = CompositeDisposable()


    val mcurrentTime = Calendar.getInstance()
    val year = mcurrentTime.get(Calendar.YEAR)
    val month = mcurrentTime.get(Calendar.MONTH)
    val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)

    override fun onStop() {
        compositeDisposable.clear();
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_set_up_match)

        val retrofit = RetrofitClient.getInstance();
        tacTalkAPI = retrofit.create(TacTalkAPI::class.java)


        val mTimePicker: TimePickerDialog = TimePickerDialog(this, { view, hourOfDay, minute ->
            selectedTime.text = String.format("%d : %d", hourOfDay, minute)
        }, hour, minute, false)


        selectTime.setOnClickListener {
            mTimePicker.show()
        }

    }

    fun onClick(view: View) {
        when(view.id){
            R.id.btn_setUpMatch -> {
                val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                    selectedDate.text = String.format("%d / %d / %d", dayOfMonth, month + 1, year) }, year, month, day);

                datePicker.show()
            }
            R.id.btn_setUpMatch -> {
                setUpMatch(edt_game_name.text.toString(),
                    chosen_game_type.toString(),
                    edt_team_name.text.toString(),
                    chosen_team_colour.toString(),
                    edt_opposition.text.toString(),
                    chosen_opp_colour.toString(),
                    edt_location.text.toString(),
                    selectDate.toString(),
                    selectTime.toString())
                startActivity(Intent(this, MainMenuFragment::class.java))
                finish()
            }
        }

    }

    private fun setUpMatch(gameType: String, teamName: String,
                           teamColor: String, opposition: String, oppColor: String,
                           location: String, public: String, startDate: String, startTime: String) {

        if (TextUtils.isEmpty(gameType)){
            Toast.makeText(this@SetUpMatchFragment, "Game Type can not be null or Empty", Toast.LENGTH_SHORT).show()
            return;
        }

        if (TextUtils.isEmpty(teamName)){
            Toast.makeText(this@SetUpMatchFragment, "Team Name can not be null or Empty", Toast.LENGTH_SHORT).show()
            return;
        }

        if (TextUtils.isEmpty(opposition)){
            Toast.makeText(this@SetUpMatchFragment, "Opposition can not be null or Empty", Toast.LENGTH_SHORT).show()
            return;
        }

        if (TextUtils.isEmpty(location)){
            Toast.makeText(this@SetUpMatchFragment, "Location can not be null or Empty", Toast.LENGTH_SHORT).show()
            return;
        }

        if (TextUtils.isEmpty(startDate)){
            Toast.makeText(this@SetUpMatchFragment, "You must select a start date", Toast.LENGTH_SHORT).show()
            return;
        }

        compositeDisposable.addAll(tacTalkAPI.setUpMatch(gameType, teamName, teamColor, opposition, oppColor, location, public, startDate, startTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    Toast.makeText(this@SetUpMatchFragment, "" + result, Toast.LENGTH_SHORT).show()
                })
    }

}

