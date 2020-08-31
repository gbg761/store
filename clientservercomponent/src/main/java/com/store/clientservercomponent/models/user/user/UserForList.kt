package com.store.clientservercomponent.models.user.user

data class UserForList(
    val id: Long, // ID элемент
    val login: String, // Логин пользователя
    val avatar: String // Ссылка на аватар пользователя
)