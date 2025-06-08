package com.example.becycle.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recycle")
data class RecycleEntity(
    @PrimaryKey
    @ColumnInfo(name = "recycle_id")
    val recycleId: Int,

    @ColumnInfo(name = "material_type")
    val materialType: String?,

    @ColumnInfo(name = "can_be_recycled")
    val canBeRecycled: Boolean?,

    @ColumnInfo(name = "recycle_process")
    val recycleProcess: String?,

    @ColumnInfo(name = "possible_products")
    val possibleProducts: String?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?
)
