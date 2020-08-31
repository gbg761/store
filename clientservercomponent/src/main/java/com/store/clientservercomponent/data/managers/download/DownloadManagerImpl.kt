package com.store.clientservercomponent.data.managers.download

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.store.clientservercomponent.R
import com.store.clientservercomponent.constants.Constants
import com.store.clientservercomponent.data.managers.BaseManager
import com.store.clientservercomponent.data.network.module.RestModule
import com.store.clientservercomponent.extensions.models.addHost
import com.store.clientservercomponent.models.error.DownloadThrowable
import com.store.clientservercomponent.models.error.ResponseThrowable
import com.store.clientservercomponent.models.error.WriteFileThrowable
import com.store.clientservercomponent.models.product.Build
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

class DownloadManagerImpl(private val context: Context, private val restModule: RestModule) :
    BaseManager(context),
    DownloadManager,
    Observer<Progress> {

    private val downloadFileTaskMap = hashMapOf<Long, DownloadFileTask>()

    // используем для передачи значения прогресса из разных потоков
    private val subject: PublishSubject<Progress> = PublishSubject.create()

    init {
        subject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this)
    }

    @SuppressLint("CheckResult")
    override fun download(productId: Long, listener: OnChangeProgressListener) {

        // полуить текущий билд продукта с сервера
        restModule.getCurrentBuild(productId).subscribe(
            { t: Build ->
                // добавляем к хост ссылке на apk файл
                t.addHost()
                Log.d(
                    "tage_download",
                    "DowM download fillProgress Thread: ${Thread.currentThread().name}"
                )
                downloadFile(productId, t.buildfile, listener)
            },
            { t: Throwable ->
                listener.onFailure(handleError(t), productId)
            }
        )
    }

    private fun downloadFile(productId: Long, fileUrl: String, listener: OnChangeProgressListener) {

        val call: Call<ResponseBody> = restModule.downloadFile(fileUrl)

        // создание задачи скачивания
        val downloadFileTask = DownloadFileTask(
            productId = productId,
            retrofitTask = call
        )

        // добавляем слушатель прогресса в список
        downloadFileTask.changeProgressListenerList.add(WeakReference(listener))

        // добавить DownloadFileTask задачу на скачивание  в коллецию задач
        addDownloadFileTaskToMap(productId, downloadFileTask)

        // асинхронный запрос к серверу для получения apk файла
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    // сохранение файла на диск
                    Single.fromCallable(
                        DownloadFileCallable(
                            context,
                            productId,
                            response.body(),
                            subject.toSerialized()
                        )
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { filePath: String ->
                                // уведомляем всех подписчиков о завершении скачивания
                                onCompleteDownloadTask(productId, filePath)
                            },
                            { t: Throwable ->

                                // отключился интернет во время скачивания
                                if (t is SocketTimeoutException) {

                                    // уведомляем всех подписчиков о возниконовении ошибки во время скачивания
                                    onErrorDownloadTask(
                                        productId,
                                        ResponseThrowable(
                                            ResponseThrowable.ThrowableType.CONNECTION_TIMED_OUT.code,
                                            ResponseThrowable.ThrowableType.CONNECTION_TIMED_OUT,
                                            context.getString(R.string.no_internet_connection)
                                        )
                                    )
                                } else {

                                    // IOException
                                    // если true (т.е. response.body() == null), то
                                    // unknown error
                                    // иначе возникла ошибка, связанная с сохранением файла на диск
                                    if (t.message == null) {

                                        // уведомляем всех подписчиков о возниконовении ошибки во время скачивания
                                        onErrorDownloadTask(
                                            productId,
                                            ResponseThrowable(
                                                ResponseThrowable.ThrowableType.UNKNOWN_ERROR.code,
                                                ResponseThrowable.ThrowableType.UNKNOWN_ERROR,
                                                context.getString(R.string.unknown_error)
                                            )
                                        )
                                    } else {

                                        // уведомляем всех подписчиков о возниконовении ошибки во время скачивания
                                        onErrorDownloadTask(
                                            productId,
                                            WriteFileThrowable(productId, t.message)
                                        )
                                    }
                                }

                                // удаляем временный файл
                                deleteTemporaryFile(productId)
                            })

                } else {

                    // уведомляем всех подписчиков о возниконовении ошибки во время скачивания
                    onErrorDownloadTask(
                        productId,
                        ResponseThrowable(
                            ResponseThrowable.ThrowableType.CONNECTION_TIMED_OUT.code,
                            ResponseThrowable.ThrowableType.CONNECTION_TIMED_OUT,
                            context.getString(R.string.connection_timed_out)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (!call.isCanceled) {
                    // уведомляем всех подписчиков о возниконовении ошибки во время скачивания
                    onErrorDownloadTask(productId, handleError(t))
                }
            }
        })
    }

    // отменяем процесс загрузки приложения
    override fun cancelDownload(productId: Long) {

        downloadFileTaskMap[productId]?.retrofitTask?.cancel()

        // уведомляем всех подписчиков об отмене скачивания
        onErrorDownloadTask(
            productId,
            DownloadThrowable(productId, context.getString(R.string.download_task_cancel_error))
        )

        // удаляем временный файл
        deleteTemporaryFile(productId)
    }

    // проверяем существование файла
    private fun isDownloaded(productId: Long) =
        File("${context.getExternalFilesDir(null)}" + File.separator + "$productId.apk").exists()

    // проверяем что для данного id идет скачивание приложения
    private fun isDownloading(productId: Long) = productId in downloadFileTaskMap


    override fun onComplete() {
        // do nothing
    }

    override fun onSubscribe(d: Disposable) {
        // do nothing
    }

    // уведомляем подписчиков об изменении прогресса
    override fun onNext(t: Progress) {
        Log.d("tage_download", "DowM onNext  Thread: ${Thread.currentThread().name}")
        val weakListenerList = downloadFileTaskMap[t.productId]?.changeProgressListenerList

        weakListenerList?.let {

            for (value in weakListenerList) {

                //получаем сильную ссылку на слушателя
                val onChangeProgressListener = value.get()

                if (onChangeProgressListener == null)
                    weakListenerList.remove(value)
                else {
                    onChangeProgressListener.onChangeProgress(t.progress, t.productId)
                }
            }
        }
    }

    override fun onError(e: Throwable) {
        // do nothing
    }

    // уведомляем всех подписчиков о завершении скачивания
    private fun onCompleteDownloadTask(productId: Long, filePath: String) {

        val weakListenerList = downloadFileTaskMap[productId]?.changeProgressListenerList

        weakListenerList?.let {

            for (value in weakListenerList) {

                //получаем сильную ссылку на слушателя
                val onChangeProgressListener = value.get()
                onChangeProgressListener?.onComplete(filePath, productId)
            }
        }

        // удаляем задачу из коллекции задач и
        // отписываемся от прослушивания прогресса
        closeDownloadTask(productId)
    }

    // уведомляем всех подписчиков о возниконовении ошибки во время скачивания
    private fun onErrorDownloadTask(productId: Long, throwable: Throwable) {

        val weakListenerList = downloadFileTaskMap[productId]?.changeProgressListenerList

        weakListenerList?.let {

            for (value in weakListenerList) {

                //получаем сильную ссылку на слушателя
                val onChangeProgressListener = value.get()
                onChangeProgressListener?.onFailure(throwable, productId)
            }
        }

        // удаляем задачу из коллекции задач и
        // отписываемся от прослушивания прогресса
        closeDownloadTask(productId)
    }

    // удаляем скачанный файл
    override fun deleteFile(productId: Long) {
        File("${context.getExternalFilesDir(null)}" + File.separator + "$productId.apk").delete()
    }

    // удаляем временный файл
    private fun deleteTemporaryFile(productId: Long) =
        File("${context.getExternalFilesDir(null)}" + File.separator + "$productId.temp").delete()

    override fun getFilePath(productId: Long): String? {

        val path = "${context.getExternalFilesDir(null)}" + File.separator + "$productId.apk"
        return if (File(path).exists())
            path
        else
            null
    }

    override fun getProductDownloadStatus(productId: Long): Constants.DownloadStatus =
        when {
            isDownloaded(productId) -> Constants.DownloadStatus.DOWNLOADED
            isDownloading(productId) -> Constants.DownloadStatus.DOWNLOADING
            else -> Constants.DownloadStatus.NOT_DOWNLOADED
        }

    // добавляем слушателя прогресса скачивания
    override fun addChangeProgressListener(productId: Long, listener: OnChangeProgressListener) {

        var weakListenerList =
            downloadFileTaskMap[productId]?.changeProgressListenerList

        weakListenerList?.let {

            // если список, содержащий слабые ссылки на слушатели прогресса,
            // не содержит слабую ссылку на параметр метода listener, то
            // добавим этот listener в список
            if (!weakListenerList.contains(WeakReference(listener))) {
                weakListenerList.add(WeakReference(listener))
            }
        }
    }

    // удаляем слушателя прогресса скачивания
    override fun removeChangeProgressListeners(
        productId: Long,
        listener: OnChangeProgressListener
    ) {
        downloadFileTaskMap[productId]?.changeProgressListenerList?.remove(WeakReference(listener))
    }

    // добавляем задачу на скачивание в коллекцию задач
    private fun addDownloadFileTaskToMap(productId: Long, downloadFileTask: DownloadFileTask) {
        downloadFileTaskMap += productId to downloadFileTask
    }

    // удаляем задачу на скачивание файла из коллекции задач
    private fun removeDownloadFileTaskFromMap(productId: Long) {
        downloadFileTaskMap.remove(productId)
    }

    private fun closeDownloadTask(productId: Long) {

        // очищаем список со слушателями прогресса
        downloadFileTaskMap[productId]?.changeProgressListenerList?.clear()

        // удаляем задачу из коллекции задач
        removeDownloadFileTaskFromMap(productId)
    }
}