package com.jrmydorm.todo.tasklist

import androidx.lifecycle.ViewModel
import com.jrmydorm.todo.data.TasksRepository
import com.jrmydorm.todo.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class TaskListViewModel : ViewModel() {
    private val tasksRepository = TasksRepository()
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    public val taskList: StateFlow<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch {
            val taskResponse = tasksRepository.loadTasks();
            if(taskResponse != null){
                _taskList.value = taskResponse
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val taskResponse = tasksRepository.deleteTask(task);
            if(taskResponse){
                _taskList.value = _taskList.value - task
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val taskResponse = tasksRepository.createTask(task);
            if(taskResponse != null){
                _taskList.value = _taskList.value + taskResponse
            }
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            val taskResponse = tasksRepository.updateTask(task);
            if(taskResponse != null){
                val oldTask = taskList.value.firstOrNull { it.id == taskResponse.id }
                if (oldTask != null) _taskList.value = taskList.value - oldTask + taskResponse
            }
        }
    }
}