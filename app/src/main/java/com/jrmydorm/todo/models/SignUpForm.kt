package com.jrmydorm.todo.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpForm(
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String,
    @SerialName("email")
    val email   : String,
    @SerialName("password")
    val password   : String,
)
