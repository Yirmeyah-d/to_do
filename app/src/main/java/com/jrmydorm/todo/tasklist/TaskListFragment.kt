package com.jrmydorm.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrmydorm.todo.FormActivity
import com.jrmydorm.todo.databinding.FragmentTaskListBinding
import com.jrmydorm.todo.model.Task
import java.util.*

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    val adapter = TaskListAdapter()

    val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as? Task

        if (task != null) {
            val oldTask = taskList.firstOrNull { it.id == task.id }
            if (oldTask != null)  taskList.remove(oldTask)

            taskList.add(task)
            adapter.submitList(taskList.toList())
        }

    }

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        adapter.submitList(taskList.toList())
        binding.floatingActionButton.setOnClickListener{
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }
        adapter.onClickDelete = { task ->
            taskList.remove(task)
            adapter.submitList(taskList.toList())
        }

        adapter.onClickEdit = { task ->
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("task", task)
            formLauncher.launch(intent)
        }

    }
}
