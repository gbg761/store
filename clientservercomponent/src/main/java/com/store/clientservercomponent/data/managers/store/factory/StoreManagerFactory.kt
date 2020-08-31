package com.store.clientservercomponent.data.managers.store.factory

import android.content.Context
import com.store.clientservercomponent.constants.Constants
import com.store.clientservercomponent.data.managers.authorization.AuthorizationManagerImpl
import com.store.clientservercomponent.data.managers.connection.ConnectionManagerImpl
import com.store.clientservercomponent.data.managers.download.DownloadManagerImpl
import com.store.clientservercomponent.data.managers.product.ProductManagerImpl
import com.store.clientservercomponent.data.managers.publisher.PublisherManagerImpl
import com.store.clientservercomponent.data.managers.purchase.PurchaseManagerImpl
import com.store.clientservercomponent.data.managers.store.StoreManagerImpl
import com.store.clientservercomponent.data.managers.user.UserManagerImpl
import com.store.clientservercomponent.data.network.module.RestModule
import com.store.clientservercomponent.data.network.module.RestModuleImpl
import com.store.clientservercomponent.data.network.service.RestService
import com.store.clientservercomponent.data.network.service.api.GameStoreApi
import com.store.clientservercomponent.data.storage.module.StorageModule
import com.store.clientservercomponent.data.storage.module.StorageModuleImpl

class StoreManagerFactory {

    companion object {

        fun createStoreManager(context: Context): StoreManagerImpl {

            val restService = RestService()
            val gameStoreApi = restService.retrofit.create(GameStoreApi::class.java)

            val storageModule: StorageModule = StorageModuleImpl(context)

            // возващает из preferences токен доступа
            // который пользователь получает после авторизации
            val token = storageModule.getToken(Constants.ACCESS_TOKEN)
            val restModule: RestModule = RestModuleImpl(gameStoreApi, token)

            val authManager = AuthorizationManagerImpl(context, restModule, storageModule)

            return StoreManagerImpl(
                authManager,
                ConnectionManagerImpl(context, restModule),
                ProductManagerImpl(context, restModule, authManager),
                DownloadManagerImpl(context, restModule),
                PublisherManagerImpl(context, restModule),
                PurchaseManagerImpl(context, restModule, authManager),
                UserManagerImpl(context, restModule, authManager)
            )
        }
    }
}