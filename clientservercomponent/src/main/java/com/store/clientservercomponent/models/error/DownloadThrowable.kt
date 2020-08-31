package com.store.clientservercomponent.models.error

class DownloadThrowable(productId: Long, message: String?) : WriteFileThrowable(productId, message)