package com.jrmydorm.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jrmydorm.todo.network.Api
import android.content.Intent




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}