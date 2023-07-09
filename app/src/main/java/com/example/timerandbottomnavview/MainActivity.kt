package com.example.timerandbottomnavview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("key", 30000).apply()
        sharedPreferences.edit().putFloat("key_progress", 0f).apply()
        sharedPreferences.edit().putFloat("key_progress_start", 100f).apply()
    }


}