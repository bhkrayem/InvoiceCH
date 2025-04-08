package com.bahaa.invoicech

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert
    suspend fun insertCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE name LIKE :query")
    fun searchCustomers(query: String): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers")
    suspend fun getAllCustomersOnce(): List<CustomerEntity>

}