package com.practicum.cookbookapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String,
    @kotlinx.serialization.Transient val categoryId: Int = 0,
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var ingredients: List<Ingredient> = emptyList()

    @IgnoredOnParcel
    @Ignore
    var method: List<String> = emptyList()
}