package com.example.tactalk

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import kotlinx.android.synthetic.main.timepicker.*
import java.util.*

//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.timepicker)
//
//        val mcurrentTime = Calendar.getInstance()
//        val year = mcurrentTime.get(Calendar.YEAR)
//        val month = mcurrentTime.get(Calendar.MONTH)
//        val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)
//
//        val datePicker = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
//            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                selectedDate.setText(String.format("%d / %d / %d", dayOfMonth, month + 1, year))
//            }
//        }, year, month, day)
//
//
//        selectDate.setOnClickListener({ v ->
//            datePicker.show()
//        })
//    }
//}
//
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_set_up_match)
    }

}
