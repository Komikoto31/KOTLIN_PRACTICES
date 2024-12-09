package com.example.work2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstFragment = FirstFragment()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, firstFragment)
            .commit()
    }
}