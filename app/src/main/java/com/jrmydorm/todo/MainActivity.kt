package com.jrmydorm.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.jrmydorm.todo.network.Api
import android.content.Intent




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val Token = Api.getToken()
        if(Token.isNullOrBlank()){
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }
}