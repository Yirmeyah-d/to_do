package com.jrmydorm.todo.data

import com.jrmydorm.todo.models.Task
import com.jrmydorm.todo.network.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableStateFlow<List<Task>>(value = emptyList())
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            if (fetchedTasks != null) _taskList.value = fetchedTasks
        }
    }


    suspend fun createTask(task: Task): Task? {
        val createTaskResponse = tasksWebService.create(task)
        return if (createTaskResponse.isSuccessful) createTaskResponse.body() else null
    }


    suspend fun deleteTask(task: Task): Boolean {
        return task.id != null && tasksWebService.deleteTask(task.id).isSuccessful
    }

    suspend fun updateTask(task: Task) {
        // TODO: appel réseau et récupération de la tache:
        val tasksResponse = tasksWebService.update(task)
        if (tasksResponse.isSuccessful) {
            val updatedTask = tasksResponse.body()!!
            val oldTask = taskList.value.firstOrNull { it.id == updatedTask.id }
            if (oldTask != null) _taskList.value = _taskList.value - oldTask + updatedTask
        }
    }

}