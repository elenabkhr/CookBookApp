package com.practicum.cookbookapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.cookbookapp.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for ActivityLearnWordBinding " +
                    "must not be null"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}