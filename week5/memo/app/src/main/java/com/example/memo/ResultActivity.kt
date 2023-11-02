package com.example.memo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memo.databinding.ActivityResultBinding

class ResultActivity: AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        binding.tv.text=intent.getStringExtra("text")
        setContentView(binding.root)
        binding.button.setOnClickListener{
            finish()
        }
    }
}