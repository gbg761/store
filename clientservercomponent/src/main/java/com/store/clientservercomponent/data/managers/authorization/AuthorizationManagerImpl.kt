package com.store.clientservercomponent.data.managers.authorization

import android.content.Context
import android.util.Log
import com.store.clientservercomponent.Carry
import com.store.clientservercomponent.constants.Constants
import com.store.clientservercomponent.data.managers.BaseManager
import com.store.clientservercomponent.data.network.module.RestModule
import com.store.clientservercomponent.data.storage.module.StorageModule
import com.store.clientservercomponent.models.tokens.AccessToken
import com.store.clientservercomponent.models.tokens.RefreshToken
import com.store.clientservercomponent.models.user.authorization.AuthorizationResponse
import com.store.clientservercomponent.models.user.authorization.Success
import com.store.clientservercomponent.models.user.authorization.UserLogin
import com.store.clientservercomponent.models.user.registraion.UserRegistration
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

class AuthorizationManagerImpl(
    context: Context,
    private val restModule: RestModule,
    private val storageModule: StorageModule
) : BaseManager(context),
    AuthorizationManager,
    InvalidAccessTokenListener {

    private val tag = "AuthorizationManager"

    private var authorizationStatus: Variable<Constants.AuthorizationStatus>

    // при инициализации выставляем статус
    init {
        authorizationStatus = if (checkAuthorizationStatus()) {
            Variable(Constants.AuthorizationStatus.AUTHORIZED)
        } else {
            Variable(Constants.AuthorizationStatus.UNDEFINED)
        }
    }

    override fun registerUser(
        login: String,
        email: String,
        password: String,
        carry: Carry<Unit>
    ) {

        val d = restModule.registerUser(
            UserRegistration.Builder.buildWithEmailProvider(
                login,
                email,
                password
            )
        ).subscribe(
            { carry.onSuccess(Unit) },
            { t: Throwable -> carry.onFailure(handleError(t)) }
        )
    }

    override fun authorizeUser(login: String, password: String, carry: Carry<Success>) {

        val d = restModule.authorizeUser(UserLogin.Builder.buildWithEmailProvider(login, password))
            .subscribe(
                { t: AuthorizationResponse ->
                    saveAuthorization(t)
                    restModule.refreshToken(t.access)
                    authorizationStatus.value = Constants.AuthorizationStatus.AUTHORIZED
                    carry.onSuccess(Success(true))
                },
                { t: Throwable ->
                    carry.onFailure(handleError(t))
                }
            )
    }

    // Обновление access-токена
    private fun refreshToken(refreshToken: RefreshToken) =
        restModule.refreshToken(refreshToken)

    // проверяем, авторизован юзер или нет
    private fun checkAuthorizationStatus() = storageModule.checkAuthorizationStatus()

    private fun saveAuthorization(authorizationResponse: AuthorizationResponse) =
        storageModule.saveAuthorization(authorizationResponse)

    override fun refreshInvalidAccessToken() {

        Log.d(tag, "on refreshInvalidAccessToken()")

        // оповещаем подсписчиков о смене статуса
        authorizationStatus.value = Constants.AuthorizationStatus.NOT_AUTHORIZED

        val refreshToken = storageModule.getToken(Constants.REFRESH_TOKEN)

        //val refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCIsImV4cCI6MTU2ODQ1MTEwOCwianRpIjoiZTZlMjMwZWU3NDgyNDQxMWE2NzU0NGU2ODBmNDFlZjciLCJ1c2VyX2lkIjo3fQ.2wH-in_NEOsUIil7qsNQxSl45LrXJy9eXyMTBvVPXtw"
        val d = refreshToken(RefreshToken(refreshToken)).subscribe(
            { t: AccessToken ->

                authorizationStatus.value = Constants.AuthorizationStatus.AUTHORIZED
                restModule.refreshToken(t.access)
                storageModule.saveToken(Constants.ACCESS_TOKEN, t.access)
            },
            { t: Throwable ->
                Log.d("TAG", handleError(t).message)
                if (t is HttpException)
                    authorizationStatus.value =
                        Constants.AuthorizationStatus.SHOW_LOGIN_SCREEN
            })
    }

    override fun subscribe(observer: Observer<Constants.AuthorizationStatus>) = authorizationStatus.observable.subscribe(observer)

    override fun unsubscribe(d: Disposable) = d.dispose()

    override fun getAuthorizationStatus() = authorizationStatus.value
}