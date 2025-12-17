package com.practicum.cookbookapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.practicum.cookbookapp.R
import com.practicum.cookbookapp.data.URL_RECIPES_CATEGORY
import com.practicum.cookbookapp.databinding.ActivityMainBinding
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for ActivityMainBinding must not be null"
        )

    val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

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

        threadPool.execute {
            val url = URL(URL_RECIPES_CATEGORY)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val jsonBody = connection.inputStream.bufferedReader().readText()
            val categories = Json.decodeFromString<List<Category>>(jsonBody)
            val categoriesIds = categories.map { it.id }

            for (id in categoriesIds) {
                threadPool.execute {
                    val url = URL("$URL_RECIPES_CATEGORY/$id/recipes")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connect()
                    val jsonBody = connection.inputStream.bufferedReader().readText()
                    val recipe = Json.decodeFromString<List<Recipe>>(jsonBody)

                    Log.i(
                        "!!!",
                        "threadPool - Выполняю запрос на потоке: ${Thread.currentThread().name}"
                    )
                    Log.i("!!!", "Список рецептов: ${recipe.map { it.title }}")
                }
            }
        }
        Log.i("!!!", "main - Выполняю запрос на потоке: ${Thread.currentThread().name}")

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