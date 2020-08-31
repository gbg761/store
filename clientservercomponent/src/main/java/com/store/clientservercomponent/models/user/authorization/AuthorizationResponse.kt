package com.store.clientservercomponent.models.user.authorization

data class AuthorizationResponse(
    val refresh: String, // Токен для обновления access-токена
    val access: String // Токен для авторизации при запросах в API
)