package com.jrmydorm.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jrmydorm.todo.R
import com.jrmydorm.todo.model.Task

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) =  oldItem.id == newItem.id;
    override fun areContentsTheSame(oldItem: Task, newItem: Task) =oldItem.equals(newItem);
}


class TaskListAdapter() : ListAdapter<Task,TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.findViewById<TextView>(R.id.task_title).text = task.title
            itemView.findViewById<TextView>(R.id.task_desc).text = task.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task,
            parent,
            false
        )
        return TaskViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])

    }
}
