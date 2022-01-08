package com.jrmydorm.todo.task
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jrmydorm.todo.databinding.ItemTaskBinding
import com.jrmydorm.todo.models.Task

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) =  oldItem.id == newItem.id;
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.equals(newItem);
}

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}

class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task,TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text =  task.title
            binding.taskDesc.text = task.description
            binding.deleteButton.setOnClickListener{
                listener.onClickDelete(task)
            }
            binding.editButton.setOnClickListener{
                listener.onClickEdit(task)
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
