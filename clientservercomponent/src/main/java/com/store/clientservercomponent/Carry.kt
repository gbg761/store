package com.store.clientservercomponent

import com.store.clientservercomponent.models.error.ResponseThrowable

interface Carry<T> {

    fun onSuccess(result: T)

    fun onFailure(throwable: ResponseThrowable)
}