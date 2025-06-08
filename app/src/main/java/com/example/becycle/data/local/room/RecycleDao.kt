package com.example.becycle.data.local.room
import com.example.becycle.data.local.entity.RecycleEntity
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecycleDao {
    @Query("SELECT * FROM recycle")
    fun getAllRecycleData(): LiveData<List<RecycleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecycle(recycle: RecycleEntity)

    @Update
    suspend fun updateRecycle(recycle: RecycleEntity)

    @Query("DELETE FROM recycle WHERE recycle_id = :recycleId")
    suspend fun deleteRecycleById(recycleId: Int)
}
