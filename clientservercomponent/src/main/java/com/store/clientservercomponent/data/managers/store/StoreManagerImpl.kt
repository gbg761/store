package com.store.clientservercomponent.data.managers.store

import com.store.clientservercomponent.data.managers.authorization.AuthorizationManager
import com.store.clientservercomponent.data.managers.connection.ConnectionManager
import com.store.clientservercomponent.data.managers.download.DownloadManager
import com.store.clientservercomponent.data.managers.product.ProductManager
import com.store.clientservercomponent.data.managers.publisher.PublisherManager
import com.store.clientservercomponent.data.managers.purchase.PurchaseManager
import com.store.clientservercomponent.data.managers.user.UserManager

data class StoreManagerImpl(
    val authorizationManager: AuthorizationManager,
    val connectionManager: ConnectionManager,
    val productManager: ProductManager,
    val downloadManager: DownloadManager,
    val publisherManager: PublisherManager,
    val purchaseManager: PurchaseManager,
    val userManager: UserManager
)