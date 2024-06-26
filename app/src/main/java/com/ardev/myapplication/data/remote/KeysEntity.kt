package com.ardev.myapplication.data.remote

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class KeysEntity (
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)