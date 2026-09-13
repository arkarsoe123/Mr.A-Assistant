package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.UserTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM user_tasks WHERE userId = :userId ORDER BY isCompleted ASC, createdAt DESC")
    fun getTasksForUser(userId: Long): Flow<List<UserTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: UserTaskEntity): Long

    @Update
    suspend fun updateTask(task: UserTaskEntity)

    @Query("UPDATE user_tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun setTaskCompleted(id: Long, completed: Boolean)

    @Query("DELETE FROM user_tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("DELETE FROM user_tasks WHERE userId = :userId AND isCompleted = 1")
    suspend fun clearCompletedTasks(userId: Long)
}
