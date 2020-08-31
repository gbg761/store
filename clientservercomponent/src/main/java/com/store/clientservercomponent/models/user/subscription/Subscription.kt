package com.store.clientservercomponent.models.user.subscription

data class Subscription(
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val locale: String, //Код локали
    val cost: Int, // Стоимость подписки
    val duration: Int // Продолжительность подписки (в днях)
)