package com.example.tactalk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main_menu.view.*
import kotlinx.android.synthetic.main.fragment_set_up_match.view.*
import kotlinx.android.synthetic.main.fragment_stats.view.*


class StatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        view.NextHalfButton.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.StatToSecondHalf)}
        return view
    }


}