package com.rafael0117.appgestionalmacen.utils

import android.app.Application
import android.content.Context
import com.rafael0117.appgestionalmacen.data.InitBD

class appConfig:Application() {
    companion object{
        lateinit var CONTEXT:Context
        lateinit var BD:InitBD
        var BD_NAME="almacen.bd"
        var VERSION=2
    }
    override fun onCreate() {
        super.onCreate()
        CONTEXT =applicationContext
        BD=InitBD()
    }

}