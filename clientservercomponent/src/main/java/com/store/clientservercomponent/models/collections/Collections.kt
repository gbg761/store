package com.store.clientservercomponent.models.collections

data class Collections(
    val count: Long, // Общее количество доступных элементов
    val next: String?, // Ссылка на следующую страницу
    val previous: String?, // Ссылка на предыдущую страницу
    var results: List<Collection>
)