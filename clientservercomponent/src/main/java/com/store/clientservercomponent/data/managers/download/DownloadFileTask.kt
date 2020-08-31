package com.store.clientservercomponent.data.managers.download

import okhttp3.ResponseBody
import retrofit2.Call
import java.lang.ref.WeakReference

class DownloadFileTask(
    val productId: Long,
    val retrofitTask: Call<ResponseBody>,
    var changeProgressListenerList: MutableList<WeakReference<OnChangeProgressListener>> = mutableListOf()
)