package com.store.clientservercomponent.models.collections

import com.store.clientservercomponent.models.product.Product

data class Collection(
    val id: Long, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val name: String, // Название подборки
    var products: MutableList<Product>
)