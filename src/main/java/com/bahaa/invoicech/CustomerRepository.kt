package com.bahaa.invoicech

import kotlinx.coroutines.flow.Flow

class CustomerRepository(private val customerDao: CustomerDao) {
    suspend fun insertCustomer(customer: CustomerEntity) = customerDao.insertCustomer(customer)
    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    fun searchCustomers(query: String): Flow<List<CustomerEntity>> = customerDao.searchCustomers("%$query%")
}