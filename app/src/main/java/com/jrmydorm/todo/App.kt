package com.jrmydorm.todo

import android.app.Application
import com.jrmydorm.todo.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.setUpContext(this)
    }
}