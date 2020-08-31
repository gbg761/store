package com.store.clientservercomponent.data.managers.authorization

import com.store.clientservercomponent.Carry
import com.store.clientservercomponent.constants.Constants
import com.store.clientservercomponent.models.user.authorization.Success
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

// регистрация и авторизация
interface AuthorizationManager {

    // регистрация нового пользователя
    fun registerUser(login: String, email: String, password: String, carry: Carry<Unit>)

    //Авторизация пользователя
    fun authorizeUser(login: String, password: String, carry: Carry<Success>)

    fun subscribe(observer: Observer<Constants.AuthorizationStatus>)

    fun unsubscribe(d: Disposable)

    fun getAuthorizationStatus(): Constants.AuthorizationStatus
}