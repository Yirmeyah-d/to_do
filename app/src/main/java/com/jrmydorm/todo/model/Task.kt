package com.jrmydorm.todo.model

data class Task(
    val id: String,
    var title: String,
    var description: String? = ""
    ) : java.io.Serializable
