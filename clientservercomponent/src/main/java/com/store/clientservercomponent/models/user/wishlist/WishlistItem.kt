package com.store.clientservercomponent.models.user.wishlist

import com.store.clientservercomponent.models.product.Product

data class WishlistItem(
    val id: Long, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val product: Product
)
