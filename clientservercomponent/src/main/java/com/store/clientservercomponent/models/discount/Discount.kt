package com.store.clientservercomponent.models.discount

data class Discount(
    val created: Long,
    val updated: Long,
    val discount: Int,
    val until: Long
)