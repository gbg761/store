package com.store.clientservercomponent.models.user.authorization

data class UserLogin(
    val login: String, // Логин пользователя
    val provider: String, // Тип входа логин-пароль или один из ключей соцсети
    val meta: Meta
) {
    data class Meta(
        val password: String, // Пароль пользователя
        val access_token: String? // Токен, полученый от социальной сети, для выполнения запросов к API
    )

    companion object Builder {

        const val PROVIDER_EMAIL = "email"

        fun buildWithEmailProvider(login: String, password: String) =
            UserLogin(login, PROVIDER_EMAIL, Meta(password, null))
    }
}