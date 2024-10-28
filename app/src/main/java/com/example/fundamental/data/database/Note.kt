package com.example.fundamental.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val title: String,
    val description: String,
    val coverImage: String,
    val beginTime: String,
    val eventQuota: String,
    val eventOwner: String,
    val isFavorite: Boolean = false,
    val eventLink: String? = null
) : Parcelable
