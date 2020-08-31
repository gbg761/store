package com.store.clientservercomponent.data.managers.user

interface InvalidRefreshTokenListener {

    fun onInvalidRefreshTokenListener(t: Throwable)
}