package com.store.clientservercomponent.models.error

class ResponseThrowable(
    // http код ошибки
    val id: Int,
    val type: ThrowableType, // Тип ошибки, значение из Enum : ResponseThrowable
    message: String? // описание ошибки
) : Throwable(message) {
    enum class ThrowableType(val code: Int) {
        BAD_REQUEST(400),
        INVALID_DATA(401),
        ILLEGAL_OPERATION(403),
        ENTITY_NOT_FOUND(404),
        USER_EXIST_ERROR(409),
        SERVER_ERROR(500),
        CONNECTION_TIMED_OUT(522),
        PARSE_ERROR(600),
        NO_INTERNET_CONNECTION(601),
        UNKNOWN_ERROR(700),
        DOWNLOAD_TASK_CANCEL(800)
    }
}