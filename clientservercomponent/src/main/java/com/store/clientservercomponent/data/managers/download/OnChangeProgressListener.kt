package com.store.clientservercomponent.data.managers.download

interface OnChangeProgressListener {

    fun onChangeProgress(progress: Double, productId: Long)

    fun onFailure(t: Throwable, productId: Long)

    fun onComplete(filePath: String, productId: Long)
}