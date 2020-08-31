package com.store.clientservercomponent.models.product

data class Build(
    val id: Long, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val product_id: Long, // ID продукта
    val platform: String, // Код платформы сборки
    val version: String, // Версия сборки
    var buildfile: String, // Ссылка на файл сборки
    val buildfile_size: Int // Размер файла сборки в КБ
)