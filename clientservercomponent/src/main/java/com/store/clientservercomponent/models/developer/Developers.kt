package com.store.clientservercomponent.models.developer

data class Developers(
    val count: Long, // Общее количество доступных элементов
    val next: String?, // Ссылка на следующую страницу
    val previous: String?, // Ссылка на предыдущую страницу
    val results: List<Developer>
)