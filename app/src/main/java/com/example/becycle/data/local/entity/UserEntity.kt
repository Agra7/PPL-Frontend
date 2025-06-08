package com.example.becycle.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "full_name")
    val fullName: String?,

    @ColumnInfo(name = "bio")
    val bio: String?,

    @ColumnInfo(name = "address")
    val address: String?,

    @ColumnInfo(name = "exp")
    val exp: Int?,

    @ColumnInfo(name = "profile_picture")
    val profilePicture: String?
)
