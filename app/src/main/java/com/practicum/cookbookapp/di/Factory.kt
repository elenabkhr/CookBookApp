package com.practicum.cookbookapp.di

interface Factory<T> {
    fun create(): T
}