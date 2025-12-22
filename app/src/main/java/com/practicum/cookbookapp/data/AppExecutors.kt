package com.practicum.cookbookapp.data

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppExecutors {
    val threadPool: ExecutorService = Executors.newFixedThreadPool(4)
}