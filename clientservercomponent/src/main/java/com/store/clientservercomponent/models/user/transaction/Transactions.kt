package com.store.clientservercomponent.models.user.transaction

data class Transactions(
    val count: Long, // Общее количество доступных элементов
    val next: String?, // Ссылка на следующую страницу
    val previous: String?, // Ссылка на предыдущую страницу
    val results: List<Transaction>
)