package com.store.clientservercomponent.models.user.wishlist

data class Wishlist(
    val count: Int, // Общее количество доступных элементов
    val next: String?, // Ссылка на следующую страницу
    val previous: String?, // Ссылка на предыдущую страницу
    val results: List<WishlistItem>
)