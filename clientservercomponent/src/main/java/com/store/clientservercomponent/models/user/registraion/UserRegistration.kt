package com.store.clientservercomponent.models.user.registraion

// Регистрация через логин-пароль/социальную сеть.
data class UserRegistration(
    val login: String, // Логин пользователя
    val provider: String, // Тип регистрации логин-пароль (в нашем случае provider  = "email")
    val meta: Meta
) {
    data class Meta(
        val email: String,
        val password: String,
        val social_id: String? = null,
        val access_token: String? = null
    )

    companion object Builder {

        const val PROVIDER_EMAIL = "email"

        fun buildWithEmailProvider(login: String, email: String, password: String) =
            UserRegistration(login, PROVIDER_EMAIL, Meta(email, password))
    }
}