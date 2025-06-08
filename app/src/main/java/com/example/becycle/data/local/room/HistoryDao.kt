package com.example.becycle.data.local.room
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.becycle.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getHistory(): LiveData<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    @Update
    suspend fun updateHistory(history: HistoryEntity)

    @Query("DELETE FROM history WHERE history_id = :historyId")
    suspend fun deleteHistoryById(historyId: Int)
}
