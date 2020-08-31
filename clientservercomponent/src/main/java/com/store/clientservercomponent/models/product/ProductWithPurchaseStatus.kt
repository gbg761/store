package com.store.clientservercomponent.models.product

import com.store.clientservercomponent.constants.Constants
import com.store.clientservercomponent.models.discount.Discount
import com.store.clientservercomponent.models.publisher.Publisher

data class ProductWithPurchaseStatus(
    val id: Long,
    var tech_name: String, // Ключ продукта
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val name: String, // Название продукта
    val short_description: String, // Короткое описание продукта
    val description: String, // Описание продукта
    val cost: Int, // Стоимость продукта
    val genres: List<String>,
    val discount: Discount?,
    val price: Int, // Итоговая цена продукта
    val publisher: Publisher,
    val release_date: Long, // Timestamp даты релиза продукта
    var header: String, // Ссылка на изображение шапки
    var screenshots: MutableList<String>, // Ссылка на скриншот
    var trailers: MutableList<String>, // Cсылка на трейлер
    val website: String, // Сайт разработчика продукта
    val license_link: String, // Ссылка на текст лицензионного соглашения
    val purchaseStatus: Constants.PurchaseStatus
)