package com.store.clientservercomponent.models.user.transaction

data class TransactionItem(
    val id: Long, // ID элемента
    val created: Long, // 2019-03-22T09:05:48.830940Z
    val updated: Long, // 2019-03-22T09:05:48.830940Z
    val cost: Int, // Цена сущности
    val discount: Int, // Скидка
    val user_transaction: String, // ID транзакции
    val product: Long?, // Если заполнено, то продукт является покупаемой сущностью
    val product_subscription: Int?, // Если заполнено, то продукт является покупаемой сущностью
    val bundle: Int?, // Если заполнено, то продукт является покупаемой сущностью
    val subscription: Int? // Если заполнено, то продукт является покупаемой сущностью
)