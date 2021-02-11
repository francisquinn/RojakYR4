package com.example.tactalk.network

import com.squareup.moshi.Json

data class StatsProperty (
        val result: Response
)

data class Response (
        val teamGoal: String,
        val teamPoints: Int,
        val teamShots: Int,
        val teamKickouts:Int,
        val teamWides: Int,
        val oppTeamGoal: Int,
        val oppTeamPoints:Int,
        val oppTeamShots: Int,
        val oppTeamTurnover: Int
)

