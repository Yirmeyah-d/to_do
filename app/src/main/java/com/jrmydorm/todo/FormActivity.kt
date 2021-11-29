package com.jrmydorm.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.jrmydorm.todo.model.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val button =  findViewById<Button>(R.id.validation_button);
        button.setOnClickListener{
            val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}