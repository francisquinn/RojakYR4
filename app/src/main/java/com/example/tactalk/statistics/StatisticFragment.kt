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
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tactalk.MainMenuFragment
import com.example.tactalk.R
import com.example.tactalk.RecordingPageFragment
import com.example.tactalk.SetUpMatchFragment
import com.example.tactalk.databinding.FragmentStatisticBinding

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

