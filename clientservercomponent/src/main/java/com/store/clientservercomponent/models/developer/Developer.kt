package com.store.clientservercomponent.models.developer

import com.store.clientservercomponent.models.publisher.Publisher

data class Developer(
    val id: Long, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val name: String, // Название разработчика
    val email: String,
    val publisher: Publisher,
    val website: String
)