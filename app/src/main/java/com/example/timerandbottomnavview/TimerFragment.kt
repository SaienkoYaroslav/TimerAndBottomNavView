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


class TimerFragment : Fragment() {

    private lateinit var btnNext: Button

    private lateinit var timerText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var countdownTimer: CountDownTimer
    private var totalTime = 30000 // Загальний час у мілісекундах
    private val interval = 100 // Інтервал оновлення прогресу у мілісекундах
    private var startProgress = 100f
    var currentProgress = 0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnNext = view.findViewById(R.id.btn_next_activity)
        btnNext.setOnClickListener {
            Toast.makeText(view.context, "Clicked", Toast.LENGTH_SHORT).show()
            startCountdownTimer(view)
        }

        timerText = view.findViewById(R.id.timer_text)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.max = 100 // Встановлення максимального значення прогресу
        progressBar.scaleX = -1f

        startCountdownTimer(view)
    }


    private fun saveTimer(view: View?, key: String, value: Int) {
        if (view != null) {
            val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(key, value).apply()
        }
    }

    private fun loadTimer(view: View, key: String): Int {
        val sharedPreferences = view.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val contTimerFrom = sharedPreferences.getInt(key, 30000)
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
        totalTime = loadTimer(view, "key")
        if (totalTime < 1000) totalTime = 30000
        Log.d("Timer", "load timer: $totalTime")
        startProgress = loadProgressBar(view, "key_progress_start")
        currentProgress = loadProgressBar(view, "key_progress")
        Log.d("Timer", "load start progress: $startProgress")
        val progressIncrement =
            startProgress / (totalTime / interval) // Приріст прогресу на кожному інтервалі
//        var currentProgress = 0f // Поточне значення прогресу

        progressBar.progress = currentProgress.toInt()

        countdownTimer = object : CountDownTimer(totalTime.toLong(), interval.toLong()) {
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
            }
        }
        countdownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer.cancel()
        Log.d("Timer", "save timer: $totalTime")
        saveTimer(view, "key", totalTime)
        saveProgressBar(view, "key_progress", currentProgress)
        saveProgressBar(view, "key_progress_start", startProgress)
    }

}