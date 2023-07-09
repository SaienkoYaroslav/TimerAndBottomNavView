package com.example.timerandbottomnavview

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlin.concurrent.timer
import kotlin.math.log


class TimerFragment : Fragment() {

    private lateinit var btnNext: Button

    private lateinit var timerText: TextView
    private lateinit var progressBar: ProgressBar
    var countdownTimer: CountDownTimer? = null
    private var totalTime: Long = 30000L // Загальний час у мілісекундах
    private val interval = 100 // Інтервал оновлення прогресу у мілісекундах
    private var startProgress = 100f
    var currentProgress = 0f

    var exitTime = 0L
    var currentTime = 0L

    var timerIsRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnNext = view.findViewById(R.id.btn_next_activity)
        timerText = view.findViewById(R.id.timer_text)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.max = 100 // Встановлення максимального значення прогресу
        progressBar.scaleX = -1f
        btnNext.setOnClickListener {
            Toast.makeText(view.context, "Clicked", Toast.LENGTH_SHORT).show()
            startCountdownTimer(view)
        }

        timerIsRunning = loadIsTimerRunning(view, "is_running")
        if (timerIsRunning) {
            exitTime = loadCurrentTime(view, "key_current_time")
            currentTime = System.currentTimeMillis() - exitTime
            Log.d("Deff", "deff time is: $currentTime")
            totalTime = loadTimer(view, "key")
            totalTime -= currentTime

            startProgress = loadProgressBar(view, "key_progress_start")
//            var x = totalTime / 1000
//            startProgress = (x * 100)/30f
            Log.d("Deff", "deff start progress is: $startProgress")
            currentProgress = loadProgressBar(view, "key_progress")
            Log.d("Deff", "deff start current progress is: $currentProgress")
        } else {
            totalTime = loadTimer(view, "key")
        }


        startCountdownTimer(view)
    }

    private fun saveIsTimerRunning(view: View?, key: String, value: Boolean) {
        if (view != null) {
            val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, value).apply()
        }
    }

    private fun loadIsTimerRunning(view: View, key: String): Boolean {
        val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val isTimerRunning = sharedPreferences.getBoolean(key, true)
        return isTimerRunning
    }

    private fun saveCurrentTime(view: View?, key: String, value: Long) {
        if (view != null) {
            val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putLong(key, value).apply()
        }
    }

    private fun loadCurrentTime(view: View, key: String): Long {
        val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val currentTime = sharedPreferences.getLong(key, 0)
        return currentTime
    }


    private fun saveTimer(view: View?, key: String, value: Long) {
        if (view != null) {
            val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putLong(key, value).apply()
        }
    }

    private fun loadTimer(view: View, key: String): Long {
        val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val contTimerFrom = sharedPreferences.getLong(key, 30000L)
        return contTimerFrom
    }

    private fun saveProgressBar(view: View?, key: String, value: Float) {
        if (view != null) {
            val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putFloat(key, value).apply()
        }
    }

    private fun loadProgressBar(view: View, key: String): Float {
        val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val contProgressBarFrom = sharedPreferences.getFloat(key, 0f)
        return contProgressBarFrom
    }


    private fun startCountdownTimer(view: View) {
        timerIsRunning = true

//        startProgress = loadProgressBar(view, "key_progress_start")
//        currentProgress = loadProgressBar(view, "key_progress")
//        Log.d("Timer", "load start progress: $startProgress")
        val progressIncrement =
            startProgress / (totalTime / interval) // Приріст прогресу на кожному інтервалі
//        var currentProgress = 0f // Поточне значення прогресу

        progressBar.progress = currentProgress.toInt()

        countdownTimer = object : CountDownTimer(totalTime, interval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                currentProgress += progressIncrement
                Log.d("Timer", "current progress: $currentProgress")
                startProgress -= 0.33f
                Log.d("Timer", "current start progress: $startProgress")
                totalTime -= 100
                Log.d("Timer", "current timer: $totalTime")
                progressBar.progress = currentProgress.toInt()
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                timerText.text = secondsRemaining.toString()
            }

            override fun onFinish() {
                progressBar.progress = 100 // Завершення прогресу на 100%
                timerText.text = "0"
                timerIsRunning = false
            }
        }
        countdownTimer?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        Log.d("Timer", "save timer: $totalTime")
        saveTimer(view, "key", totalTime)
        saveProgressBar(view, "key_progress", currentProgress)
        saveProgressBar(view, "key_progress_start", startProgress)

        val currentTime = System.currentTimeMillis()
        saveCurrentTime(view, "key_current_time", currentTime)

        saveIsTimerRunning(view, "is_running", timerIsRunning)
    }

}