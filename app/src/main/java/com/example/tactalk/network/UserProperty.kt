package com.example.tactalk.network

data class UserProperty (
    val result: Response
)

data class Response (
    val username: String,
    val email: String
)