package com.store.clientservercomponent.models.user.library

import com.store.clientservercomponent.models.product.Product
import com.store.clientservercomponent.models.user.transaction.Transaction

data class UserLibrary(
    val product: Product,
    val transaction: Transaction
)