package com.store.clientservercomponent.data.managers.download

import android.content.Context

import io.reactivex.subjects.Subject
import okhttp3.ResponseBody
import java.io.*
import java.util.concurrent.Callable

class DownloadFileCallable(
    private val context: Context,
    private val productId: Long,
    private val body: ResponseBody?,
    private val subject: Subject<Progress>
) :
    Callable<String> {

    var progressCurrent = 0

    @Throws(IOException::class)
    override fun call() = writeResponseBodyToDisk()

    @Throws(IOException::class)
    private fun writeResponseBodyToDisk(): String {

        if (body == null) throw IOException()
        else {

            val inputStream: InputStream = body.byteStream()

            val tempFile =
                File("${context.getExternalFilesDir(null)}" + File.separator + "$productId.temp")

            val fileReader = ByteArray(4096)

            val fileSize = body.contentLength()
            var fileSizeDownloaded = 0L

            val outputStream = FileOutputStream(tempFile)

            while (true) {
                val read = inputStream.read(fileReader)

                if (read == -1) {
                    break
                }

                outputStream.write(fileReader, 0, read)

                fileSizeDownloaded += read.toLong()

                var progress = calculateProgress(fileSizeDownloaded, fileSize)
                var progressInt = (progress*100F).toInt()
                if(progressInt > progressCurrent) {
                    progressCurrent = progressInt
                    subject
                        .onNext(
                            Progress(
                                productId,
                                progress
                            )
                        )
                }
            }

            outputStream.flush()

            val filename = "$productId.apk"
            val apkFile =
                File("${context.getExternalFilesDir(null)}" + File.separator + filename)
            if (!tempFile.renameTo(apkFile))
                throw IOException()

            inputStream.close()
            outputStream.close()

            return context.getFileStreamPath(filename).absolutePath
        }
    }

    private fun calculateProgress(fileSizeDownloaded: Long, fileSize: Long) =
        (fileSizeDownloaded.toDouble() / fileSize)
}