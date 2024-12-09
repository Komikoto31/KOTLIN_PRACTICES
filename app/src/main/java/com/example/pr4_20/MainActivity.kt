package com.example.pr4_20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Показать фрагмент камеры при запуске
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CameraFragment())
                .commit()
        }
    }


}