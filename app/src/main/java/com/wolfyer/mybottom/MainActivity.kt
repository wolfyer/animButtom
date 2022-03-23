package com.wolfyer.mybottom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.wolfyer.mybottom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btn.setOnClickListener {

            binding.btn.mBtnAinim()//开始加载动画
            val str = binding.et.text.toString()
            Thread(Runnable {
                Thread.sleep(2000)
                Looper.prepare()
                if (str == "是") {
                    runOnUiThread {
                        binding.btn.endAnim("知道就行！弟弟！")
                    }
                } else {
                    runOnUiThread {
                        binding.btn.endAnim("你就是弟弟！")
                    }
                }
                Looper.loop()
            }).start()
        }
    }

    }
