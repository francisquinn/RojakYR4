package com.example.tactalk.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
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


class SetUpMatchFragment : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    lateinit var tacTalkAPI: TacTalkAPI
    internal var compositeDisposable = CompositeDisposable()
    private var mDisplayDate: EditText? = null
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var mDisplayTime: EditText? = null
    private var mTimeSetListener: TimePickerDialog.OnTimeSetListener? = null

    override fun onStop() {
        compositeDisposable.clear();
        super.onStop()
    }


    fun onCreate(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val retrofit = RetrofitClient.getInstance();
        tacTalkAPI = retrofit.create(TacTalkAPI::class.java)

        pickDate()
        pickTime()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_set_up_match, container, false)

        view.findViewById<View>(R.id.btn_setUpMatch).setOnClickListener {
            setUpMatch(edt_game_name.text.toString(),
                    chosen_game_type.toString(),
                    edt_team_name.text.toString(),
                    chosen_team_colour.toString(),
                    edt_opposition.text.toString(),
                    chosen_opp_colour.toString(),
                    edt_location.text.toString(),
                    set_match_date.toString(),
                    set_match_time.toString())
            startActivity(Intent(this, MainMenuFragment::class.java))
        }

        view.btn_setUpMatch.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.SetupToMain)}
        return view

    }

    private fun getDateCalendar(){
        val cal = Calendar.getInstance()
        year = cal[Calendar.YEAR]
        month = cal[Calendar.MONTH]
        day = cal[Calendar.DAY_OF_MONTH]
    }

    private fun getTimeCalendar(){
        val cal = Calendar.getInstance()
        hour = cal[Calendar.HOUR_OF_DAY]
        minute = cal[Calendar.MINUTE]
    }


    private fun pickDate(){
        mDisplayDate = findViewById<View>(R.id.set_match_date) as EditText

        mDisplayDate!!.setOnClickListener{
            getDateCalendar()
            val dialog = DatePickerDialog(
                    this@SetUpMatchFragment,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }


    private fun pickTime(){
        mDisplayTime = findViewById<View>(R.id.set_match_time) as EditText

    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        getTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()


    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

        mDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            var month = month
            month += 1
            Log.d("SetUpMatchFragment", "onDateSet: dd/mm/yy: $day/$month/$year")

        }

        mTimeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            Log.d("SetUpMatchFragment", "onDateSet: hh:mm : $hour/$minute")
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

