package com.example.eslatmalar

data class UserModel(
    val userFirstName: String,
    val userLastNAme: String,
    val userProfileImage: Int,
    val isLogin: Boolean = false
)
