package com.jrmydorm.todo.task

import com.jrmydorm.todo.models.Task
import com.jrmydorm.todo.network.Api

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createTask(task: Task): Task? {
        val createTaskResponse = tasksWebService.createTask(task)
        return if (createTaskResponse.isSuccessful) createTaskResponse.body() else null
    }

    suspend fun deleteTask(task: Task): Boolean {
        val deleteTaskResponse = tasksWebService.deleteTask(task.id)
        return deleteTaskResponse.isSuccessful
    }

    suspend fun updateTask(task: Task): Task? {
        val updateTaskResponse = tasksWebService.updateTask(task)
        return if (updateTaskResponse.isSuccessful) updateTaskResponse.body() else null
    }

}