package com.store.clientservercomponent.models.bundle

import com.store.clientservercomponent.models.discount.Discount
import com.store.clientservercomponent.models.product.Product

data class Bundle(
    val id: Long, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val name: String, // Название бандла
    val cost: Int, // Стоимость бандла
    val discount: Discount?,
    val price: Int, // Итоговая цена бандла
    var products: MutableList<Product>
)