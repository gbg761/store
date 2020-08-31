package com.store.clientservercomponent.data.managers

import android.content.Context
import android.util.Log
import com.store.clientservercomponent.R
import com.store.clientservercomponent.models.error.ResponseThrowable
import com.store.clientservercomponent.constants.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

abstract class BaseManager(private val context: Context) {

    fun handleError(t: Throwable): ResponseThrowable = when (t) {

        is IOException -> // отсуствие интернет - соединения

            ResponseThrowable(
                Constants.NO_INTERNET_CONNECTION_ID,
                ResponseThrowable.ThrowableType.NO_INTERNET_CONNECTION,
                context.resources.getString(R.string.no_internet_connection)
            )

        is HttpException -> { // http ошибки

            when (t.code()) {

                ResponseThrowable.ThrowableType.BAD_REQUEST.code -> { // ошибка 400 : Плохой запрос или ошибка локали, или
                    // пустые поля при регистрации/авторизации

                    ResponseThrowable(
                        t.code(),
                        ResponseThrowable.ThrowableType.BAD_REQUEST,
                        getBadRequestErrorMessage(t.response()?.errorBody()?.string())
                    )
                }

                ResponseThrowable.ThrowableType.INVALID_DATA.code -> { // ошибка 401 : Неавторизованый запрос (например, истек срок токена или пользователь не залогинен)

                    ResponseThrowable(
                        t.code(),
                        ResponseThrowable.ThrowableType.INVALID_DATA,
                        getErrorMessage(t.response()?.errorBody()?.string())
                    )
                }

                ResponseThrowable.ThrowableType.ILLEGAL_OPERATION.code -> { // ошибка 403 : Запрещенная операция (например, у вас нет прав на это)

                    ResponseThrowable(
                        t.code(),
                        ResponseThrowable.ThrowableType.ILLEGAL_OPERATION,
                        getErrorMessage(t.response()?.errorBody()?.string())
                    )
                }

                ResponseThrowable.ThrowableType.ENTITY_NOT_FOUND.code -> { // ошибка 404 : Сущность не найдена

                    ResponseThrowable(
                        t.code(),
                        ResponseThrowable.ThrowableType.ENTITY_NOT_FOUND,
                        getErrorMessage(t.response()?.errorBody()?.string())
                    )
                }

                ResponseThrowable.ThrowableType.USER_EXIST_ERROR.code -> { // ошибка 409

                    ResponseThrowable(
                        t.code(),
                        ResponseThrowable.ThrowableType.USER_EXIST_ERROR,
                        getErrorMessage(t.response()?.errorBody()?.string())
                    )
                }

                ResponseThrowable.ThrowableType.SERVER_ERROR.code -> { // ошибка 500 : Ошибка на сервере

                    ResponseThrowable(
                        t.code(),
                        ResponseThrowable.ThrowableType.SERVER_ERROR,
                        context.resources.getString(R.string.server_error)
                    )
                }

                else -> { // ошибка 700 : неизвестная ошибка

                    ResponseThrowable(
                        ResponseThrowable.ThrowableType.UNKNOWN_ERROR.code,
                        ResponseThrowable.ThrowableType.UNKNOWN_ERROR,
                        context.resources.getString(R.string.unknown_error)
                    )
                }
            }

        }

        else -> { // ошибка парсинга

            ResponseThrowable(
                ResponseThrowable.ThrowableType.PARSE_ERROR.code,
                ResponseThrowable.ThrowableType.PARSE_ERROR,
                t.message
            )
        }
    }

    //fun handleErrorDownloadingFile()

    // проверяет валидность токена
    // ResponseThrowable.ThrowableType.INVALID_DATA.code = 401,
    // т.е. неавторизованый запрос (например, истек срок токена или
    //пользователь не залогинен)
    fun isInvalidToken(e: HttpException) =
        e.code() == ResponseThrowable.ThrowableType.INVALID_DATA.code

    // так как json для ошики 400 (пустые поля при регистрации/авторизации) отличается от json
    // для остальных ошибок, необходимо их обработать по-разному
    // getBadRequestErrorMessage обрабатывает ошибку 400
    private fun getBadRequestErrorMessage(json: String?): String = if (json != null) {
        Log.d("tag_crash_locale", "BM getBadRequestErrorMessage json: $json")
        val jsonObj = JSONObject(json)
        if (jsonObj.get("detail") == "Product have not requested locale.") {
            "Product have not requested locale."
        } else {
            // конвертирует json строку в map
            // из которой затем получаем текст сообщения об ошибке
            val mapType = object : TypeToken<HashMap<String, List<String>>>() {}.type
            val map: Map<String, List<String>> = Gson().fromJson(json, mapType)
            val keys: Array<String> = map.keys.toTypedArray()

            val msgList = map[keys[0]]

            // строка вывода
            keys[0] + ": " + (msgList?.get(0)
                ?: context.resources.getString(R.string.empty_field_error))
        }
    } else {
        ""
    }

    // getErrorMessage обрабатывает все остальные ошибки
    private fun getErrorMessage(json: String?): String = if (json != null) {

        val mapType = object : TypeToken<HashMap<String, String>>() {}.type

        val map: Map<String, String> = Gson().fromJson(json, mapType)
        val keys: Array<String> = map.keys.toTypedArray()

        map[keys[0]].toString()
    } else {
        ""
    }
}