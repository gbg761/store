package com.store.clientservercomponent.models.user.transaction

import com.store.clientservercomponent.constants.Constants

data class TransactionRequest(
    val items: List<Item>
) {
    // Оплачиваемые сущности
    data class Item(
        val item_id: Long, // ID продукта/бандла/подписки
        val item_type: String, // Тип сущности, которую нужно оплатить
        val subscription_id: Long? // ID подписки, по которой берется продукт
    )

    companion object Builder {

        fun build(productId: Long): TransactionRequest {
            val t = TransactionRequest(listOf(Item(productId, Constants.PaidEntity.PRODUCT.entity, null)))
            return t
        }
    }
}