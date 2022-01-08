package com.jrmydorm.todo.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class NavigationUtils {
    companion object {

        fun <T> Fragment.getCurrentNavigationResultLiveData(key: String = "result") =
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

        fun <T> Fragment.getPreviousNavigationResultLiveData(key: String = "result") =
            findNavController().previousBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

        fun <T> Fragment.getPreviousNavigationResult(key: String = "result") =
            findNavController().previousBackStackEntry?.savedStateHandle?.get<T>(key)

        fun <T> Fragment.getCurrentNavigationResult(key: String = "result") =
            findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

        fun <T> Fragment.setPreviousNavigationResult(result: T, key: String = "result") {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
        }

        fun <T> Fragment.setCurrentNavigationResult(result: T, key: String = "result") {
            findNavController().currentBackStackEntry?.savedStateHandle?.set(key, result)
        }

        fun <T> Fragment.removeCurrentNavigationResult(key: String = "result") {
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
        }

        fun <T> Fragment.removePreviousNavigationResult(key: String = "result") {
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
        }
    }
}