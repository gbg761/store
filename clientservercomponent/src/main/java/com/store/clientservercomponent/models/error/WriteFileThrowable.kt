package com.store.clientservercomponent.models.error

open class WriteFileThrowable(
    val productId: Long,
    message: String?
) : Throwable(message)