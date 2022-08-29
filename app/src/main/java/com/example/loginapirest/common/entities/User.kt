package com.example.loginapirest.common.entities

data class User(
    var id: Int = 0,
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var avatar: String = "",
    var password: String = "",
    var url: String = "",
    var text: String = ""
) {
    val fullName: String get() = "$firstName $lastName"
}