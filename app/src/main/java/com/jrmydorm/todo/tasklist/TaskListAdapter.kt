package com.jrmydorm.todo.tasklist
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jrmydorm.todo.FormActivity
import com.jrmydorm.todo.databinding.ItemTaskBinding
import com.jrmydorm.todo.model.Task

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) =  oldItem.id == newItem.id;
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.equals(newItem);
}


class TaskListAdapter() : ListAdapter<Task,TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text =  task.title
            binding.taskDesc.text = task.description
            binding.deleteButton.setOnClickListener{
                onClickDelete(task)
            }
            binding.editButton.setOnClickListener{
                onClickEdit(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
