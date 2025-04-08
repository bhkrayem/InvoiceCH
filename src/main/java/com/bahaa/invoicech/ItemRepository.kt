package com.bahaa.invoicech


// import com.bahaa.invoicech.ItemDao
// import com.bahaa.invoicech.ItemEntity
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    suspend fun insertItem(item: ItemEntity) = itemDao.insertItem(item)
    fun getAllItems(): Flow<List<ItemEntity>> = itemDao.getAllItems() // Remove suspend, return Flow
}