package com.store.clientservercomponent.models.publisher

data class Publisher(
    val id: Long, // ID элемента
    val created: Long, // дата создания
    val updated: Long, // дата последнего обновления
    val name: String // Наименование издателя
)