package com.store.clientservercomponent.models.user.subscription

data class UserSubscription(
    val id: Long, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val until: Long, // Timestamp даты окончания подписки
    val cost: Int, // Стоимость подписки
    val currency: String // Код валюты
)