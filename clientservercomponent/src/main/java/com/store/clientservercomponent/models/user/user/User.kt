package com.store.clientservercomponent.models.user.user

data class User(
    val id: Long, // ID элемента
    val created: Long, // Timestamp дата создания
    val updated: Long, // Timestamp дата последнего обновления
    val login: String, // Логин пользователя
    val email: String, // E-mail адрес пользователя
    val avatar: String, // Ссылка на аватар пользователя
    val subscribed: Boolean // Флаг активности подписки
)