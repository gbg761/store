package com.store.clientservercomponent.data.network.module

import com.store.clientservercomponent.models.ServerHealth
import com.store.clientservercomponent.models.bundle.Bundle
import com.store.clientservercomponent.models.bundle.Bundles
import com.store.clientservercomponent.models.collections.Collection
import com.store.clientservercomponent.models.collections.Collections
import com.store.clientservercomponent.models.developer.Developer
import com.store.clientservercomponent.models.developer.Developers
import com.store.clientservercomponent.models.product.Build
import com.store.clientservercomponent.models.product.Product
import com.store.clientservercomponent.models.product.Products
import com.store.clientservercomponent.models.publisher.Publisher
import com.store.clientservercomponent.models.publisher.Publishers
import com.store.clientservercomponent.models.tokens.AccessToken
import com.store.clientservercomponent.models.tokens.RefreshToken
import com.store.clientservercomponent.models.user.authorization.AuthorizationResponse
import com.store.clientservercomponent.models.user.authorization.UserLogin
import com.store.clientservercomponent.models.user.library.UserLibrary
import com.store.clientservercomponent.models.user.registraion.UserRegistration
import com.store.clientservercomponent.models.user.subscription.Subscriptions
import com.store.clientservercomponent.models.user.transaction.Transaction
import com.store.clientservercomponent.models.user.transaction.TransactionRequest
import com.store.clientservercomponent.models.user.transaction.Transactions
import com.store.clientservercomponent.models.user.user.User
import com.store.clientservercomponent.models.user.user.Users
import com.store.clientservercomponent.models.user.wishlist.Wishlist
import com.store.clientservercomponent.models.user.wishlist.WishlistItem
import com.store.clientservercomponent.models.user.wishlist.WishlistProduct
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Url

interface RestModule {

    // обновить access токен
    fun refreshToken(token: String)

    // запрос к серверу для обновления токена
    fun refreshToken(refreshToken: RefreshToken): Single<AccessToken>

    // Получение списка издателей
    // limit - Количество элементов в выдаче
    // offset - Смещение элементов для пагинации
    fun getPublishers(limit: Int, offset: Int): Single<Publishers>

    // Получение данных об издателе
    fun getPublisher(id: Long): Single<Publisher>

    //////////////////////////////////////////////////////////////////////////////////////

    // Получение списка разработчиков
    fun getDevelopers(limit: Int, offset: Int): Single<Developers>

    // Получение данных о разработчике
    fun getDeveloper(id: Long): Single<Developer>

    //////////////////////////////////////////////////////////////////////////////////////

    // Получение списка продуктов
    // locale - Код локали пользователя
    fun getProducts(limit: Int, offset: Int): Single<Products>

    // Получение продукта
    fun getProduct(productId: Long): Single<Product>

    // Получение актуального билда продукта
    // platform - Код платформы билда
    fun getCurrentBuild(productId: Long): Single<Build>

    // Получение списка бандлов
    fun getBundles(limit: Int, offset: Int): Single<Bundles>

    //Получение бандла
    fun getBundle(bundleId: Long): Single<Bundle>

    //////////////////////////////////////////////////////////////////////////////////////

    //Получение списка подборок
    fun getCollections(limit: Int, offset: Int): Single<Collections>

    //Получение подборки
    fun getCollection(collectionId: Long): Single<Collection>

    //////////////////////////////////////////////////////////////////////////////////////

    // Создание транзакции. Покупка продукта/бандла/подписки происходит через этот метод
    fun buy(transactionRequest: TransactionRequest): Single<Transaction>

    //////////////////////////////////////////////////////////////////////////////////////

    // регистрация нового пользователя
    fun registerUser(userRegistration: UserRegistration): Completable

    //Авторизация пользователя
    fun authorizeUser(userLogin: UserLogin): Single<AuthorizationResponse>

    //////////////////////////////////////////////////////////////////////////////////////

    //Получение списка пользователей
    fun getUsers(limit: Int, offset: Int): Single<Users>

    // Получение пользователя
    fun getUser(): Single<User>

    // Получение истории транзакций пользователя
    fun getUserTransactions(limit: Int, offset: Int): Single<Transactions>

    // Получение всех транзакций пользователя
    fun getAllUserTransactions(): Single<Transactions>

    // Получение транзакции
    fun getUserTransaction(transactionId: Long): Single<Transaction>

    // Получение истории подписок пользователя
    fun getUserSubscriptions(limit: Int, offset: Int): Single<Subscriptions>

    //Получение списка желаемых продуктов пользователя
    fun getUserWishlist(limit: Int, offset: Int): Single<Wishlist>

    //Создание записи в список желаемых продуктов пользователя
    fun addInUserWishlist(wishlistProduct: WishlistProduct): Single<WishlistItem>

    //Получение библиотеки пользователя
    fun getUserLibrary(limit: Int, offset: Int): Single<List<UserLibrary>>

    // Удаление записи из списка желаемых продуктов пользователя
    fun deleteFromUserWishlist(productId: Long): Completable

    //////////////////////////////////////////////////////////////////////////////////////

    // скачиваем apk файл с сервера
    fun downloadApkFile(@Url url: String): Observable<Response<ResponseBody>>

    fun downloadFile(fileUrl: String): Call<ResponseBody>

    // Проверка доступности сервера
    fun checkServerHealth(): Single<ServerHealth>
}