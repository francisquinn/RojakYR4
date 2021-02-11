package com.example.tactalk.network

import com.squareup.moshi.Json

data class StatsProperty (
        val result: Response
)

data class Response (
        val teamGoal: String,
        val teamPoints: String,
        val teamShots: String,
        val teamKickouts:String,
        val teamWides: String,
        val oppTeamGoal: Int,
        val oppTeamPoints:Int,
        val oppTeamShots: Int,
        val oppTeamTurnover: Int
)

