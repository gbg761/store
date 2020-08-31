package com.store.clientservercomponent.models.user.transaction

data class Transaction(
    val id: String, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val user: Long, // ID пользователя
    val transaction_items: List<TransactionItem>,
    val price: Int, // Итоговая цена продукта
    val status: String // Статус транзакции
)