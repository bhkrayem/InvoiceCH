// ItemDao.kt - Fix the package name
package com.bahaa.invoicech // Changed from just "com"

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item: ItemEntity)

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<ItemEntity>>
}