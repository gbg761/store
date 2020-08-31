package com.store.clientservercomponent.models.bundle

data class Bundles(
    val count: Long, // Общее количество доступных элементов
    val next: String?, // Ссылка на следующую страницу
    val previous: String?, // Ссылка на предыдущую страницу
    var results: MutableList<Bundle>
)