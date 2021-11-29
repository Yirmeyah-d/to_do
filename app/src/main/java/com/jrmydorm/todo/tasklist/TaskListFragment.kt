package com.jrmydorm.todo.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrmydorm.todo.databinding.FragmentTaskListBinding
import com.jrmydorm.todo.model.Task
import java.util.*

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding

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
        val adapter = TaskListAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(taskList.toList())
        binding.floatingActionButton.setOnClickListener{
            taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"))
            recyclerView.adapter = adapter
            adapter.submitList(taskList.toList())
        }
    }
}
