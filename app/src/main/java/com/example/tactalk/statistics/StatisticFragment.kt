/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.tactalk.statistics

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tactalk.MainMenuFragment
import com.example.tactalk.R
import com.example.tactalk.recording.RecordingPageFragment
import com.example.tactalk.databinding.FragmentStatisticBinding
import kotlinx.android.synthetic.main.fragment_statistic.*

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class StatisticFragment : AppCompatActivity() {

    private val viewModel: StatisticViewModel by lazy {
        ViewModelProvider(this).get(StatisticViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: FragmentStatisticBinding = DataBindingUtil.setContentView(this, R.layout.fragment_statistic)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        stats()
        main()


        //for home team stats
        hometeam2.isInvisible = false

        shots2.isInvisible = false

        kickout2.isInvisible = false

        wides2.isInvisible = false

        turn.isInvisible = false

        //for opp team

        op2.isInvisible = true

        opshots2.isInvisible = true

        opkickout2.isInvisible = true

        opwides.isInvisible = true

        opturn.isInvisible = true

        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked


                hometeam2.isInvisible = false

                shots2.isInvisible = false

                kickout2.isInvisible = false

                wides2.isInvisible = false

                opturn.isInvisible = false


                //op team
                op2.isInvisible = true

                opshots2.isInvisible = true

                opwides.isInvisible = true

                opkickout2.isInvisible = true


                turn.isInvisible = true
            } else {
                // The switch is disabled

                hometeam2.isInvisible = true

                shots2.isInvisible = true

                kickout2.isInvisible = true

                wides2.isInvisible = true

                turn.isInvisible = true



                // op team
                op2.isInvisible = false

                opshots2.isInvisible = false

                opkickout2.isInvisible = false

                opwides.isInvisible = false

                opturn.isInvisible = false
            }
        }

        // Set a click listener for root layout object
        switch1.setOnClickListener{
            // Get the switch button state programmatically
            if(switch1.isChecked){
                // If switch button is checked/on then
                // The switch is enabled/checked

                hometeam2.isInvisible = true

                shots2.isInvisible = true

                kickout2.isInvisible = true

                wides2.isInvisible = true

                turn.isInvisible = true

                //op team
                op2.isInvisible = false

                opshots2.isInvisible = false

                opwides.isInvisible = false

                opkickout2.isInvisible = false


                opturn.isInvisible = false
            }else{
                // The switch is unchecked

                hometeam2.isInvisible = false

                shots2.isInvisible = false

                kickout2.isInvisible = false

                wides2.isInvisible = false

                turn.isInvisible = false

                //op team
                op2.isInvisible = true

                opshots2.isInvisible = true

                opkickout2.isInvisible = true

                opwides.isInvisible = true

                opturn.isInvisible = true
            }
        }



    }

    fun stats(){
        val stats  : Button = findViewById(R.id.record)
        stats.setOnClickListener{
            startActivity(Intent(this, RecordingPageFragment::class.java))
        }
    }

    fun main(){
        val stats  : Button = findViewById(R.id.mainmenu)
        stats.setOnClickListener{
            startActivity(Intent(this, MainMenuFragment::class.java))
        }
    }




}

