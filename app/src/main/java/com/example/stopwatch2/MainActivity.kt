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

        binding.btnRecord.setOnClickListener { timeSave() }

        binding.btnReset.setOnClickListener { reset() }
    }

    private fun start() {
        binding.btnStart.setImageResource(R.drawable.stop_vector)
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
                }
            }
        }
    }

    private fun pause() {
        binding.btnStart.setImageResource(R.drawable.start_vector)
        timerTask?.cancel()
    }

    private fun timeSave() {
        val lapTime = this.time
        val textView = TextView(this)
        textView.text = "${timeRecord}번째 기록: ${lapTime / 6000}:${(lapTime % 6000) / 100}.${lapTime % 100}"

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

        binding.recordLayout.removeAllViews()
        timeRecord = 1
    }
}
