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

class FormFragment : Fragment() {

    private lateinit var _binding: FragmentFormBinding
private lateinit var task : Task

    fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = _binding.validationButton
        val editTextTitle = _binding.titleInputEditText
        val editTextDesc = _binding.descInputEditText
        //val editTask = intent.getSerializableExtra("task") as Task?


        val navController = findNavController();
        // We use a String here, but any type that can be put in a Bundle is supported
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Task>("task")?.observe(
            viewLifecycleOwner
        ) { editTask ->
            editTextDesc.setText(editTask?.description)
            editTextTitle.setText(editTask?.title)
            task = editTask
        }

        button.setOnClickListener {
            val id = task?.id ?: UUID.randomUUID().toString()

            val newTask = Task(
                id = id,
                title = editTextTitle.text.toString(),
                description = editTextDesc.text.toString()
            )
            setNavigationResult(newTask, "task")
            findNavController().popBackStack()
            //intent.putExtra("task", newTask)
            //setResult(AppCompatActivity.RESULT_OK, intent)
            //finish()
        }

        //editTextDesc.setText(editTask?.description)
        //editTextTitle.setText(editTask?.title)

       // button.setOnClickListener {
            //val id = editTask?.id ?: UUID.randomUUID().toString()

            //val newTask = Task(id = id, title = editTextTitle.text.toString(),description = editTextDesc.text.toString())
            //intent.putExtra("task", newTask)
            //setResult(AppCompatActivity.RESULT_OK, intent)
            //finish()
        //}
    }


}