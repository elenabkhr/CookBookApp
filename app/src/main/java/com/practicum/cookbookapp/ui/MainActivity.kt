package com.practicum.cookbookapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.practicum.cookbookapp.R
import com.practicum.cookbookapp.databinding.ActivityMainBinding
import com.practicum.cookbookapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for ActivityMainBinding must not be null"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val thread = Thread(object : Runnable {
            override fun run() {
                val url = URL("https://recipes.androidsprint.ru/api/category")
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val jsonBody = connection.inputStream.bufferedReader().readText()
                Log.i("!!!", "Body: $jsonBody")

                val categories = Json.decodeFromString<List<Category>>(jsonBody)

                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            }
        })
        thread.start()
        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        binding.btnNavFavorites.setOnClickListener {
            findNavController(R.id.mainContainer)
                .navigate(R.id.favoritesFragment)
        }

        binding.btnNavCategories.setOnClickListener {
            findNavController(R.id.mainContainer)
                .navigate(R.id.categoriesListFragment)
        }
    }
}