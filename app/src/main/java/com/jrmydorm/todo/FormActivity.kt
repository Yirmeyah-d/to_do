package com.jrmydorm.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.jrmydorm.todo.model.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val button =  findViewById<Button>(R.id.validation_button);
        val editTextTitle =  findViewById<EditText>(R.id.title_input_edit_text);
        val editTextDesc =  findViewById<EditText>(R.id.desc_input_edit_text);
        val editTask = intent.getSerializableExtra("task") as Task?
        editTextDesc.setText(editTask?.description)
        editTextTitle.setText(editTask?.title)

        button.setOnClickListener{
            val id = editTask?.id ?: UUID.randomUUID().toString()

            val newTask = Task(id = id, title = editTextTitle.text.toString(),description = editTextDesc.text.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}