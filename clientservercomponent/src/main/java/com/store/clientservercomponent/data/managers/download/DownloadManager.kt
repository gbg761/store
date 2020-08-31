package com.store.clientservercomponent.data.managers.download

import com.store.clientservercomponent.constants.Constants

interface DownloadManager {

    // Получение актуального билда продукта
    fun download(productId: Long, listener: OnChangeProgressListener)

    // отмена скачивания по id
    fun cancelDownload(productId: Long)

    // получить статус продукта
    fun getProductDownloadStatus(productId: Long): Constants.DownloadStatus

    // добавить слушатель прогресса
    fun addChangeProgressListener(
        productId: Long,
        listener: OnChangeProgressListener
    )

    // удалить слушатель прогресса
    fun removeChangeProgressListeners(productId: Long, listener: OnChangeProgressListener)

    // возвращает расположение файла на диске
    fun getFilePath(productId: Long): String?

    // удаление файла с диска
    fun deleteFile(productId: Long)
}