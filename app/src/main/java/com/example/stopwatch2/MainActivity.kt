package com.example.stopwatch2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.stopwatch2.databinding.ActivityMainBinding
import java.util.Timer
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0
    private var timerTask: Timer? = null
    private var isRunning = false
    private var timeRecord = 1

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            isRunning = !isRunning

            if(isRunning) {
                start()
            } else {
                pause()
            }
        }

        binding.btnRecord.setOnClickListener {
            timeSave()
        }

        binding.btnReset.setOnClickListener {
            reset()
        }
    }

    private fun start() {
        val color = getColor(R.color.coral)
        binding.btnStart.setImageResource(R.drawable.stop_vector)
//        binding.btnStart.imageTintList = c
        timerTask = timer(period = 10) {
            time++
            val min = time / 6000
            val sec = (time % 6000) / 100
            val milli = time % 100
            runOnUiThread{
                if(isRunning) {
                    binding.showMillisecond.text = if(milli < 10) ".0$milli" else ".$milli"
                    binding.showSecond.text = if(sec < 10) ":0$sec" else ".$sec"
                    binding.showMinute.text = if(min < 10) "0$min" else "$min"
                    binding.btnRecord.isEnabled = true
                    binding.btnReset.isEnabled = true
                }
            }
        }
    }

    private fun pause() {
        binding.btnStart.setImageResource(R.drawable.start_vector)
        timerTask?.cancel()
    }

    private fun timeSave() {
        val minTime = this.time / 6000
        val secTime = (this.time % 6000) / 100
        val milliTime = this.time % 100
        val textView = TextView(this)
        val minRecord = if(minTime < 10) "0${minTime}" else "$minTime"
        val secRecord = if(secTime < 10) "0${secTime}" else "$secTime"
        val milliRecord = if(milliTime < 10) "0${milliTime}" else "$milliTime"
        textView.text = if(timeRecord < 10) "0${timeRecord}번째 기록: ${minRecord}:${secRecord}.${milliRecord}" else "${timeRecord}번째 기록: ${minRecord}:${secRecord}.${milliRecord}"

        binding.btnReset.isEnabled = true
        binding.recordLayout.addView(textView, 0)
        timeRecord++
    }

    private fun reset() {
        timerTask?.cancel()
        time = 0
        isRunning = false

        binding.btnStart.setImageResource(R.drawable.start_vector)
        binding.showMinute.text = "00"
        binding.showSecond.text = ":00"
        binding.showMillisecond.text = ".00"

        binding.btnRecord.isEnabled = false
        binding.btnReset.isEnabled = false

        binding.recordLayout.removeAllViews()
        timeRecord = 1
    }
}
