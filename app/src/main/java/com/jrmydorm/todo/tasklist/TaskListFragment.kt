package com.jrmydorm.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.jrmydorm.todo.FormActivity
import com.jrmydorm.todo.UserInfoActivity
import com.jrmydorm.todo.databinding.FragmentTaskListBinding
import com.jrmydorm.todo.models.Task
import com.jrmydorm.todo.network.Api
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private val adapter = TaskListAdapter(createListener())
    private val viewModel: TaskListViewModel by viewModels()

    fun createListener(): TaskListListener {
        val listener = object :  TaskListListener{
            override fun onClickDelete(task: Task) {
                viewModel.deleteTask(task)
            }

            override fun onClickEdit(task: Task) {
                val intent = Intent(activity, FormActivity::class.java)
                intent.putExtra("task", task)
                updateFormLauncher.launch(intent)
            }
        }
        return listener
    }

    val createFormLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as? Task

            if (task != null) {
                lifecycleScope.launch {
                    viewModel.addTask(task)
                }

            }

        }

    val updateFormLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = result.data?.getSerializableExtra("task") as? Task
            if (task != null) {
                lifecycleScope.launch {
                    viewModel.editTask(task)
                }
            }
        }

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
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(activity, FormActivity::class.java)
            createFormLauncher.launch(intent)
        }

        lifecycleScope.launch {
            viewModel.taskList.collectLatest { newList ->
                adapter.submitList(newList)
            }
        }
        binding.imageView.setOnClickListener{
            val intent = Intent(activity, UserInfoActivity::class.java)
            createFormLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.userInfoTextView.text = "${userInfo.firstName} ${userInfo.lastName}"
            viewModel.loadTasks()
        }
        binding.imageView.load("https://goo.gl/gEgYUd") {
            transformations(CircleCropTransformation())
        }
    }

}
