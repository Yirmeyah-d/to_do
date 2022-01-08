package com.jrmydorm.todo.form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jrmydorm.todo.models.Task
import java.util.*
import com.jrmydorm.todo.databinding.FragmentFormBinding
import com.jrmydorm.todo.utils.NavigationUtils.Companion.getPreviousNavigationResult

class FormFragment : Fragment() {

    private lateinit var task: Task
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!


    fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var task = getPreviousNavigationResult<Task>("task")


        val validationButton = binding.validationButton
        val editTextTitle = binding.titleInputEditText
        val editTextDesc = binding.descInputEditText

        editTextTitle.setText(task?.title)
        editTextDesc.setText(task?.description)


        validationButton.setOnClickListener {
            val id = task?.id ?: UUID.randomUUID().toString()

            val newTask = Task(
                id = id,
                title = editTextTitle.text.toString(),
                description = editTextDesc.text.toString()
            )
            setNavigationResult(newTask, "newTask")
            findNavController().popBackStack()
        }

    }


}