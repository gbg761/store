package com.store.clientservercomponent.models.publisher

data class Publishers(
    val count: Long, // Общее количество доступных элементов
    val next: String, // Ссылка на следующую страницу
    val previous: String, // Ссылка на предыдущую страницу
    val results: List<Publisher>
)