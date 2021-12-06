package com.jrmydorm.todo.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    var title: String? = "",
    @SerialName("description")
    var description: String? = ""
) : java.io.Serializable